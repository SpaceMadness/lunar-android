package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
public abstract class BaseObject implements IDestroyable
{
    protected void RegisterNotifications(NotificationInfo... list)
    {
        NotificationCenter.RegisterNotifications(list);
    }

    protected void RegisterNotification(string name, NotificationDelegate del)
    {
        NotificationCenter.RegisterNotification(name, del);
    }

    protected void UnregisterNotifications(NotificationInfo... list)
    {
        NotificationCenter.UnregisterNotifications(list);
    }

    protected void UnregisterNotification(string name, NotificationDelegate del)
    {
        NotificationCenter.UnregisterNotification(name, del);
    }

    protected void UnregisterNotifications(NotificationDelegate del)
    {
        NotificationCenter.UnregisterNotifications(del);
    }

    protected void UnregisterNotifications()
    {
        NotificationCenter.UnregisterNotifications(this);
    }

    protected void PostNotification(string name, object... data)
    {
        NotificationCenter.PostNotification(this, name, data);
    }

    protected void PostNotificationImmediately(string name, object... data)
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