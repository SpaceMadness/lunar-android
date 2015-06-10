package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.Config;
import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVarCommand;
import com.spacemadness.lunar.console.CommandListOptions;
import com.spacemadness.lunar.console.ListCommandsFilter;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.List;

/**
 * Created by weee on 6/10/15.
 */

@Command(Name="cmdlist", Description="Lists all available terminal commands.")
public class Cmd_cmdlist extends CCommand
{
    @CommandOption(Name="all", ShortName="a", Description="List all commands (including system)")
    private boolean includeSystem;

    boolean Execute()
    {
        return Execute(null);
    }

    boolean Execute(final String prefix)
    {
        final int options = getOptions();

        List<CCommand> commands = CRegistery.ListCommands(new ListCommandsFilter<CCommand>()
        {
            @Override
            public boolean accept(CCommand cmd)
            {
                return !(cmd instanceof CVarCommand) && CRegistery.ShouldListCommand(cmd, prefix, options);
            }
        });

        if (commands.size() > 0)
        {
            String[] names = new String[commands.size()];
            for (int i = 0; i < commands.size(); ++i)
            {
                CCommand cmd = commands.get(i);
                names[i] = StringUtils.C(cmd.Name, cmd.ColorCode);
            }
            Print(names);
        }

        return true;
    }

    private int getOptions()
    {
        int options = CommandListOptions.None;
        if (Config.isDebugBuild)
        {
            options |= CommandListOptions.Debug;
        }
        if (includeSystem)
        {
            options |= CommandListOptions.System;
        }
        return options;
    }
}
