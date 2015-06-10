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
import com.spacemadness.lunar.utils.StringUtils;

@Command("alias", Description="Creates an alias name for command(s)")
public class Cmd_alias extends CCommand
{
    void Execute(String name, String commands)
    {
        CRegistery.AddAlias(name, StringUtils.UnArg(commands));
        
        PostNotification(
            CCommandNotifications.CAliasesChanged, 
            CCommandNotifications.KeyName, name,
            CCommandNotifications.KeyManualMode, this.IsManualMode
        );
    }
    
    public static void ListAliasesConfig(List<String> lines)
    {
        List<CAliasCommand> aliases = CRegistery.ListAliases();
        
        for (int i = 0; i < aliases.Count; ++i)
        {
            lines.Add(ToString(aliases[i]));
        }
    }
    
    private static String ToString(CAliasCommand cmd)
    {
        return String.Format("alias {0} {1}", cmd.Name, StringUtils.Arg(cmd.Alias));
    }
}
