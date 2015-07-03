package com.spacemadness.lunar;

import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.debug.Assert;

import java.io.File;
import java.io.IOException;

public abstract class RuntimePlatform
{
    private static final RuntimePlatform instance = new DefaultRuntimePlatform();

    private TimerManager timerManager;
    private TimerManager backgroundTimerManager;
    private NotificationCenter notificationCenter;
    private File configsDir;

    //////////////////////////////////////////////////////////////////////////////
    // Static access

    public static NotificationCenter getNotificationCenter()
    {
        return instance.resolveNotificationCenter();
    }

    public static TimerManager getTimerManager()
    {
        return instance.resolveTimerManager();
    }

    public static TimerManager getBackgroundTimerManager()
    {
        return instance.resolveBackgroundTimerManager();
    }

    public static File getConfigsDir()
    {
        return instance.resolveConfigsDir();
    }

    public static File getConfigsDir(boolean createIfNeccesary) throws IOException
    {
        final File configsDir = getConfigsDir();
        if (!configsDir.exists() && createIfNeccesary && !configsDir.mkdirs())
        {
            throw new IOException("Can't create configs dir: " + configsDir);
        }

        return configsDir;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Lazy object creation

    private synchronized NotificationCenter resolveNotificationCenter()
    {
        if (notificationCenter == null)
        {
            notificationCenter = createNotificationCenter();
        }

        return notificationCenter;
    }

    private synchronized TimerManager resolveTimerManager()
    {
        if (timerManager == null)
        {
            timerManager = createTimerManager();
        }

        return timerManager;
    }

    private synchronized TimerManager resolveBackgroundTimerManager()
    {
        if (backgroundTimerManager == null)
        {
            backgroundTimerManager = createBackgroundTimerManager();
        }

        return backgroundTimerManager;
    }

    private synchronized File resolveConfigsDir()
    {
        if (configsDir == null)
        {
            configsDir = createConfigsDirFile();
        }

        return configsDir;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Inheritance

    protected abstract TimerManager createTimerManager();

    protected abstract TimerManager createBackgroundTimerManager();

    protected abstract NotificationCenter createNotificationCenter();

    protected abstract File createConfigsDirFile();

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    protected static RuntimePlatform getInstance()
    {
        return instance;
    }
}
