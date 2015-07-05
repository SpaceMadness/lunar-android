package com.spacemadness.lunar.core;

import static com.spacemadness.lunar.AppTerminal.*;

public abstract class BaseObject implements IDestroyable
{
    protected void RegisterNotification(String name, NotificationDelegate del)
    {
        getNotificationCenter().Register(name, del);
    }

    protected void UnregisterNotification(String name, NotificationDelegate del)
    {
        getNotificationCenter().Unregister(name, del);
    }

    protected void UnregisterNotifications(NotificationDelegate del)
    {
        getNotificationCenter().UnregisterAll(del);
    }

    protected void PostNotification(String name, Object... data)
    {
        getNotificationCenter().Post(this, name, data);
    }

    protected void PostNotificationImmediately(String name, Object... data)
    {
        getNotificationCenter().PostImmediately(this, name, data);
    }

    //////////////////////////////////////////////////////////////////////////////
    // IDestroyable

    @Override
    public void Destroy()
    {
    }
}