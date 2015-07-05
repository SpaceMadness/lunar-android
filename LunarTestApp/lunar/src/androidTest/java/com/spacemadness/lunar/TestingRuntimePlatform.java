package com.spacemadness.lunar;

import com.spacemadness.lunar.core.BackgroundTimerManager;
import com.spacemadness.lunar.core.IDestroyable;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.NotificationDelegate;
import com.spacemadness.lunar.core.NotificationObject;
import com.spacemadness.lunar.core.Timer;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.utils.ClassUtilsEx;
import com.spacemadness.lunar.utils.ObjectUtils;

import java.io.File;

import static com.spacemadness.lunar.utils.ClassUtils.as;

public class TestingRuntimePlatform extends RuntimePlatform implements IDestroyable
{
    private static final TimerManager nullTimerManager = new TimerManager()
    {
        @Override
        public Timer ScheduleOnce(Runnable target, long delayMillis)
        {
            throw new NullPointerException("Attempt to scheduleOnce() on null manager");
        }

        @Override
        public Timer Schedule(Runnable target, long delayMillis)
        {
            throw new NullPointerException("Attempt to schedule() on null manager");
        }

        @Override
        public Timer Cancel(Runnable target)
        {
            throw new NullPointerException("Attempt to cancel() on null manager");
        }

        @Override
        public void cancelAll()
        {
            throw new NullPointerException("Attempt to cancelAll() on null manager");
        }

        @Override
        public void Destroy()
        {
        }
    };

    private static final NotificationCenter nullNotificationCenter = new NotificationCenter()
    {
        @Override
        public void Register(String name, NotificationDelegate del)
        {
        }

        @Override
        public boolean Unregister(String name, NotificationDelegate del)
        {
            return false;
        }

        @Override
        public boolean UnregisterAll(NotificationDelegate del)
        {
            return false;
        }

        @Override
        public void Post(Object sender, String name, Object... data)
        {
        }

        @Override
        public void PostImmediately(Object sender, String name, Object... data)
        {
        }

        @Override
        protected void PostImmediately(NotificationObject notification)
        {
        }

        @Override
        public void Destroy()
        {
        }
    };

    public static TimerManager timerManager;
    public static BackgroundTimerManager backgroundTimerManager;
    public static NotificationCenter notificationCenter;

    public static void init() throws Exception
    {
        RuntimePlatform instance = new TestingRuntimePlatform();
        ClassUtilsEx.setField(RuntimePlatform.class, null, "instance", instance);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void Destroy()
    {
        BackgroundTimerManager m = as(getBackgroundTimerManager(), BackgroundTimerManager.class);
        if (m != null)
        {
            m.Destroy();
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Inheritance

    @Override
    protected TimerManager createTimerManager()
    {
        TimerManager manager = ObjectUtils.notNullOrDefault(timerManager, nullTimerManager);
        timerManager = null;
        return manager;
    }

    @Override
    protected TimerManager createBackgroundTimerManager()
    {
        TimerManager manager = ObjectUtils.notNullOrDefault(backgroundTimerManager, nullTimerManager);
        backgroundTimerManager = null;
        return manager;
    }

    @Override
    protected NotificationCenter createNotificationCenter()
    {
        NotificationCenter center = ObjectUtils.notNullOrDefault(notificationCenter, nullNotificationCenter);
        notificationCenter = null;
        return center;
    }

    @Override
    protected File createConfigsDirFile()
    {
        return new File(".");
    }
}