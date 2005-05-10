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

package com.tacitknowledge.util.migration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tacitknowledge.util.discovery.ClassDiscoveryUtil;

/**
 * Returns a list of all public, concrete classes that implement the
 * <code>MigrationTask</code> in a specific package.
 *
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @version $Id: ClassMigrationTaskSource.java,v 1.4 2005/05/10 01:39:32 mike Exp $
 */
public class ClassMigrationTaskSource implements MigrationTaskSource
{
    /** Class logger */
    private static Log log = LogFactory.getLog(ClassMigrationTaskSource.class);
    
    /**
     * @see MigrationTaskSource#getMigrationTasks(String)
     */
    public List getMigrationTasks(String packageName) throws MigrationException
    {
        if (packageName == null)
        {
            throw new MigrationException("You must specify a package to get tasks for");
        }
        
        Class[] taskClasses = ClassDiscoveryUtil.getClasses(packageName, MigrationTask.class);
        log.debug("Found " + taskClasses.length + " patches in " + packageName);
        return instantiateTasks(taskClasses);
    }

    /**
     * Instantiates the given classes
     * 
     * @param  taskClasses the classes instantiate
     * @return a list of <code>MigrationTasks</code>
     * @throws MigrationException if a class could not be instantiated; this
     *         is most likely due to the abscense of a default constructor
     */
    private List instantiateTasks(Class[] taskClasses) throws MigrationException
    {
        List tasks = new ArrayList();
        for (int i = 0; i < taskClasses.length; i++)
        {
            Class taskClass = taskClasses[i];
            try
            {
                Object o = taskClass.newInstance();
                tasks.add(o);
            }
            catch (Exception e)
            {
                throw new MigrationException("Could not instantiate MigrationTask "
                                             + taskClass.getName(), e);
            }
        }
        return tasks;
    }
}
