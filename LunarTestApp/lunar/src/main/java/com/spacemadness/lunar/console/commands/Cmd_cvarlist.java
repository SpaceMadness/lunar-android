package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.Config;
import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.CommandListOptions;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;

import java.util.List;

import static com.spacemadness.lunar.utils.StringUtils.Arg;
import static com.spacemadness.lunar.utils.StringUtils.C;
import static com.spacemadness.lunar.utils.StringUtils.TryFormat;

@Command(Name="cvarlist", Description="Lists all available cvars and their values.")
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
        if (vars.size() > 0)
        {
            if (shortList)
            {
                String[] names = new String[vars.size()];
                for (int i = 0; i < vars.size(); ++i)
                {
                    names[i] = C(vars.get(i).Name(), ColorCode.TableVar);
                }
                Print(names);
            }
            else
            {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < vars.size(); ++i)
                {
                    CVar cvar = vars.get(i);
                    result.append(TryFormat("  %s %s", C(cvar.Name(), ColorCode.TableVar), Arg(cvar.Value())));
                    
                    // TODO: better color highlight
                    if (!cvar.IsDefault())
                    {
                        result.append(TryFormat(" %s %s", C("default", ColorCode.TableVar), cvar.DefaultValue()));
                    }
                    
                    if (i < vars.size() - 1)
                    {
                        result.append('\n');
                    }
                }
                
                Print(result.toString());
            }
        }
        
        return true;
    }
}