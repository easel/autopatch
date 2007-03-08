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
 */
package com.tacitknowledge.util.migration.jdbc;

/**
 * Overrides methods in the normal launcher factory that make it difficult to test
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class TestDistributedJdbcMigrationLauncherFactory 
    extends DistributedJdbcMigrationLauncherFactory
{
    /**
     * Returns TestDataSourceMigrationContext
     * 
     * @return TestDataSourceMigrationContext
     */
    public DataSourceMigrationContext getDataSourceMigrationContext()
    {
        return new TestDataSourceMigrationContext();
    }
    
    /**
     * Returns TestDistributedJdbcMigrationLauncher
     * 
     * @return TestDistributedJdbcMigrationLauncher
     */
    public JdbcMigrationLauncher getJdbcMigrationLauncher()
    {
        return new TestDistributedJdbcMigrationLauncher();
    }
    
    /**
     * Returns TestDistributedJdbcMigrationLauncher
     * 
     * @return TestDistributedJdbcMigrationLauncher
     */
    public DistributedJdbcMigrationLauncher getDistributedJdbcMigrationLauncher()
    {
        return new TestDistributedJdbcMigrationLauncher();
    }
}
