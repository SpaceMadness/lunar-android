package com.spacemadness.lunar;

import com.spacemadness.lunar.core.MockTimerManager;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.NotificationCenterImp;
import com.spacemadness.lunar.core.NotificationDelegate;
import com.spacemadness.lunar.core.NotificationObject;

public class MockNotificationCenter extends NotificationCenterImp
{
    public static final NotificationCenter Null = new NotificationCenter()
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

    private final MockTimerManager timerManager;

    public MockNotificationCenter()
    {
        super(new MockTimerManager());
        timerManager = (MockTimerManager) getTimerManager();
    }

    public void waitUntilNotificationsDispatched() throws InterruptedException
    {
        timerManager.sleep();
    }
}
