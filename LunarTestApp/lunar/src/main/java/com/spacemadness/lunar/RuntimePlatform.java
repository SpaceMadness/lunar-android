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

import static com.spacemadness.lunar.console.CCommandNotifications.*;

public abstract class RuntimePlatform implements IDestroyable
{
    private final Terminal terminal;
    private final NotificationCenter notificationCenter;

    private TimerManager timerManager;
    private TimerManager backgroundTimerManager;
    private File configsDir;

    protected RuntimePlatform()
    {
        notificationCenter = createNotificationCenter();
        registerNotifications();

        terminal = new Terminal(1024);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void Destroy()
    {
        if (timerManager != null)
        {
            timerManager.Destroy();
        }
        if (backgroundTimerManager != null)
        {
            backgroundTimerManager.Destroy();
        }
        notificationCenter.Destroy();
        terminal.Destroy();
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
        public synchronized void run()
        {
            execCommand("writeconfig default.cfg", false);
        }
    };

    private void scheduleSaveConfig()
    {
        synchronized (saveConfigRunnable)
        {
            getTimerManager().ScheduleOnce(saveConfigRunnable);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public Terminal getTerminal()
    {
        return terminal;
    }

    public NotificationCenter getNotificationCenter()
    {
        return notificationCenter;
    }

    public synchronized TimerManager getTimerManager()
    {
        if (timerManager == null)
        {
            timerManager = createTimerManager();
        }

        return timerManager;
    }

    public synchronized TimerManager getBackgroundTimerManager()
    {
        if (backgroundTimerManager == null)
        {
            backgroundTimerManager = createBackgroundTimerManager();
        }

        return backgroundTimerManager;
    }

    public synchronized File getConfigsDir()
    {
        if (configsDir == null)
        {
            configsDir = createConfigsDirFile();
        }

        return configsDir;
    }
}
