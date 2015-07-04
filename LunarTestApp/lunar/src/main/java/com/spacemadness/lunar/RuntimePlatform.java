package com.spacemadness.lunar;

import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.core.Notification;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.NotificationDelegate;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.debug.Assert;

import java.io.File;
import java.io.IOException;

import static com.spacemadness.lunar.console.CCommandNotifications.*;

public abstract class RuntimePlatform
{
    private static final RuntimePlatform instance = new DefaultRuntimePlatform();

    private TimerManager timerManager;
    private TimerManager backgroundTimerManager;
    private NotificationCenter notificationCenter;
    private File configsDir;

    protected RuntimePlatform()
    {
        notificationCenter = createNotificationCenter();
        registerNotifications();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Static access

    public static NotificationCenter getNotificationCenter()
    {
        return instance.notificationCenter;
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
    // Lazy object initialization

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

    protected void registerNotifications()
    {
        getNotificationCenter().Register(CVarValueChanged, new NotificationDelegate()
        {
            @Override
            public void onNotification(Notification n)
            {
                boolean manual = (boolean) n.Get(KeyManualMode);
                if (manual)
                {
                    CVar cvar = (CVar) n.Get(CVarValueChangedKeyVar);
                    Assert.IsNotNull(cvar);

                    if (cvar != null)
                    {
                        ScheduleSaveConfig();
                    }
                }
            }
        });
        getNotificationCenter().Register(CBindingsChanged, new NotificationDelegate()
        {
            @Override
            public void onNotification(Notification n)
            {
            }
        });
    }

    protected abstract TimerManager createTimerManager();

    protected abstract TimerManager createBackgroundTimerManager();

    protected abstract NotificationCenter createNotificationCenter();

    protected abstract File createConfigsDirFile();

    //////////////////////////////////////////////////////////////////////////////
    // Config

    private void ScheduleSaveConfig()
    {

    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    protected static RuntimePlatform getInstance()
    {
        return instance;
    }
}
