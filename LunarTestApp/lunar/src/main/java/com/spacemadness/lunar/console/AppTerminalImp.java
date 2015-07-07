package com.spacemadness.lunar.console;

import android.content.Context;

import com.spacemadness.lunar.AppTerminal;
import com.spacemadness.lunar.DefaultRuntimePlatform;
import com.spacemadness.lunar.RuntimePlatform;
import com.spacemadness.lunar.RuntimePlatformFactory;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.debug.Log;

import java.io.File;

public class AppTerminalImp extends AppTerminal
{
    private static final RuntimePlatformFactory runtimePlatformFactory = new RuntimePlatformFactory()
    {
        @Override
        public RuntimePlatform createRuntimePlatform(Context context)
        {
            return new DefaultRuntimePlatform(context);
        }
    };

    private final RuntimePlatform runtimePlatform;

    public AppTerminalImp(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        runtimePlatform = runtimePlatformFactory.createRuntimePlatform(context.getApplicationContext());
    }

    @Override
    protected boolean execCommand(String commandLine, boolean manualMode)
    {
        return runtimePlatform.execCommand(commandLine, manualMode);
    }

    @Override
    protected NotificationCenter notificationCenter()
    {
        return runtimePlatform.getNotificationCenter();
    }

    @Override
    protected TimerManager timerManager()
    {
        return runtimePlatform.getTimerManager();
    }

    @Override
    protected TimerManager backgroundTimerManager()
    {
        return runtimePlatform.getBackgroundTimerManager();
    }

    @Override
    protected File configsDir()
    {
        return runtimePlatform.getConfigsDir();
    }

    @Override
    protected void destroyInstance()
    {
        runtimePlatform.Destroy();
    }
}
