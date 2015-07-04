package com.spacemadness.lunar.console;

import android.content.Context;

import com.spacemadness.lunar.AppTerminal;
import com.spacemadness.lunar.DefaultRuntimePlatform;
import com.spacemadness.lunar.RuntimePlatform;
import com.spacemadness.lunar.debug.Log;

public class AppTerminalImp extends AppTerminal
{
    private final RuntimePlatform runtimePlatform;

    public AppTerminalImp(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        runtimePlatform = new DefaultRuntimePlatform(context.getApplicationContext());
    }

    @Override
    protected boolean execCommand(String commandLine, boolean manualMode)
    {
        return runtimePlatform.execCommand(commandLine, manualMode);
    }

    @Override
    protected void destroyInstance()
    {
        runtimePlatform.Destroy();
    }
}
