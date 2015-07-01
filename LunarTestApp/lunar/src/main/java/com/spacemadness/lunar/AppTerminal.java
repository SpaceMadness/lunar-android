package com.spacemadness.lunar;

import android.content.Context;

import com.spacemadness.lunar.console.AppTerminalImpl;

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
                throw new IllegalStateException("Instance is already initialized");
            }

            instance = new AppTerminalImpl(context);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    protected static AppTerminal getInstance()
    {
        return instance;
    }
}