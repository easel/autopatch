/* Copyright 2007 Tacit Knowledge LLC
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
 * Provides system resources to migration tasks. 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public interface MigrationContext
{
    /**
     * The name of the migration configuration file
     */
    public static final String MIGRATION_CONFIG_FILE = "migration.properties";

    /**
     * Commits the current migration transaction.
     * 
     * @throws MigrationException if there was an unrecoverable error committing
     *         the transaction
     */
    public void commit() throws MigrationException;
    
    /**
     * Rolls back the current migration transaction. 
     * 
     * @throws MigrationException if there was an unrecoverable error committing
     *         the transaction
     */
    public void rollback() throws MigrationException;
}