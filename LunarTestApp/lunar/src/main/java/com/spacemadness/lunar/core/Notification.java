package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
class Notification : ObjectsPoolEntry
{
    private IDictionary<string, object> m_dictionary;
    
    internal void Init(Object sender, string name, params object[] pairs)
    {
        Sender = sender;
        Name = name;

        Assert.IsTrue(pairs.Length % 2 == 0);
        for (int i = 0; i < pairs.Length;)
        {
            string key = ClassUtils.Cast<string>(pairs [i++]);
            object value = pairs [i++];

            this.Set(key, value);
        }
    }

    public T Get<T>(string key)
    {
        object value = Get(key);
        if (value is T)
        {
            return (T)value;
        }
        return default(T);
    }

    public object Get(string key)
    {
        object value;
        if (m_dictionary != null && m_dictionary.TryGetValue(key, out value))
        {
            return value;
        }

        return null;
    }

    internal void Set(string key, object value)
    {
        if (m_dictionary == null)
        {
            m_dictionary = new Dictionary<string, object>(1);
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
    
    public string Name { get; private set; }
    public object Sender { get; private set; }
}