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

import java.util.List;

/**
 * A source of <code>MigrationTask</code>s.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @version $Id: MigrationTaskSource.java,v 1.1 2004/03/15 07:42:21 scott Exp $
 */
public interface MigrationTaskSource
{
    /**
     * Returns a list of <code>MigrationTasks</code> that are in the given
     * package.
     * 
     * @param  packageName to package to search for migration tasks
     * @return a list of migration tasks; if not tasks were found, then an empty
     *         list must be returned.
     * @throws MigrationException if an unrecoverable error occurs
     */
    public List getMigrationTasks(String packageName) throws MigrationException;
}
