package com.spacemadness.lunar;

import android.content.Context;

import com.spacemadness.lunar.console.AppTerminalImp;
import com.spacemadness.lunar.debug.Log;

public abstract class AppTerminal // TODO: better class name
{
    private static final Object mutex = new Object();

    private static AppTerminal instance;

    protected AppTerminal()
    {
    }

    //////////////////////////////////////////////////////////////////////////////
    // Initialization

    public static void initialize(Context context)
    {
        synchronized (mutex)
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

            instance = new AppTerminalImp(context);
        }
    }

    public static void destroy()
    {
        synchronized (mutex)
        {
            if (instance == null)
            {
                Log.e("Instance is not initialized");
                return;
            }

            instance.destroyInstance();
            instance = null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Inheritance

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