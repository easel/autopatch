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

/**
 * 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 * @version $Id: MigrationException.java,v 1.1 2004/03/15 07:42:21 scott Exp $
 */
public class MigrationException extends Exception
{
    /**
     * @see Exception#Exception() 
     */
    public MigrationException()
    {
        super();
    }

    /**
     * @see Exception#Exception(String) 
     */
    public MigrationException(String message)
    {
        super(message);
    }

    /**
     * @see Exception#Exception(Throwable) 
     */
    public MigrationException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @see Exception#Exception(String, Throwable) 
     */
    public MigrationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
