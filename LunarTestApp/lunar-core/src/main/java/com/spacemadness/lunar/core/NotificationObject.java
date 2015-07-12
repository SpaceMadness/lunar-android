package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;

public class NotificationObject extends ObjectsPoolEntry implements Notification, Runnable
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
    public Object Get(String key)
    {
        return data != null ? data.get(key) : null;
    }

    @Override
    public String getString(String key)
    {
        return (String) Get(key);
    }

    @Override
    public boolean getBool(String key, boolean defaultValue)
    {
        Object value = Get(key);
        return value != null ? (boolean) value : defaultValue;
    }

    @Override
    public int getInt(String key, int defaultValue)
    {
        Object value = Get(key);
        return value != null ? (int) value : defaultValue;
    }

    @Override
    public float getFloat(String key, float defaultValue)
    {
        Object value = Get(key);
        return value != null ? (float) value : defaultValue;
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