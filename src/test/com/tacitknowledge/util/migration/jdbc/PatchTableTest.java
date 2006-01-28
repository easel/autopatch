/* Copyright 2005 Tacit Knowledge LLC
 * 
 * Licensed under the Tacit Knowledge Open License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at http://www.tacitknowledge.com/licenses-1.0.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration.jdbc;

import java.sql.SQLException;

import junit.framework.AssertionFailedError;

import com.mockrunner.jdbc.JDBCTestCaseAdapter;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.tacitknowledge.util.migration.jdbc.util.ConnectionWrapperDataSource;

/**
 * Out-of-container tests the <code>PatchTable</code> class using a
 * mock JDBC driver. 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @version $Id: PatchTableTest.java,v 1.5 2006/01/28 03:04:47 mike Exp $
 */
public class PatchTableTest extends JDBCTestCaseAdapter
{
    /**
     * The <code>PatchTable</code> to test
     */
    private PatchTable table = null; 

    /**
     * The mock JDBC connection to use during the tests
     */
    private MockConnection conn = null;
    
    /**
     * The <code>JDBCMigrationConteext</code> used for testing
     */
    private DataSourceMigrationContext context = new DataSourceMigrationContext(); 
    
    /**
     * Constructor for PatchTableTest.
     *
     * @param name the name of the test to run
     */
    public PatchTableTest(String name)
    {
        super(name);
    }
    
    /**
     * @see com.mockrunner.jdbc.JDBCTestCaseAdapter#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        conn = getJDBCMockObjectFactory().getMockConnection();
        
        context = new DataSourceMigrationContext();
        context.setDataSource(new ConnectionWrapperDataSource(conn));
        context.setSystemName("milestone");
        context.setDatabaseType(new DatabaseType("postgres"));
        
        table = new PatchTable(context, conn);
    }
    
    /**
     * Ensures that the class throws an <code>IllegalArgumentException</code>
     * if an unknown database type is specified in the constructor. 
     */
    public void testUnknownDatabaseType()
    {
        try
        {
            context.setDatabaseType(new DatabaseType("bad-database-type"));
            new PatchTable(context, conn);
            fail("Expected IllegalArgumentException because of unknown database type");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }
    
    /**
     * Validates the automatic creation of the patches table.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testCreatePatchesTable() throws Exception
    {
        // Test-specific setup
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        h.prepareThrowsSQLException(table.getSql("level.read"));
        
        table.createPatchStoreIfNeeded();
        
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
        verifyPreparedStatementParameter(0, 1, "milestone");
        verifySQLStatementExecuted(table.getSql("patches.create"));
    }

    /**
     * Validates that the system recognizes an existing patches table.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testVerifyPatchesTable() throws Exception
    {
        // Test-specific setup
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        h.prepareGlobalResultSet(rs);
        rs.addRow(new Integer[] {new Integer(13)});
        
        table.createPatchStoreIfNeeded();
        
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
        verifyPreparedStatementParameter(0, 1, "milestone");
        verifyPreparedStatementNotPresent(table.getSql("patches.create"));
    }
    
    /**
     * Validates that <code>getPatchLevel</code> works on an existing system.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testGetPatchLevel() throws Exception
    {
        // Test-specific setup
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        rs.addRow(new Integer[] {new Integer(13)});
        h.prepareGlobalResultSet(rs);

        int i = table.getPatchLevel();

        assertEquals(13, i);
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
        verifyPreparedStatementParameter(1, 1, "milestone");
        verifyPreparedStatementNotPresent(table.getSql("level.create"));
    }

    /**
     * Validates that <code>getPatchLevel</code> works on a new system.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testGetPatchLevelFirstTime() throws Exception
    {
        // Test-specific setup
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        // empty result set
        h.prepareGlobalResultSet(rs);

        int i = table.getPatchLevel();

        assertEquals(0, i);
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
        verifyPreparedStatementPresent(table.getSql("level.create"));
    }

    /**
     * Validates that the patch level can be updated.
     *  
     * @throws Exception if an unexpected error occurs
     */
    public void testUpdatePatchLevel() throws Exception
    {
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        rs.addRow(new Integer[] {new Integer(12)});
        h.prepareResultSet(table.getSql("level.read"), rs, new String[] {"milestone"});

        table.updatePatchLevel(13);
        
        verifyPreparedStatementParameter(table.getSql("level.update"), 1, new Integer(13));
        verifyPreparedStatementParameter(table.getSql("level.update"), 2, "milestone");
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
    }
    
    /**
     * Validates that <code>isPatchTableLocked</code> works when no lock exists.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testIsPatchTableNotLocked() throws Exception
    {
        // Test-specific setup
        // Return a non-empty set in response to the patch lock query
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        rs.addRow(new String[] {"F"});
        h.prepareResultSet(table.getSql("lock.read"), rs, new String[] {"milestone"});
        
        assertFalse(table.isPatchStoreLocked());
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
    }
    
    /**
     * Validates that <code>isPatchTableLocked</code> works when a lock already exists.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testIsPatchTableLocked() throws Exception
    {
        // Test-specific setup
        // Return a non-empty set in response to the patch lock query
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        rs.addRow(new String[] {"T"});
        h.prepareResultSet(table.getSql("lock.read"), rs, new String[] {"milestone"});
        
        assertTrue(table.isPatchStoreLocked());
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
    }
    
    /**
     * Validates that an <code>IllegalStateException</code> is thrown when trying
     * to lock an already locked patches table.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testLockPatchTableWhenAlreadyLocked() throws Exception
    {
        // Test-specific setup
        // Return a non-empty set in response to the patch lock query
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        rs.addRow(new String[] {"T"});
        h.prepareResultSet(table.getSql("lock.read"), rs, new String[] {"milestone"});
        
        try
        {
            table.lockPatchStore();
            fail("Expected an IllegalStateException since a lock already exists.");
        }
        catch (IllegalStateException e)
        {
            // Expected
        }
        
        verifyPreparedStatementNotPresent(table.getSql("lock.obtain"));
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
    }

    /**
     * Validates that the patches table can be locked as long as no other lock
     * is in place.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testLockPatchTableWhenNotAlreadyLocked() throws Exception
    {
        // Test-specific setup
        // Return an empty set in response to the patch lock query
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = h.createResultSet();
        h.prepareResultSet(table.getSql("lock.read"), rs, new String[] {"milestone"});
        
        table.lockPatchStore();

        verifyPreparedStatementParameter(table.getSql("lock.obtain"), 1, "milestone");
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
    }
    
    /**
     * Validates that the patches table lock can be removed.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testUnlockPatchTable() throws Exception
    {       
        table.unlockPatchStore();

        verifyPreparedStatementParameter(table.getSql("lock.release"), 1, "milestone");
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionNotClosed();
        verifyNotCommitted();
    }
    
    /**
     * Validates that the <code>Connection</code> has not been closed by the 
     * class under test.
     * 
     * @throws SQLException if an unexpected error occured
     * @throws AssertionFailedError if the test failed
     */
    private void verifyConnectionNotClosed() throws SQLException, AssertionFailedError
    {
        if (conn.isClosed())
        {
            throw new AssertionFailedError("Connection was closed by PatchTest.");
        }
    }
}
