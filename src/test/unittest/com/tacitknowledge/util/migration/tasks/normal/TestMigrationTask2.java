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

package com.tacitknowledge.util.migration.tasks.normal;

import com.tacitknowledge.util.migration.tasks.BaseTestMigrationTask;

/**
 * Basic test migration task.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @version $Id: TestMigrationTask2.java,v 1.1 2007/11/13 13:43:27 woffca Exp $
 */
public class TestMigrationTask2 extends BaseTestMigrationTask
{
    /**
     * The patch level to use instead of '2'
     */
    private static Integer patchLevelOverride = new Integer(5);
    
    /**
     * Creates a new <code>TestMigrationTask3</code>.
     */
    public TestMigrationTask2()
    {
        super("TestTask2", 5);
    }
    
    /**
     * @see com.tacitknowledge.util.migration.MigrationTaskSupport#getLevel()
     */
    public Integer getLevel()
    {
        return patchLevelOverride;
    }

    /**
     * Sets the patch level to use for all instances of this class.
     * 
     * @param level the patch level to use for all instances of this class; if
     *        <code>null</code>, then the default patch level (2) will be used
     */
    public static void setPatchLevelOverride(Integer level)
    {
        patchLevelOverride = level;
    }

    /**
     * Resets the task the its default state. 
     */    
    public static void reset()
    {
        patchLevelOverride = new Integer(5);
    }

}
