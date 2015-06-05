package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 5/28/15.
 */
public interface ICCommandDelegate
{
    void LogTerminal(String message);
    void LogTerminal(String[] table);
    void LogTerminal(Exception e, String message);

    void ClearTerminal();

    boolean ExecuteCommandLine(String commandLine, boolean manual);
    void PostNotification(CCommand cmd, String name, Object... data);

    boolean IsPromptEnabled();
}
