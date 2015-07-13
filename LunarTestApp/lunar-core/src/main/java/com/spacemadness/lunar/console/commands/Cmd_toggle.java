package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.CVarCommand;
import com.spacemadness.lunar.console.ListCommandsFilter;
import com.spacemadness.lunar.console.annotations.Arg;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.List;

@Command(Name="toggle", Description="Toggles boolean cvar value.")
public class Cmd_toggle extends CCommand
{
    boolean execute(@Arg("cvar") String cvar)
    {
        CVarCommand cmd = CRegistery.FindCvarCommand(cvar);
        if (cmd == null)
        {
            PrintError("Can't find cvar '" + cvar + "'");
            return false;
        }
        
        if (!cmd.IsInt())
        {
            PrintError("Can't toggle non-int value");
            return false;
        }

        cmd.setParentCommand(this);
        cmd.SetValue(cmd.BoolValue() ? 0 : 1);
        cmd.setParentCommand(null);

        return true;
    }

    @Override
    protected String[] AutoCompleteArgs(String commandLine, final String prefix)
    {

        List<CVar> vars = CRegistery.ListVars(new ListCommandsFilter<CVarCommand>()
        {
            @Override
            public boolean accept(CVarCommand command)
            {
                return command.IsBool() && CRegistery.ShouldListCommand(command, prefix);
            }
        });
        
        if (vars.size() == 0)
        {
            return null;
        }
        
        String[] values = new String[vars.size()];
        for (int i = 0; i < vars.size(); ++i)
        {
            values[i] = StringUtils.C(vars.get(i).Name(), ColorCode.TableVar);
        }
        return values;
    }
}