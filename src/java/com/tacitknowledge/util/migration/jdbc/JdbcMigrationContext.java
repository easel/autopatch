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

package com.tacitknowledge.util.migration.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;

/**
 * Provides JDBC resources to migration tasks. 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @version $Id: JdbcMigrationContext.java,v 1.6 2005/02/15 21:53:10 scott Exp $
 */
public class JdbcMigrationContext implements MigrationContext
{
    /** 
     * The field name used for the database dialect
     */
    public static final String DIALECT_PROPERTY_SUFFIX = ".jdbc.dialect";
    
    /** 
     * The path to the patches
     */ 
    public static final String PATCH_PATH_SUFFIX = ".patch.path";
    
    /**
     * The database connection to use
     */
    private Connection connection = null;
    
    /**
     * System migration config
     */
    private Properties properties = new Properties();
    
    /**
     * Loads the configuration from the migration config properties file.
     * 
     * @throws MigrationException if an unexpected error occurs
     */
    public void loadFromMigrationProperties() throws MigrationException
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(MIGRATION_CONFIG_FILE);
        if (is != null)
        {
            try
            {
                properties.load(is);
            }
            catch (IOException e)
            {
                throw new MigrationException("Error reading in migration properties file", e);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException ioe)
                {
                    throw new MigrationException("Error closing migration properties file", ioe);
                }
            }
        }
        else
        {
            throw new MigrationException("Unable to find migration properties file '"
                    + MIGRATION_CONFIG_FILE + "'");
        }
    }
    
    /**
     * Returns the database connection to use
     * 
     * @return the database connection to use
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Returns the database connection to use
     * 
     * @param connection the database connection to use
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }
    
    /**
     * @see MigrationContext#commit()
     */
    public void commit() throws MigrationException
    {
        try
        {
            this.connection.commit();
        }
        catch (SQLException e)
        {
            throw new MigrationException("Error committing SQL transaction", e);
        }
    }

    /**
     * @see MigrationContext#rollback()
     */
    public void rollback() throws MigrationException
    {
        try
        {
            getConnection().rollback();
        }
        catch (SQLException e)
        {
            throw new MigrationException("Could not rollback SQL transaction", e);
        }
    }

    /**
     * @see MigrationContext#getConfiguration()
     */
    public Properties getConfiguration()
    {
        return properties;
    }
    
    /**
     * @return Returns the dialect.
     */
    public String getDialect()
    {
        return getConfiguration().getProperty(DIALECT_PROPERTY_SUFFIX);
    }
    
    /**
     * @param dialect The dialect to set.
     */
    public void setDialect(String dialect)
    {
        getConfiguration().setProperty(DIALECT_PROPERTY_SUFFIX, dialect);
    }

    /**
     * @return Returns the patchPath.
     */
    public String getPatchPath()
    {
        return getConfiguration().getProperty(DIALECT_PROPERTY_SUFFIX);
    }
    
    /**
     * @param patchPath The patchPath to set.
     */
    public void setPatchPath(String patchPath)
    {
        getConfiguration().setProperty(DIALECT_PROPERTY_SUFFIX, patchPath);
    }
}
