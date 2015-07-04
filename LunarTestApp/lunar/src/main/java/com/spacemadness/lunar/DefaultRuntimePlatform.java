package com.spacemadness.lunar;

import android.content.Context;
import android.os.Looper;

import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.core.BackgroundTimerManager;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.NotificationCenterImp;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.core.TimerManagerImp;

import java.io.File;
import java.lang.ref.WeakReference;

public class DefaultRuntimePlatform extends RuntimePlatform
{
    private static final String CONFIGS_DIR_NAME = "com.spacemadness.lunar.Configs";

    private WeakReference<Context> contextRef;

    @Override
    protected TimerManager createTimerManager()
    {
        return new TimerManagerImp(Looper.getMainLooper());
    }

    @Override
    protected TimerManager createBackgroundTimerManager()
    {
        return BackgroundTimerManager.create();
    }

    @Override
    protected NotificationCenter createNotificationCenter()
    {
        return new NotificationCenterImp(Looper.getMainLooper());
    }

    @Override
    protected File createConfigsDirFile()
    {
        File filesDir = getExistingContext().getFilesDir();
        return new File(filesDir, CONFIGS_DIR_NAME);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Lifecycle

    public static void initialize(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        getInstance().contextRef = new WeakReference<>(context);

        CRegistery.ResolveCommands();
    }

    public static void destroy()
    {
        // TODO
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public static Context getContext()
    {
        final WeakReference<Context> contextRef = getInstance().contextRef;
        if (contextRef == null)
        {
            throw new NullPointerException("Context reference is not initialized");
        }

        return contextRef.get();
    }

    public static Context getExistingContext()
    {
        Context context = getContext();
        if (context == null)
        {
            throw new NullPointerException("Context is lost");
        }

        return context;
    }

    public static DefaultRuntimePlatform getInstance()
    {
        return (DefaultRuntimePlatform) RuntimePlatform.getInstance();
    }
}
