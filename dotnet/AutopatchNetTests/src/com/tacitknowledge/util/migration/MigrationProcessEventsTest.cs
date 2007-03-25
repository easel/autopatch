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
#region Imports
using System;
using System.Collections.Generic;
using NUnit.Framework;
using com.tacitknowledge.testhelpers;
#endregion

namespace com.tacitknowledge.util.migration
{
    /// <summary>
    /// A unit test for verifying migration events functionality of <code>MigrationProcess</code>.
    /// </summary>
    /// <author>Vladislav Gangan (vgangan@tacitknowledge.com)</author>
    /// <version>$Id: MigrationProcessEventsTest.cs,v 1.4 2007/03/25 14:56:45 vgangantk Exp $</version>
    [TestFixture]
    public class MigrationProcessEventsTest
    {
        private bool started = false;
        private bool succeeded = false;
        private bool failed = false;

        [SetUp]
        public void SetUp()
        {
            started = false;
            succeeded = false;
            failed = false;
        }

        /// <summary>
        /// Make sure this class does not receive any event notifications if it did not subscribe to them.
        /// </summary>
        [Test]
        public void TestNoListeners()
        {
            TestMigrationContext context = new TestMigrationContext();
            MigrationProcess process = new MigrationProcess();
            MigrationTask1 task = new MigrationTask1();

            process.ApplyPatch(context, task, true);

            Assert.IsFalse(started, "'started' should be false");
            Assert.IsFalse(succeeded, "'succeeded' should be false");
            Assert.IsFalse(failed, "'failed' should be false");
        }

        /// <summary>
        /// Make sure this class does not receive any event notifications if is subscribed to them but
        /// the owner does not want to broadcast them.
        /// </summary>
        [Test]
        public void TestNoBroadcast()
        {
            TestMigrationContext context = new TestMigrationContext();
            MigrationProcess process = new MigrationProcess();
            MigrationTask1 task = new MigrationTask1();

            process.MigrationStarted +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationStarted);
            process.MigrationSuccessful +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationSuccessful);
            process.MigrationFailed +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationFailed);
            process.ApplyPatch(context, task, false);

            Assert.IsFalse(started, "'started' should be false");
            Assert.IsFalse(succeeded, "'succeeded' should be false");
            Assert.IsFalse(failed, "'failed' should be false");
        }

        /// <summary>
        /// Make sure this class receives 'MigrationStarted' and 'MigrationSuccessful' notifications.
        /// </summary>
        [Test]
        public void TestSuccessfulMigrationBroadcast()
        {
            TestMigrationContext context = new TestMigrationContext();
            MigrationProcess process = new MigrationProcess();
            MigrationTask1 task = new MigrationTask1();

            process.MigrationStarted +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationStarted);
            process.MigrationSuccessful +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationSuccessful);
            process.MigrationFailed +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationFailed);
            process.ApplyPatch(context, task, true);

            Assert.IsTrue(started, "'started' should be true");
            Assert.IsTrue(succeeded, "'succeeded' should be true");
            Assert.IsFalse(failed, "'failed' should be false");
        }

        /// <summary>
        /// Make sure this class receives 'MigrationStarted' and 'MigrationFailed' notifications.
        /// </summary>
        [Test]
        [ExpectedException(typeof(MigrationException))]
        public void TestFailedMigrationBroadcast()
        {
            TestMigrationContext context = new TestMigrationContext();
            MigrationProcess process = new MigrationProcess();
            MigrationTask2 task = new MigrationTask2();

            task.ForceFail = true;
            process.MigrationStarted +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationStarted);
            process.MigrationSuccessful +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationSuccessful);
            process.MigrationFailed +=
                new MigrationProcess.MigrationStatusEventHandler(process_MigrationFailed);

            try
            {
                process.ApplyPatch(context, task, true);
            }
            catch (MigrationException me)
            {
                Assert.IsTrue(started, "'started' should be true");
                Assert.IsFalse(succeeded, "'succeeded' should be false");
                Assert.IsTrue(failed, "'failed' should be true");
                throw me;
            }
        }

        void process_MigrationFailed(IMigrationTask task, IMigrationContext context, MigrationException e)
        {
            failed = true;
        }

        void process_MigrationSuccessful(IMigrationTask task, IMigrationContext context, MigrationException e)
        {
            succeeded = true;
        }

        void process_MigrationStarted(IMigrationTask task, IMigrationContext context, MigrationException e)
        {
            started = true;
        }
    }
}
