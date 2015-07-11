package com.spacemadness.lunar.console;

public interface ICCommandDelegate
{
    void LogTerminal(String message);
    void LogTerminal(String[] table);
    void LogTerminal(Throwable e, String message);

    boolean ExecuteCommandLine(String commandLine, boolean manual);

    boolean IsPromptEnabled(); // FIXME: remove this method
}
