package com.spacemadness.lunar.console;

import android.content.Context;

import com.spacemadness.lunar.AppTerminal;
import com.spacemadness.lunar.debug.Log;

import java.io.File;
import java.lang.ref.WeakReference;

public class AppTerminalImpl extends AppTerminal
{
    public AppTerminalImpl(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        final WeakReference<Context> contextRef = new WeakReference<>(context.getApplicationContext());

        Global.setInstance(new Global()
        {
            @Override
            protected File getFilesDirImpl()
            {
                Context context = getContextImpl();
                return context != null ? context.getFilesDir() : null;
            }

            @Override
            protected File getCacheDirImpl()
            {
                Context context = getContextImpl();
                return context != null ? context.getCacheDir() : null;
            }

            @Override
            protected Context getContextImpl()
            {
                Context context = contextRef.get();
                if (context == null) Log.logCrit("Context is lost");
                return context;
            }
        });

        CRegistery.ResolveCommands();
    }
}
