package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.Config;
import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.CommandListOptions;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.utils.StringUtils;

@Command("cvarlist", Description="Lists all available cvars and their values.")
public class Cmd_cvarlist extends CCommand
{
    @CommandOption(Name="short", ShortName="s", Description="Outputs only names")
    private boolean shortList;
    
    @CommandOption(Name="all", ShortName="a", Description="List all vars (including system)")
    private boolean includeSystem;

    boolean Execute()
    {
        return Execute(null);
    }

    boolean Execute(String prefix)
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
        
        // TODO: refactoring
        List<CVar> vars = CRegistery.ListVars(prefix, options);
        if (vars.Count > 0)
        {
            if (shortList)
            {
                String[] names = new String[vars.Count];
                for (int i = 0; i < vars.Count; ++i)
                {
                    names[i] = StringUtils.C(vars[i].Name, ColorCode.TableVar);
                }
                Print(names);
            }
            else
            {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < vars.Count; ++i)
                {
                    CVar cvar = vars[i];
                    result.AppendFormat("  {0} {1}", StringUtils.C(cvar.Name, ColorCode.TableVar), StringUtils.Arg(cvar.Value));
                    
                    // TODO: better color highlight
                    if (!cvar.IsDefault)
                    {
                        result.AppendFormat(" {0} {1}", StringUtils.C("default", ColorCode.TableVar), cvar.DefaultValue);
                    }
                    
                    if (i < vars.Count - 1)
                    {
                        result.Append('\n');
                    }
                }
                
                Print(result.ToString());
            }
        }
        
        return true;
    }
}