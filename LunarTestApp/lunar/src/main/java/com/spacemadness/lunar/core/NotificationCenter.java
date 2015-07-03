package com.spacemadness.lunar.core;

public abstract class NotificationCenter
{
    private static final Object[] EMPTY_DATA = new Object[0];

    public abstract void Register(String name, NotificationDelegate del);

    public abstract boolean Unregister(String name, NotificationDelegate del);

    public abstract boolean UnregisterAll(NotificationDelegate del);

    public void Post(Object sender, String name)
    {
        Post(sender, name, EMPTY_DATA);
    }

    public abstract void Post(Object sender, String name, Object... data);

    public void PostImmediately(Object sender, String name)
    {
        PostImmediately(sender, name, EMPTY_DATA);
    }

    public abstract void PostImmediately(Object sender, String name, Object... data);

    protected abstract void PostImmediately(NotificationObject notification);
}
