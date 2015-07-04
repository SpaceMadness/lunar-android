package com.spacemadness.lunar;

import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.Terminal;
import com.spacemadness.lunar.core.IDestroyable;
import com.spacemadness.lunar.core.Notification;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.NotificationDelegate;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.debug.Assert;

import java.io.File;
import java.io.IOException;

import static com.spacemadness.lunar.console.CCommandNotifications.*;

public abstract class RuntimePlatform implements IDestroyable
{
    private static RuntimePlatform instance;

    private final Terminal terminal;
    private final NotificationCenter notificationCenter;

    private TimerManager timerManager;
    private TimerManager backgroundTimerManager;
    private File configsDir;

    protected RuntimePlatform()
    {
        RuntimePlatform.instance = this; // TODO: thread safety and error checking

        notificationCenter = createNotificationCenter();
        registerNotifications();

        terminal = new Terminal(1024);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void Destroy()
    {
        RuntimePlatform.instance = null;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Static access

    public static boolean executeCommand(String commandLine)
    {
        return executeCommand(commandLine, false);
    }

    public static boolean executeCommand(String commandLine, boolean manualMode)
    {
        return instance.execCommand(commandLine, manualMode);
    }

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
                boolean manual = n.getBool(KeyManualMode, false);
                if (manual)
                {
                    CVar cvar = (CVar) n.Get(CVarValueChangedKeyVar);
                    Assert.IsNotNull(cvar);

                    if (cvar != null)
                    {
                        scheduleSaveConfig();
                    }
                }
            }
        });
        getNotificationCenter().Register(CBindingsChanged, new NotificationDelegate()
        {
            @Override
            public void onNotification(Notification n)
            {
                boolean manual = n.getBool(KeyManualMode, false);
                if (manual)
                {
                    scheduleSaveConfig();
                }
            }
        });
        getNotificationCenter().Register(CAliasesChanged, new NotificationDelegate()
        {
            @Override
            public void onNotification(Notification n)
            {
                boolean manual = n.getBool(KeyManualMode, false);
                if (manual)
                {
                    scheduleSaveConfig();
                }
            }
        });
    }

    protected abstract TimerManager createTimerManager();

    protected abstract TimerManager createBackgroundTimerManager();

    protected abstract NotificationCenter createNotificationCenter();

    protected abstract File createConfigsDirFile();

    //////////////////////////////////////////////////////////////////////////////
    // Terminal

    public boolean execCommand(String commandLine, boolean manualMode)
    {
        return terminal.ExecuteCommandLine(commandLine, manualMode);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Config

    private Runnable saveConfigRunnable = new Runnable() // TODO: lazy initialization
    {
        @Override
        public void run()
        {
            RuntimePlatform.executeCommand("writeconfig default.cfg");
        }
    };

    private void scheduleSaveConfig()
    {
        getTimerManager().ScheduleOnce(saveConfigRunnable);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    protected static RuntimePlatform getInstance()
    {
        return instance; // TODO: thread safety
    }

    protected static RuntimePlatform getExistingInstance()
    {
        RuntimePlatform instance = getInstance();
        Assert.IsNotNull(instance, "Instance is not initialized");
        return instance;
    }
}
