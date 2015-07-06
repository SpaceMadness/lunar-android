package com.spacemadness.lunar;

import android.content.Context;

import com.spacemadness.lunar.core.IDestroyable;
import com.spacemadness.lunar.core.MockTimerManager;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.FileUtils;

import junit.framework.AssertionFailedError;

import java.io.File;

public class MockRuntimePlatform extends RuntimePlatform implements IDestroyable
{
    private static MockRuntimePlatform currentInstance;
    private final Context context;

    public MockRuntimePlatform(Context context)
    {
        this.context = context;
        currentInstance = this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void Destroy()
    {
        super.Destroy();

        currentInstance = null;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Inheritance

    @Override
    protected TimerManager createTimerManager()
    {
        return MockTimerManager.Null;
    }

    @Override
    protected TimerManager createBackgroundTimerManager()
    {
        return MockTimerManager.Null;
    }

    @Override
    protected NotificationCenter createNotificationCenter()
    {
        return MockNotificationCenter.Null;
    }

    @Override
    protected File createConfigsDirFile()
    {
        return new File(context.getCacheDir(), "configs");
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    public static void waitUntilNotificationsDispatched() throws InterruptedException
    {
        MockNotificationCenter mock = ClassUtils.as(getCurrentInstance().getNotificationCenter(), MockNotificationCenter.class);
        if (mock != null)
        {
            mock.waitUntilNotificationsDispatched();
        }
    }

    public static void waitUntilTimersFinished() throws InterruptedException
    {
        MockTimerManager mock = ClassUtils.as(getCurrentInstance().getTimerManager(), MockTimerManager.class);
        if (mock != null)
        {
            mock.waitUntilTimersFinished();
        }
    }

    public static void deleteConfigsDir()
    {
        FileUtils.delete(getCurrentInstance().getConfigsDir());
    }

    private static MockRuntimePlatform getCurrentInstance()
    {
        if (currentInstance == null)
        {
            throw new AssertionFailedError("Instance is not initialized");
        }

        return currentInstance;
    }
}