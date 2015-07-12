package com.spacemadness.lunar;

import android.content.Context;

import com.spacemadness.lunar.console.AppTerminalImp;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.debug.Log;

import java.io.File;
import java.io.IOException;

public abstract class AppTerminal
{
    private static final Object lock = new Object();

    private static AppTerminal instance;

    protected AppTerminal()
    {
    }

    //////////////////////////////////////////////////////////////////////////////
    // Initialization

    public static void initialize(Context context)
    {
        synchronized (lock)
        {
            if (instance != null)
            {
                if (Config.isDebugBuild)
                {
                    throw new IllegalStateException("Instance is already initialized");
                }

                Log.e("Instance is already initialized");
                return;
            }

            try
            {
                instance = new AppTerminalImp(context);
            }
            catch (Exception e)
            {
                Log.logException(e, "Exception while initializing instance");
            }
        }
    }

    public static void destroy()
    {
        synchronized (lock)
        {
            if (instance == null)
            {
                Log.e("Instance is not initialized"); // TODO: replace with warning
                return;
            }

            try
            {
                instance.destroyInstance();
            }
            catch (Exception e)
            {
                Log.logException(e, "Exception while destroying instance");
            }

            instance = null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Commands

    public static boolean executeCommand(String commandLine)
    {
        return executeCommand(commandLine, false);
    }

    public static boolean executeCommand(String commandLine, boolean manualMode)
    {
        if (commandLine == null)
        {
            throw new NullPointerException("Command line is null");
        }

        synchronized (lock)
        {
            if (instance == null)
            {
                if (Config.isDebugBuild)
                {
                    throw new NullPointerException("Instance is not initialized. Can't execute command: " + commandLine);
                }

                Log.c("Instance is not initialized. Can't execute command: %s", commandLine);
                return false;
            }

            return instance.execCommand(commandLine, manualMode);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Static access

    public static NotificationCenter getNotificationCenter()
    {
        return instance.notificationCenter();
    }

    public static TimerManager getTimerManager()
    {
        return instance.timerManager();
    }

    public static TimerManager getBackgroundTimerManager()
    {
        return instance.backgroundTimerManager();
    }

    public static File getConfigsDir()
    {
        return instance.configsDir();
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
    // Inheritance

    protected abstract boolean execCommand(String commandLine, boolean manualMode);

    protected abstract NotificationCenter notificationCenter();

    protected abstract TimerManager timerManager();

    protected abstract TimerManager backgroundTimerManager();

    protected abstract File configsDir();

    protected void destroyInstance()
    {
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    protected static AppTerminal getInstance()
    {
        return instance;
    }
}