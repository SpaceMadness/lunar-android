package com.spacemadness.lunar.console;

import android.content.Context;

import java.io.File;

public abstract class Global
{
    private static Global instance;

    public static Context getContext()
    {
        return instance.getContextImpl();
    }

    public static File getCacheDir()
    {
        return instance.getCacheDirImpl();
    }

    public static File getFilesDir()
    {
        return instance.getFilesDirImpl();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Initialization

    public static void setInstance(Global instance)
    {
        if (instance == null)
        {
            throw new NullPointerException("Instance is null");
        }
        Global.instance = instance;
    }


    //////////////////////////////////////////////////////////////////////////////
    // Inheritance

    protected abstract Context getContextImpl();

    protected abstract File getFilesDirImpl();
    protected abstract File getCacheDirImpl();
}
