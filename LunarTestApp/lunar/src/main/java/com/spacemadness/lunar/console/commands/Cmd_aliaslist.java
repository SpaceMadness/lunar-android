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

import com.spacemadness.lunar.console.CAliasCommand;
import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.annotations.Arg;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.List;

@Command(Name="aliaslist", Description="List current aliases")
public class Cmd_aliaslist extends CCommand
{
    @CommandOption(Name="short", ShortName="s", Description="Outputs only names")
    private boolean shortList;

    boolean execute()
    {
        return execute(null);
    }

    boolean execute(@Arg("prefix") String prefix)
    {
        List<CAliasCommand> cmds = CRegistery.ListAliases(prefix);
        if (cmds.size() > 0)
        {
            if (shortList)
            {
                String[] names = new String[cmds.size()];
                for (int i = 0; i < cmds.size(); ++i)
                {
                    names[i] = cmds.get(i).Name;
                }
                Print(names);
            }
            else
            {
                for (CAliasCommand cmd : cmds)
                {
                    PrintIndent("%s %s", cmd.Name, cmd.Alias);
                }
            }
        }
        
        return true;
    }
    
    public static void ListAliasesConfig(List<String> lines)
    {
        List<CAliasCommand> aliases = CRegistery.ListAliases();
        
        for (int i = 0; i < aliases.size(); ++i)
        {
            lines.add(ToString(aliases.get(i)));
        }
    }
    
    private static String ToString(CAliasCommand cmd)
    {
        return String.format("alias %s %s", cmd.Name, StringUtils.Arg(cmd.Alias));
    }
}