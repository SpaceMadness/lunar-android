package com.spacemadness.lunar.console;

import android.content.Context;

import com.spacemadness.lunar.AppTerminal;
import com.spacemadness.lunar.DefaultRuntimePlatform;

public class AppTerminalImp extends AppTerminal
{
    public AppTerminalImp(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        DefaultRuntimePlatform.setContext(context.getApplicationContext());
        CRegistery.ResolveCommands();
    }
}
