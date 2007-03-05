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
#region Imports
using System;
#endregion
namespace com.tacitknowledge.util.migration
{
	
	/// <summary> 
	/// 
	/// </summary>
	/// <author>   Scott Askew (scott@tacitknowledge.com)
	/// </author>
	/// <version>  $Id: MigrationException.cs,v 1.1 2007/03/05 18:08:08 imorti Exp $
	/// </version>
	[Serializable]
	public class MigrationException:System.Exception
	{
		/// <seealso cref="Exception.Exception(String)">
		/// </seealso>
		public MigrationException(System.String message):base(message)
		{
		}
		
		/// <seealso cref="Exception.Exception(String, Throwable)">
		/// </seealso>
		//UPGRADE_NOTE: Exception 'java.lang.Throwable' was converted to 'System.Exception' which has different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1100'"
		public MigrationException(System.String message, System.Exception cause):base(message, cause)
		{
		}
	}
}