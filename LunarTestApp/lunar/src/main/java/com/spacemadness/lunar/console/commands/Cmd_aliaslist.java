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
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;

@Command("aliaslist", Description="List current aliases")
public class Cmd_aliaslist extends CCommand
{
    @CommandOption(Name="short", ShortName="s", Description="Outputs only names")
    private bool shortList;
    
    bool Execute(string prefix = null)
    {
        IList<CAliasCommand> cmds = CRegistery.ListAliases(prefix);
        if (cmds.Count > 0)
        {
            if (shortList)
            {
                string[] names = new string[cmds.Count];
                for (int i = 0; i < cmds.Count; ++i)
                {
                    names[i] = cmds[i].Name;
                }
                Print(names);
            }
            else
            {
                foreach (CAliasCommand cmd in cmds)
                {
                    PrintIndent("{0} {1}", cmd.Name, cmd.Alias);
                }
            }
        }
        
        return true;
    }
    
    public static void ListAliasesConfig(IList<string> lines)
    {
        IList<CAliasCommand> aliases = CRegistery.ListAliases();
        
        for (int i = 0; i < aliases.Count; ++i)
        {
            lines.Add(ToString(aliases[i]));
        }
    }
    
    private static string ToString(CAliasCommand cmd)
    {
        return string.Format("alias {0} {1}", cmd.Name, StringUtils.Arg(cmd.Alias));
    }
}