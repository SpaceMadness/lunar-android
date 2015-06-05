package com.spacemadness.lunar.console;

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
    public void LogTerminal(Exception e, String message)
    {
    }

    @Override
    public void ClearTerminal()
    {
    }

    @Override
    public boolean ExecuteCommandLine(String commandLine, boolean manual)
    {
        return false;
    }

    @Override
    public void PostNotification(CCommand cmd, String name, Object... data)
    {
    }

    @Override
    public boolean IsPromptEnabled()
    {
        return false;
    }
}
