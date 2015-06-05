package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weee on 5/28/15.
 */
class NotificationCenter implements IDestroyable
{
    private static NotificationCenter s_sharedInstance;

    private TimerManager m_timerManager;
    private Map<String, NotificationDelegateList> m_registerMap;
    private ObjectsPool<Notification> m_notificatoinsPool;

    static
    {
        s_sharedInstance = new NotificationCenter(TimerManager.SharedInstance());
    }

    private final TimerRunnable PostCallback = new TimerRunnable() // FIXME: rename
    {
        @Override
        public void run(Timer timer)
        {
            Notification notification = ClassUtils.as(timer.userData, Notification.class);
            Assert.IsNotNull(notification);

            PostImmediately(notification);
        }
    };

    public NotificationCenter(TimerManager timerManager)
    {
        m_timerManager = timerManager;
        m_registerMap = new HashMap<String, NotificationDelegateList>();
        m_notificatoinsPool = new ObjectsPool<Notification>();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Shared instance

    public static void RegisterNotification(String name, NotificationDelegate del)
    {
        s_sharedInstance.Register(name, del);
    }

    public static void RegisterNotifications(NotificationInfo... list)
    {
        s_sharedInstance.Register(list);
    }

    public static void UnregisterNotification(String name, NotificationDelegate del)
    {
        s_sharedInstance.Unregister(name, del);
    }

    public static void UnregisterNotifications(NotificationInfo... list)
    {
        s_sharedInstance.Unregister(list);
    }

    public static void UnregisterNotifications(NotificationDelegate del)
    {
        s_sharedInstance.UnregisterAll(del);
    }

    public static void PostNotification(Object sender, String name, Object... data)
    {
        s_sharedInstance.Post(sender, name, data);
    }

    public static void PostNotificationImmediately(Object sender, String name, Object... data)
    {
        s_sharedInstance.PostImmediately(sender, name, data);
    }

    public static NotificationCenter SharedInstance() // TODO: decrease visiblity
    {
        return s_sharedInstance;
    }

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
            m_registerMap.put(name, list);
        }
        
        list.Add(del);
    }

    public void Register(NotificationInfo... list)
    {
        for (int i = 0; i < list.length; ++i)
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

    public void Unregister(NotificationInfo... list)
    {
        for (int i = 0; i < list.length; ++i)
        {
            Unregister(list [i].name, list [i].del);
        }
    }
    
    public boolean UnregisterAll(NotificationDelegate del)
    {
        boolean removed = false;
        for (NotificationDelegateList list : m_registerMap.values())
        {   
            removed |= list.Remove(del);
        }
        return removed;
    }
    
    public void Post(Object sender, String name, Object... data)
    {
        NotificationDelegateList list = FindList(name);
        if (list != null && list.Count() > 0)
        {
            Notification notification = m_notificatoinsPool.NextObject();
            notification.Init(sender, name, data);
            
            SchedulePost(notification);
        }
    }
    
    public void PostImmediately(Object sender, String name, Object... data)
    {   
        NotificationDelegateList list = FindList(name);
        if (list != null && list.Count() > 0)
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
        return m_registerMap.get(name);
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

    //////////////////////////////////////////////////////////////////////////////
    // Unit Testing

    private Map<String, NotificationDelegateList> RegistryMap()
    {
        return m_registerMap;
    }
}