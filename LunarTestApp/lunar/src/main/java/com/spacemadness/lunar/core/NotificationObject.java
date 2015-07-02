package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;

class NotificationObject extends ObjectsPoolEntry implements Notification, Runnable
{
    private Map<String, Object> data;

    private String name;
    private Object sender;

    NotificationCenter notificationCenter;

    public NotificationObject()
    {
    }

    void Init(Object sender, String name, Object[] pairs)
    {
        this.sender = sender;
        this.name = name;

        Assert.IsTrue(pairs.length % 2 == 0);
        for (int i = 0; i < pairs.length; )
        {
            String key = ClassUtils.cast(pairs[i++], String.class);
            Object value = pairs[i++];

            Set(key, value);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Runnable

    @Override
    public void run()
    {
        notificationCenter.PostImmediately(this);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Data

    @Override
    public <T> T Get(String key, Class<? extends T> cls)
    {
        Object value = Get(key);
        return ClassUtils.as(value, cls);
    }

    @Override
    public Object Get(String key)
    {
        if (data != null)
        {
            return data.get(key);
        }

        return null;
    }

    void Set(String key, Object value)
    {
        if (data == null)
        {
            data = new HashMap<>(1);
        }
        data.put(key, value);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Objects pool

    @Override
    protected void OnRecycleObject()
    {
        sender = null;
        name = null;
        notificationCenter = null;

        if (data != null)
        {
            data.clear();
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Object getSender()
    {
        return sender;
    }

    Map<String, Object> getData()
    {
        return data;
    }
}