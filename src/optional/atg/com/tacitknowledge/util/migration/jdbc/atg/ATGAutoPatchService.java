/* 
 * Copyright 2006 Tacit Knowledge LLC
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
package com.tacitknowledge.util.migration.jdbc.atg;

import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.AutoPatchService;
import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;
import com.tacitknowledge.util.migration.jdbc.DatabaseType;

import atg.nucleus.Configuration;
import atg.nucleus.Nucleus;
import atg.nucleus.Service;
import atg.nucleus.ServiceEvent;
import atg.nucleus.ServiceException;

import javax.sql.DataSource;

/**
 * Automatically applies database DDL and SQL patches to all schemas on server startup.
 * See http://autopatch.sf.net
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class ATGAutoPatchService extends AutoPatchService implements Service
{
    /** Our Nucleus */
    private Nucleus nucleus = null;

    /** The Configuration we have */
    private Configuration configuration = null;

    /** Whether we are running or not */
    private boolean running = false;

    /** patch on startService - set to false if orchestrated by ATGDistributedAutoPatchService */
    private boolean patchOnStartup = true;

    /** Take the server down on errors? */
    private boolean failServerOnError = true;

    /** The patch to the post-patch tasks */
    private String postPatchPath = null;

    /** The DataSources controlled by this system */
    private DataSource[] dataSources;

    /**
     * Handle patching the database on startup
     *
     * @see atg.nucleus.ServiceListener#startService(atg.nucleus.ServiceEvent)
     */
    public void startService(ServiceEvent se) throws ServiceException
    {
        if ((se.getService() == this) && !isRunning())
        {
            setRunning(true);
            setNucleus(se.getNucleus());
            setServiceConfiguration(se.getServiceConfiguration());

            /* if we have multiple datasources defined, create and add contexts for them */
            configureDataSources();

            doStartService();
        }
    }

    /**
     * @see com.tacitknowledge.util.migration.jdbc.AutoPatchService#patch()
     * @throws atg.nucleus.ServiceException on error
     */
    public void doStartService() throws ServiceException
    {
        if (isPatchOnStartup())
        {
            try
            {
                patch();
            }
            catch (MigrationException me)
            {
                if (isFailServerOnError())
                {
                    System.err.println("There was a problem patching the database");
                    me.printStackTrace(System.err);
                    System.err.println("Shutting down server to prevent inconsistency");
                    System.err.println("Change the 'failServerOnError' property if "
                                       + "you want it to start anyway");
                    System.exit(1);
                }

                throw new ServiceException("There was a problem patching the database", me);
            }
        }
    }

    /**
     * Resets the "running" state to false
     *
     * @see atg.nucleus.Service#stopService()
     */
    public void stopService() throws ServiceException
    {
        setRunning(false);
    }

    /** configures multiple datasources for this system */
    private void configureDataSources()
    {
        if (getDataSources() != null)
        {
            int numSources = getDataSources().length;

            for (int i = 0; i < numSources; i++)
            {
                DataSource source = getDataSources()[i];
                DataSourceMigrationContext context = getDataSourceMigrationContext();

                context.setSystemName(getSystemName());
                context.setDatabaseType(new DatabaseType(getDatabaseType()));
                context.setDataSource(source);

                addContext(context);
            }
        }
    }

    /**
     * Get the service configuration used to start us
     * @return Configuration
     */
    public Configuration getServiceConfiguration()
    {
        return configuration;
    }

    /**
     * Set the service configuration the Nucleus is using for us
     * @param config the config for the service
     */
    public void setServiceConfiguration(Configuration config)
    {
        this.configuration = config;
    }

    /**
     * Get the Nucleus that started us
     * @return Nucleus the Nucleus resolver to use
     */
    public Nucleus getNucleus()
    {
        return nucleus;
    }

    /**
     * Set the Nucleus that started us
     * @param nucleus the Nucleus resolver to use
     */
    public void setNucleus(Nucleus nucleus)
    {
        this.nucleus = nucleus;
    }

    /**
     * Return boolean true if we are started
     * @return true if we are started and running
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * Set whether we are running
     * @param running whether the server is running or not
     */
    public void setRunning(boolean running)
    {
        this.running = running;
    }

    /**
     * Whether we should patch in startService or not. This is useful because
     * under ATGDistributedAutoPatchService control these services should not
     * start on their own
     *
     * @return boolean true if the service should patch in startService
     */
    public boolean isPatchOnStartup()
    {
        return patchOnStartup;
    }

    /**
     * Whether we should patch in startService or not. This is useful because
     * under ATGDistributedAutoPatchService control these services should not
     * start on their own
     *
     * @param patchOnStartup true if the service should patch in startService
     */
    public void setPatchOnStartup(boolean patchOnStartup)
    {
        this.patchOnStartup = patchOnStartup;
    }

    /**
     * Determine whether we should fail the server on an error condition. This involves
     * throwing an Error instead of an Exception so ATG will halt module loading. It is
     * recommended to leave this set to true as allowing startup on patch failure may allow
     * your software to run despite an inconsistent state in the database
     *
     * @return boolean true if the server should stop module load on patch failure
     */
    public boolean isFailServerOnError()
    {
        return failServerOnError;
    }

    /**
     * @see #isFailServerOnError()
     * @param failServerOnError boolean true to fail server on patch failure
     */
    public void setFailServerOnError(boolean failServerOnError)
    {
        this.failServerOnError = failServerOnError;
    }

    /**
     * @return Returns the postPatchPath.
     */
    public String getPostPatchPath()
    {
        return postPatchPath;
    }

    /**
     * @param postPatchPath The postPatchPath to set.
     */
    public void setPostPatchPath(String postPatchPath)
    {
        this.postPatchPath = postPatchPath;
    }

    /** @return the DataSources managed by this system */
    public DataSource[] getDataSources()
    {
        return dataSources;
    }

    /** @param dataSources the DataSources managed by this system */
    public void setDataSources(DataSource[] dataSources)
    {
        this.dataSources = dataSources;
    }
}
