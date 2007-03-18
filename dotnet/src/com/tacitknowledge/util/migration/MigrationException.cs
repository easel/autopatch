/*
 * Copyright 2006 Tacit Knowledge LLC
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
#region Imports
using System;
#endregion

namespace com.tacitknowledge.util.migration
{
    /// <summary>
    /// An exception thrown by different migration processes.
    /// </summary>
    /// <author>Scott Askew (scott@tacitknowledge.com)</author>
    /// <author>Ian Mortimer (imorti@tacitknowledge.com)</author>
    /// <version>$Id: MigrationException.cs,v 1.4 2007/03/18 17:57:24 vgangantk Exp $</version>
    [Serializable]
    public class MigrationException : Exception
    {
        #region Constructors
        /// <seealso cref="Exception(String)"/>
        public MigrationException(String message)
            : base(message)
        {
        }

        /// <seealso cref="Exception(String, Exception)"/>
        public MigrationException(String message, Exception cause)
            : base(message, cause)
        {
        }
        #endregion
    }
}
