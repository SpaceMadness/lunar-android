package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.annotations.Command;

@Command(Name = "throwException")
public class Cmd_throwException extends CCommand
{
    void execute()
    {
        throw new RuntimeException("Test exception");
    }
}
