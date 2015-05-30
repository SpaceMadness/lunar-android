package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.BaseList;

/**
 * Created by weee on 5/28/15.
 */
class NotificationDelegateList extends BaseList<NotificationDelegate>
{
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
    
    public boolean RemoveAll(Object target)
    {
        boolean removed = false;
        for (int i = 0; i < list.size(); ++i)
        {
            NotificationDelegate del = list.get(i);
            if (del.Target == target)
            {
                RemoveAt(i); // it's safe: the list size will be changed on the next update
                removed = true;
            }
        }
        
        return removed;
    }
    
    public void NotifyDelegates(Notification notification)
    {
        int delegatesCount = list.size();
        for (int i = 0; i < delegatesCount; ++i)
        {
            NotificationDelegate del = list.get(i);
            try
            {
                del.onNotification(notification);
            }
            catch (Exception e)
            {
                Log.error(e, "Error while notifying delegate");
            }
        }
        ClearRemoved();
    }
}