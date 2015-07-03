package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 5/28/15.
 */
public interface ICCommandDelegate
{
    void LogTerminal(String message);
    void LogTerminal(String[] table);
    void LogTerminal(Throwable e, String message);

    boolean ExecuteCommandLine(String commandLine, boolean manual);

    boolean IsPromptEnabled(); // FIXME: remove this method
}
