package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.CVarCommand;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.utils.StringUtils;

@Command("reset", Description="Resets cvar to its default value.")
public class Cmd_reset extends CCommand
{
    bool Execute(string name)
    {
        CVarCommand cmd = CRegistery.FindCvarCommand(name);
        if (cmd == null)
        {
            PrintError("Can't find cvar: '{0}'", name);
            return false;
        }
        
        cmd.SetDefault();
        return true;
    }
    
    protected override string[] AutoCompleteArgs(string commandLine, string prefix)
    {
        IList<CVar> vars = CRegistery.ListVars(prefix);
        if (vars.Count == 0)
        {
            return null;
        }
        
        string[] values = new string[vars.Count];
        for (int i = 0; i < vars.Count; ++i)
        {
            values[i] = StringUtils.C(vars[i].Name, ColorCode.TableVar);
        }
        return values;
    }
}