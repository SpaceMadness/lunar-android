package com.spacemadness.lunar.console;

import android.content.Context;

import com.spacemadness.lunar.AppTerminal;
import com.spacemadness.lunar.DefaultRuntimePlatform;
import com.spacemadness.lunar.debug.Log;

public class AppTerminalImp extends AppTerminal // TODO: think about merging platform and terminal classes
{
    public AppTerminalImp(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        try
        {
            DefaultRuntimePlatform.initialize(context.getApplicationContext());
        }
        catch (Exception e)
        {
            Log.logException(e, "Exception while initializing terminal");
        }
    }

    @Override
    protected void destroyInstance()
    {
        try
        {
            DefaultRuntimePlatform.destroy();
            super.destroyInstance();
        }
        catch (Exception e)
        {
            Log.logException(e, "Error while destroying terminal");
        }
    }
}
