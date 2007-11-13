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
 * @version $Id: TestMigrationTask1.java,v 1.1 2007/11/13 13:43:27 woffca Exp $
 */
public class TestMigrationTask1 extends BaseTestMigrationTask
{
    /**
     * Creates a new <code>TestMigrationTask1</code>.
     */
    public TestMigrationTask1()
    {
        super("TestTask1", 4);
    }
}
