/* Copyright (c) 2004 Tacit Knowledge LLC  
 * See licensing terms below.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY  EXPRESSED OR IMPLIED 
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL TACIT KNOWLEDGE LLC OR ITS CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  THIS HEADER MUST
 * BE INCLUDED IN ANY DISTRIBUTIONS OF THIS CODE.
 */
package com.tacitknowledge.util.migration.jdbc.loader;

import java.io.IOException;
import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationContext;

/**
 * This is a utility class for reading excel files and 
 * performing a database insert based upon a cell value 
 * provided. 
 * 
 * @author Chris A. (chris@tacitknowledge.com)
 * @version $Id: ExcelFileLoader.java,v 1.1 2004/11/19 02:05:45 chrisa Exp $
 */
public abstract class ExcelFileLoader extends MigrationTaskSupport
{
    /**
     * Class logger
     */
    protected static Log log = LogFactory.getLog(ExcelFileLoader.class);
    
    /**
     * Obtains a database connection, reads a file assumed to be in Excel 
     * format based on the name provided <code>getName()</code>.  Calls the 
     * abstract method <code>processWorkbook()</code>
     */
    public void migrate(MigrationContext ctx) throws MigrationException
    {
        JdbcMigrationContext context = (JdbcMigrationContext) ctx;
        Connection conn = context.getConnection();
        FileLoadingUtility utility = new FileLoadingUtility(getName());
        try
        {
            POIFSFileSystem fs = new POIFSFileSystem(utility.getResourceAsStream());
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            processWorkbook(wb, conn);
        } 
        catch (IOException e)
        {
            log.error("An IO Exception occurred while trying to parse the Excel file.", e);
            throw new MigrationException("Error reading file.", e);
        }
    }
    
    /**
     * Process workbook by overwriting this method
     * 
     * @param wb the excel workbook to process
     * @param conn the database connection to use for data loading
     * @throws MigrationException if something goes wrong
     */
    public abstract void  processWorkbook(HSSFWorkbook wb, Connection conn) 
    	throws MigrationException;
}
