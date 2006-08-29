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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the <code>MessageListener</code> that are associated with a
 * <code>Migration</code> instance. 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
class MigrationBroadcaster
{
    /**
     * Used by <code>notifyListeners</code> to indicate that listeners should
     * be informed that a task is about to start. 
     */
    public static final int TASK_START = 1;

    /**
     * Used by <code>notifyListeners</code> to indicate that listeners should
     * be informed that a task has successfully completed. 
     */
    public static final int TASK_SUCCESS = 2;

    /**
     * Used by <code>notifyListeners</code> to indicate that listeners should
     * be informed that a task failed. 
     */
    public static final int TASK_FAILED = 3;
    
    /**
     * The listeners interested in being notified of migration task events.
     */
    private List listeners = new ArrayList();

    /**
     * Notifies all registered listeners of a migration task event.
     * 
     * @param  task the task that is being or that has been executed
     * @param  context the context in which the task was executed
     * @param  eventType TASK_START, TASK_SUCCESS, or TASK_FAIL
     * @param  e the exception thrown by the task if the task failed
     * @throws MigrationException if one of the listeners threw an exception 
     */
    public void notifyListeners(MigrationTask task, MigrationContext context,
        MigrationException e, int eventType) throws MigrationException
    {
        for (Iterator i = listeners.iterator(); i.hasNext();)
        {
            MigrationListener listener = (MigrationListener) i.next();
            switch (eventType)
            {
                case TASK_START :
                    listener.migrationStarted(task, context);
                    break;

                case TASK_SUCCESS :
                    listener.migrationSuccessful(task, context);
                    break;

                case TASK_FAILED :
                    listener.migrationFailed(task, context, e);
                    break;

                default :
                    throw new IllegalArgumentException("Unknown event type");
            }
        }
    }

    /**
     * Notifies all registered listeners of a migration task event.
     * 
     * @param  task the task that is being or that has been executed
     * @param  context the context in which the task was executed
     * @param  eventType TASK_START, TASK_SUCCESS, or TASK_FAIL
     * @throws MigrationException if one of the listeners threw an exception 
     */
    public void notifyListeners(MigrationTask task, MigrationContext context,
        int eventType) throws MigrationException
    {
        notifyListeners(task, context, null, eventType);
    }
    
    /**
     * Registers the given <code>MigrationListener</code> as being interested
     * in migration task events.
     * 
     * @param listener the listener to add; may not be <code>null</code>
     */
    public void addListener(MigrationListener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException("listener cannot be null");
        }
        listeners.add(listener);
    }
    
    /**
     * Removes the given <code>MigrationListener</code> from the list of listeners
     * associated with the <code>Migration</code> instance.
     * 
     * @param  listener the listener to add; may not be <code>null</code>
     * @return <code>true</code> if the listener was located and removed,
     *         otherwise <code>false</code>.
     */
    public boolean removeListener(MigrationListener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException("listener cannot be null");
        }
        return listeners.remove(listener);
    }
    
    /**
     * Get the list of listeners
     * 
     * @return List of MigrationListener objects
     */
    public List getListeners()
    {
        return listeners;
    }
}
