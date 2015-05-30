package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
class NotificationCenter : IDestroyable
{
    private static NotificationCenter s_sharedInstance;

    private TimerManager m_timerManager;
    private IDictionary<String, NotificationDelegateList> m_registerMap;
    private ObjectsPool<Notification> m_notificatoinsPool;

    static NotificationCenter()
    {
        s_sharedInstance = new NotificationCenter(TimerManager.SharedInstance);
    }

    public NotificationCenter(TimerManager timerManager)
    {
        m_timerManager = timerManager;
        m_registerMap = new Dictionary<String, NotificationDelegateList>();
        m_notificatoinsPool = new ObjectsPool<Notification>();
    }

    #region Shared instance

    public static void RegisterNotification(String name, NotificationDelegate del)
    {
        s_sharedInstance.Register(name, del);
    }

    public static void RegisterNotifications(params NotificationInfo[] list)
    {
        s_sharedInstance.Register(list);
    }

    public static void UnregisterNotification(String name, NotificationDelegate del)
    {
        s_sharedInstance.Unregister(name, del);
    }

    public static void UnregisterNotifications(params NotificationInfo[] list)
    {
        s_sharedInstance.Unregister(list);
    }

    public static void UnregisterNotifications(Object target)
    {
        s_sharedInstance.UnregisterAll(target);
    }

    public static void UnregisterNotifications(NotificationDelegate del)
    {
        s_sharedInstance.UnregisterAll(del);
    }

    public static void PostNotification(Object sender, String name, params Object[] data)
    {
        s_sharedInstance.Post(sender, name, data);
    }

    public static void PostNotificationImmediately(Object sender, String name, params Object[] data)
    {
        s_sharedInstance.PostImmediately(sender, name, data);
    }

    public static NotificationCenter SharedInstance // TODO: decrease visiblity
    {
        get { return s_sharedInstance; }
    }

    #endregion
    
    public void Destroy()
    {
        CancelScheduledPosts();
    }
    
    public void Register(String name, NotificationDelegate del)
    {
        if (name == null)
        {
            throw new NullPointerException("Name is null");
        }

        if (del == null)
        {
            throw new NullPointerException("Delegate is null");
        }
        
        NotificationDelegateList list = FindList(name);
        if (list == null)
        {
            list = new NotificationDelegateList();
            m_registerMap [name] = list;
        }
        
        list.Add(del);
    }

    public void Register(params NotificationInfo[] list)
    {
        for (int i = 0; i < list.Length; ++i)
        {
            Register(list [i].name, list [i].del);
        }
    }
    
    public boolean Unregister(String name, NotificationDelegate del)
    {
        NotificationDelegateList list = FindList(name);
        if (list != null)
        {
            return list.Remove(del);
        }
        
        return false;
    }

    public void Unregister(params NotificationInfo[] list)
    {
        for (int i = 0; i < list.Length; ++i)
        {
            Unregister(list [i].name, list [i].del);
        }
    }
    
    public boolean UnregisterAll(NotificationDelegate del)
    {
        boolean removed = false;
        foreach (KeyValuePair<String, NotificationDelegateList> e in m_registerMap)
        {   
            NotificationDelegateList list = e.Value;
            removed |= list.Remove(del);
        }
        return removed;
    }
    
    public boolean UnregisterAll(Object target)
    {
        boolean removed = false;
        foreach (KeyValuePair<String, NotificationDelegateList> e in m_registerMap)
        {
            NotificationDelegateList list = e.Value;
            removed |= list.RemoveAll(target);
        }
        return removed;
    }
    
    public void Post(Object sender, String name, params Object[] data)
    {
        NotificationDelegateList list = FindList(name);
        if (list != null && list.Count > 0)
        {
            Notification notification = m_notificatoinsPool.NextObject();
            notification.Init(sender, name, data);
            
            SchedulePost(notification);
        }
    }
    
    public void PostImmediately(Object sender, String name, params Object[] data)
    {   
        NotificationDelegateList list = FindList(name);
        if (list != null && list.Count > 0)
        {
            Notification notification = m_notificatoinsPool.NextObject();
            notification.Init(sender, name, data);
            
            list.NotifyDelegates(notification);
            notification.Recycle();
        }
    }
    
    public void PostImmediately(Notification notification)
    {
        String name = notification.Name;
        NotificationDelegateList list = FindList(name);
        if (list != null)
        {
            list.NotifyDelegates(notification);
        }
        notification.Recycle();
    }
    
    private NotificationDelegateList FindList(String name)
    {
        NotificationDelegateList list;
        if (m_registerMap.TryGetValue(name, out list))
        {
            return list;
        }
        
        return null;
    }
    
    private void SchedulePost(Notification notification)
    {
        Timer timer = m_timerManager.Schedule(PostCallback);
        timer.userData = notification;
    }
    
    private void CancelScheduledPosts()
    {
        m_timerManager.Cancel(PostCallback);
    }
    
    private void PostCallback(Timer timer)
    {
        Notification notification = timer.userData as Notification;
        Assert.IsNotNull(notification);
        
        PostImmediately(notification);
    }

    #if LUNAR_DEVELOPMENT

    public IDictionary<String, NotificationDelegateList> RegistryMap
    {
        get { return m_registerMap; }
    }

    #endif
}