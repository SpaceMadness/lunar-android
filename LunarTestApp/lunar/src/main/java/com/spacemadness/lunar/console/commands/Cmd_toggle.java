package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.CVarCommand;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.utils.StringUtils;

@Command("toggle", Description="Toggles boolean cvar value.")
public class Cmd_toggle extends CCommand
{
    bool Execute(string cvarName)
    {
        CVarCommand cmd = CRegistery.FindCvarCommand(cvarName);
        if (cmd == null)
        {
            PrintError("Can't find cvar '" + cvarName + "'");
            return false;
        }
        
        if (!cmd.IsInt)
        {
            PrintError("Can't toggle non-int value");
            return false;
        }
        
        cmd.SetValue(cmd.BoolValue ? 0 : 1);
        return true;
    }
    
    protected override string[] AutoCompleteArgs(string commandLine, string prefix)
    {
        IList<CVar> vars = CRegistery.ListVars(delegate(CVarCommand cmd)
        {
            return cmd.IsBool && CRegistery.ShouldListCommand(cmd, prefix);
        });
        
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