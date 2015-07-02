package com.spacemadness.lunar.core;

import com.spacemadness.lunar.utils.NotImplementedException;

/**
 * Created by weee on 5/28/15.
 */
public abstract class BaseObject implements IDestroyable
{
    protected void RegisterNotification(String name, NotificationDelegate del)
    {
        NotificationCenter.getMainNotificationCenter().Register(name, del);
    }

    protected void UnregisterNotification(String name, NotificationDelegate del)
    {
        NotificationCenter.getMainNotificationCenter().Unregister(name, del);
    }

    protected void UnregisterNotifications(NotificationDelegate del)
    {
        NotificationCenter.getMainNotificationCenter().UnregisterAll(del);
    }

    protected void PostNotification(String name, Object... data)
    {
        NotificationCenter.getMainNotificationCenter().Post(this, name, data);
    }

    protected void PostNotificationImmediately(String name, Object... data)
    {
        NotificationCenter.getMainNotificationCenter().PostImmediately(this, name, data);
    }

    //////////////////////////////////////////////////////////////////////////////
    // IDestroyable

    @Override
    public void Destroy()
    {
    }
}