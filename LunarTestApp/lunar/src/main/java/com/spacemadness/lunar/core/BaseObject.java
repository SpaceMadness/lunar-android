package com.spacemadness.lunar.core;

import com.spacemadness.lunar.utils.NotImplementedException;

/**
 * Created by weee on 5/28/15.
 */
public abstract class BaseObject implements IDestroyable
{
    protected void RegisterNotification(String name, NotificationDelegate del)
    {
        // NotificationCenter.RegisterNotification(name, del);
        throw new NotImplementedException();
    }

    protected void UnregisterNotification(String name, NotificationDelegate del)
    {
        // NotificationCenter.UnregisterNotification(name, del);
        throw new NotImplementedException();
    }

    protected void UnregisterNotifications(NotificationDelegate del)
    {
        // NotificationCenter.UnregisterNotifications(del);
        throw new NotImplementedException();
    }

    protected void UnregisterNotifications()
    {
        throw new NotImplementedException();
    }

    protected void PostNotification(String name, Object... data)
    {
        // NotificationCenter.PostNotification(this, name, data);
        throw new NotImplementedException();
    }

    protected void PostNotificationImmediately(String name, Object... data)
    {
        // NotificationCenter.PostNotificationImmediately(this, name, data);
        throw new NotImplementedException();
    }

    //////////////////////////////////////////////////////////////////////////////
    // IDestroyable

    public void Destroy()
    {
        UnregisterNotifications();
    }
}