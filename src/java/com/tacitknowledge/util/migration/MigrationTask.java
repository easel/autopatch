/* Copyright 2006 Tacit Knowledge LLC
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

/**
 * A single, idempotent migration task.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public interface MigrationTask extends Comparable
{
    /**
     * Performs a migration
     * 
     * @param  context the <code>MigrationContext</code> for this run
     * @throws MigrationException if an unexpected error occurred
     */
    public void migrate(MigrationContext context) throws MigrationException;
    
    /**
     * Returns the name of this migration task. 
     * 
     * @return the name of this migration task
     */
    public String getName();
    
    /**
     * Returns the relative order in which this migration should occur.
     * 
     * @return the relative order in which this migration should occur; may never
     *         return <code>null</code>
     */
    public Integer getLevel();
}
