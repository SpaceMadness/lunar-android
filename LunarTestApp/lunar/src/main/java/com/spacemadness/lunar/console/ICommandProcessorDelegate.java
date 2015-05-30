package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 5/29/15.
 */
public interface ICommandProcessorDelegate
{
    void OnCommandExecuted(CommandProcessor processor, CCommand cmd);
    void OnCommandUnknown(CommandProcessor processor, String cmdName);
}
