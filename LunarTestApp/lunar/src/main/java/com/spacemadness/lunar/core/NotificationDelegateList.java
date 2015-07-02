package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.BaseList;
import com.spacemadness.lunar.utils.NotImplementedException;

/**
 * Created by weee on 5/28/15.
 */
class NotificationDelegateList extends BaseList<NotificationDelegate> // TODO: make concurrent
{
    // FIXME: use weak references

    private static final NotificationDelegate NullNotificationDelegate = new NotificationDelegate()
    {
        @Override
        public void onNotification(Notification notification)
        {
        }
    };

    public NotificationDelegateList()
    {
        super(NullNotificationDelegate);
    }

    @Override
    public boolean Add(NotificationDelegate del)
    {
        Assert.IsFalse(Contains(del));
        return super.Add(del);
    }
    
    public synchronized void NotifyDelegates(Notification notification)
    {
        try
        {
            lock();

            int delegatesCount = list.size();
            for (int i = 0; i < delegatesCount; ++i)
            {
                try
                {
                    NotificationDelegate del = list.get(i);
                    del.onNotification(notification);
                }
                catch (Exception e)
                {
                    Log.logException(e, "Error while notifying delegate");
                }
            }
        }
        finally
        {
            unlock();
        }
    }
}