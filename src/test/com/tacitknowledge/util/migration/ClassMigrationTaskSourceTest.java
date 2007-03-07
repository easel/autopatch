/* 
 * Copyright 2007 Tacit Knowledge LLC
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
 *
 * $Id: ClassMigrationTaskSourceTest.java,v 1.2 2007/03/07 00:25:56 mikehardy Exp $
 */
package com.tacitknowledge.util.migration;

import java.util.List;
import junit.framework.TestCase;

import com.tacitknowledge.util.migration.MigrationException;

/**
 * Exercise the ClassMigrationTaskSource object
 * 
 * @author Mike Hardy <mailto:mike@tacitknowledge.com/>
 */
public class ClassMigrationTaskSourceTest extends TestCase
{
    /**
     * Make sure class instantiation fails on null package name
     */
    public void testInstantiateTasksNullPackage()
    {
        ClassMigrationTaskSource source = new ClassMigrationTaskSource();
        try
        {
            source.getMigrationTasks(null);
            fail("We should have gotten an exception for the null package");
        }
        catch (MigrationException me)
        {
            // we expect this
        }
    }
    
    /**
     * Make sure class instantiation fails on package with no types
     */
    public void testInstantiateTasksNoTasks()
    {
        ClassMigrationTaskSource source = new ClassMigrationTaskSource();
        try
        {
            List tasks = source.getMigrationTasks("com.tacitknowledge.foo.bar");
            assertEquals(0, tasks.size());
        }
        catch (MigrationException me)
        {
            fail("We should not have gotten an exception");
        }
    }
    
    /**
     * Make sure class instantiation fails on package with bad tasks
     */
    public void testInstantiateTasksInstantiationException()
    {
        ClassMigrationTaskSource source = new ClassMigrationTaskSource();
        try
        {
            source.getMigrationTasks(getClass().getPackage().getName() + ".tasks.instantiation");
            fail("We should have gotten an exception");
        }
        catch (MigrationException me)
        {
            assertTrue(me.getCause() instanceof RuntimeException);
        }
    }
}
