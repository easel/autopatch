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

import junit.framework.TestCase;

/**
 * Test case that tests nothing; this allows the <code>inttest</code>s build
 * and test scripts to execute successfully when no tests have been defined.
 * This class should be removed when the first integration test has been added.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @version $Id: NoOpTestCase.java,v 1.1 2004/03/15 07:42:20 scott Exp $
 */
public class NoOpTestCase extends TestCase
{
    /**
     * Constructor for NoOpTestCase.
     * 
     * @param name the name of the test to run
     */
    public NoOpTestCase(String name)
    {
        super(name);
    }
}
