/* Copyright 2004 Tacit Knowledge LLC
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;

/**
 * Base class used for creating bulk data loading <code>MigrationTask</code>s.
 *  
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @version $Id: SqlLoadMigrationTask.java,v 1.5 2004/11/03 19:43:44 chrisa Exp $
 */
public abstract class SqlLoadMigrationTask extends MigrationTaskSupport
{
    /**
     * Class logger
     */
    protected static Log log = LogFactory.getLog(SqlLoadMigrationTask.class);
    
    /**
     * Creates a new <code>SqlScriptMigrationTask</code>.
     */
    public SqlLoadMigrationTask()
    {
        // Nothing to do
    }
    
    /**
     * @see MigrationTaskSupport#migrate(MigrationContext)
     */
    public void migrate(MigrationContext ctx) throws MigrationException
    {
        JdbcMigrationContext context = (JdbcMigrationContext) ctx;
        Connection conn = context.getConnection();
        
        try
        {
            PreparedStatement stmt = conn.prepareStatement(getStatmentSql());
            List rows = getData(getResourceAsStream());
            int rowCount = rows.size();
            for (int i = 0; i < rowCount; i++)
            {
                String data = (String) rows.get(i);
                boolean loadRowFlag = insert(data, stmt);
                if (loadRowFlag)
                {
	                stmt.addBatch();
	                if (i % 50 == 0)
	                {
	                    stmt.executeBatch();
	                }
                }
            }
        }
        catch (Exception e)
        {
            log.error(getName() + ": Error running SQL \"" + getStatmentSql() + "\"", e);
            if (e instanceof SQLException)
            {
                if (((SQLException) e).getNextException() != null)
                {
                    log.error("Chained SQL Exception", ((SQLException) e).getNextException());
                }
            }
            
            throw new MigrationException(e);
        }
    }
    
    /**
     * Returns an input stream representing the data to load.
     *  
     * @return an input stream representing the data to load
     */
    protected abstract InputStream getResourceAsStream();

    /**
     * Inserts the given row of data into the database using the given
     * prepared statement.  Subclasses should parse the row and call the
     * appropriate set methods on the prepared statement.
     * 
     * @param  data the current row of data to load
     * @param  stmt the statement used for inserting data into the DB
     * 
     * @return false if you do not want this row loaded, true otherwise
     * @throws Exception if an unexpected error occurs
     */    
    protected abstract boolean insert(String data, PreparedStatement stmt) throws Exception;
    
    /**
     * Returns the <code>PreparedStatement</code> SQL used for inserting rows
     * into the table.
     * 
     * @return the <code>PreparedStatement</code> SQL used for inserting rows
     *         into the table
     */
    protected abstract String getStatmentSql();

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }

    /**
     * Returns the data to load as a list of rows.
     * 
     * @param  is the input stream containing the data to load
     * @return the data to load as a list of rows
     * @throws IOException if the input stream could not be read
     */
    private List getData(InputStream is) throws IOException
    {
        List data = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            data.add(line);
        }
        return data;
    }
}
