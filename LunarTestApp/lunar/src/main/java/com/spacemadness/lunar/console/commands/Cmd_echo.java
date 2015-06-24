package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.annotations.Arg;
import com.spacemadness.lunar.console.annotations.Command;

/**
 * Created by alementuev on 6/22/15.
 */

@Command(Name="echo")
public class Cmd_echo extends CCommand
{
    void execute(@Arg("message") String message)
    {
        PrintIndent(message);
    }
}
