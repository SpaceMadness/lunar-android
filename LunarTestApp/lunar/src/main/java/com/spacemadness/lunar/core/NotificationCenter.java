package com.spacemadness.lunar.core;

import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

public class NotificationCenter implements IDestroyable
{
    private static final Object[] EMPTY_DATA = new Object[0];

    private final Map<String, NotificationDelegateList> delegateLookup;
    private final ObjectsPool<NotificationObject> notificationPool; // TODO: make shared pool
    private final TimerManager timerManager;

    public NotificationCenter(Looper looper)
    {
        this(new TimerManager(looper));
    }

    public NotificationCenter(TimerManager timerManager)
    {
        if (timerManager == null)
        {
            throw new NullPointerException("Timer manager is null");
        }

        this.timerManager = timerManager;

        delegateLookup = new HashMap<>();
        notificationPool = new ObjectsPoolConcurrent<>(NotificationObject.class); // TODO: make concurrent?
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
            delegateLookup.put(name, list);
        }
        
        list.Add(del);
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

    public boolean UnregisterAll(NotificationDelegate del)
    {
        boolean removed = false;
        for (NotificationDelegateList list : delegateLookup.values())
        {   
            removed |= list.Remove(del);
        }
        return removed;
    }

    public void Post(Object sender, String name)
    {
        Post(sender, name, EMPTY_DATA);
    }

    public void Post(Object sender, String name, Object... data)
    {
        if (data == null)
        {
            throw new NullPointerException("Data is null");
        }

        NotificationDelegateList list = FindList(name);
        if (list != null && list.Count() > 0)
        {
            NotificationObject notification = createNotification();
            notification.Init(sender, name, data);
            
            SchedulePost(notification);
        }
    }

    public void PostImmediately(Object sender, String name, Object... data)
    {   
        NotificationDelegateList list = FindList(name);
        if (list != null && list.Count() > 0)
        {
            NotificationObject notification = createNotification();
            notification.Init(sender, name, data);
            
            list.NotifyDelegates(notification);
            notification.Recycle();
        }
    }
    
    void PostImmediately(NotificationObject notification)
    {
        String name = notification.getName();
        NotificationDelegateList list = FindList(name);
        if (list != null)
        {
            list.NotifyDelegates(notification);
        }
        notification.Recycle();
    }

    private NotificationObject createNotification()
    {
        NotificationObject notification = notificationPool.NextObject();
        notification.notificationCenter = this;
        return notification;
    }
    
    private NotificationDelegateList FindList(String name)
    {
        return delegateLookup.get(name);
    }
    
    private void SchedulePost(NotificationObject notification)
    {
        timerManager.Schedule(notification);
    }
    
    private void CancelScheduledPosts()
    {
        timerManager.cancelAll(); // FIXME: better cancellation
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    TimerManager getTimerManager()
    {
        return timerManager;
    }

    int getNotificationPoolSize()
    {
        return notificationPool.size();
    }
}