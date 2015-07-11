package com.spacemadness.lunar.console;

import android.support.annotation.NonNull;

/**
 * Created by alementuev on 6/1/15.
 */
public class NullCommandDelegate implements ICCommandDelegate
{
    public static final ICCommandDelegate Instance = new NullCommandDelegate();

    private NullCommandDelegate() {}

    @Override
    public void LogTerminal(String message)
    {
    }

    @Override
    public void LogTerminal(String[] table)
    {
    }

    @Override
    public void LogTerminal(Throwable e, String message)
    {
    }

    @Override
    public void LogTerminal(TerminalEntry entry)
    {
    }

    @Override
    public boolean ExecuteCommandLine(String commandLine, boolean manual)
    {
        return false;
    }

    @Override
    public boolean IsPromptEnabled()
    {
        return false;
    }
}
