package com.spacemadness.lunar.core;

import com.spacemadness.lunar.utils.NotImplementedException;

/**
 * Created by weee on 5/28/15.
 */
public abstract class BaseObject implements IDestroyable
{
    protected void RegisterNotifications(NotificationInfo... list)
    {
        NotificationCenter.RegisterNotifications(list);
    }

    protected void RegisterNotification(String name, NotificationDelegate del)
    {
        NotificationCenter.RegisterNotification(name, del);
    }

    protected void UnregisterNotifications(NotificationInfo... list)
    {
        NotificationCenter.UnregisterNotifications(list);
    }

    protected void UnregisterNotification(String name, NotificationDelegate del)
    {
        NotificationCenter.UnregisterNotification(name, del);
    }

    protected void UnregisterNotifications(NotificationDelegate del)
    {
        NotificationCenter.UnregisterNotifications(del);
    }

    protected void UnregisterNotifications()
    {
        throw new NotImplementedException();
    }

    protected void PostNotification(String name, Object... data)
    {
        NotificationCenter.PostNotification(this, name, data);
    }

    protected void PostNotificationImmediately(String name, Object... data)
    {
        NotificationCenter.PostNotificationImmediately(this, name, data);
    }

    //////////////////////////////////////////////////////////////////////////////
    // IDestroyable

    public void Destroy()
    {
        UnregisterNotifications();
    }
}