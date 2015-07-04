package com.spacemadness.lunar;

import android.content.Context;
import android.os.Looper;

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

    private final WeakReference<Context> contextRef;

    public DefaultRuntimePlatform(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        contextRef = new WeakReference<>(context);
    }

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
    // Getters/Setters

    public static Context getContext()
    {
        return getExistingInstance().contextRef.get();
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

    protected static DefaultRuntimePlatform getInstance()
    {
        return (DefaultRuntimePlatform) RuntimePlatform.getInstance();
    }

    protected static DefaultRuntimePlatform getExistingInstance()
    {
        return (DefaultRuntimePlatform) RuntimePlatform.getExistingInstance();
    }
}
