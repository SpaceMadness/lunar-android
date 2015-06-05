package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weee on 5/28/15.
 */
class Notification extends ObjectsPoolEntry
{
    private Map<String, Object> m_dictionary; // FIXME: rename
    
    void Init(Object sender, String name, Object... pairs)
    {
        Sender = sender;
        Name = name;

        Assert.IsTrue(pairs.length % 2 == 0);
        for (int i = 0; i < pairs.length;)
        {
            String key = ClassUtils.cast(pairs[i++], String.class);
            Object value = pairs [i++];

            this.Set(key, value);
        }
    }

    public <T> T Get(String key, Class<? extends T> cls)
    {
        Object value = Get(key);
        return ClassUtils.as(value, cls);
    }

    public Object Get(String key)
    {
        if (m_dictionary != null)
        {
            return m_dictionary.get(key);
        }

        return null;
    }

    void Set(String key, Object value)
    {
        if (m_dictionary == null)
        {
            m_dictionary = new HashMap<String, Object>(1);
        }
        m_dictionary.put(key, value);
    }

    @Override
    protected void OnRecycleObject()
    {
        Sender = null;
        Name = null;

        if (m_dictionary != null)
        {
            m_dictionary.clear();
        }
    }
    
    public String Name; // FIXME: { get; private set; }
    public Object Sender; // FIXME: { get; private set; }
}