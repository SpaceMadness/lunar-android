package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
class Notification : ObjectsPoolEntry
{
    private IDictionary<String, Object> m_dictionary;
    
    internal void Init(Object sender, String name, params Object[] pairs)
    {
        Sender = sender;
        Name = name;

        Assert.IsTrue(pairs.Length % 2 == 0);
        for (int i = 0; i < pairs.Length;)
        {
            String key = ClassUtils.Cast<String>(pairs [i++]);
            Object value = pairs [i++];

            this.Set(key, value);
        }
    }

    public T Get<T>(String key)
    {
        Object value = Get(key);
        if (value is T)
        {
            return (T)value;
        }
        return default(T);
    }

    public Object Get(String key)
    {
        Object value;
        if (m_dictionary != null && m_dictionary.TryGetValue(key, out value))
        {
            return value;
        }

        return null;
    }

    internal void Set(String key, Object value)
    {
        if (m_dictionary == null)
        {
            m_dictionary = new Dictionary<String, Object>(1);
        }
        m_dictionary [key] = value;
    }
    
    protected override void OnRecycleObject()
    {
        Sender = null;
        Name = null;

        if (m_dictionary != null)
        {
            m_dictionary.Clear();
        }
    }
    
    public String Name { get; private set; }
    public Object Sender { get; private set; }
}