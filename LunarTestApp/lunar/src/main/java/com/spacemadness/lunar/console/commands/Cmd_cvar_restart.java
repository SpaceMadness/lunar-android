//  Copyright 2015 SpaceMadness.
// 
//  Lunar is licensed under the Apache License, 
//  Version 2.0 (the "License"); you may not use this file except in compliance 
//  with the License. You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.CVarCommand;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.List;

@Command(Name="cvar_restart", Description="Resets all cvars to their default values.")
public class Cmd_cvar_restart extends CCommand
{
    void Execute()
    {
        Execute(null);
    }

    void Execute(String prefix)
    {
        List<CCommand> cmds = CRegistery.ListCommands(prefix);
        for (CCommand cmd : cmds)
        {
            CVarCommand cvarCmd = ClassUtils.as(cmd, CVarCommand.class);
            if (cvarCmd != null)
            {
                cvarCmd.SetDefault();
            }
        }
    }

    @Override
    protected String[] AutoCompleteArgs(String commandLine, String prefix)
    {
        List<CVar> vars = CRegistery.ListVars(prefix);
        if (vars.size() == 0)
        {
            return null;
        }
        
        String[] values = new String[vars.size()];
        for (int i = 0; i < vars.size(); ++i)
        {
            values[i] = StringUtils.C(vars.get(i).Name, ColorCode.TableVar);
        }
        return values;
    }
}