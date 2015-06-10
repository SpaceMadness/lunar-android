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

import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.annotations.Command;

@Command("unalias", Description="Remove an alias name for command(s)")
public class Cmd_unalias extends CCommand
{
    void Execute(String name)
    {
        if (CRegistery.RemoveAlias(name))
        {
            
            PostNotification(
                CCommandNotifications.CAliasesChanged, 
                CCommandNotifications.KeyName, name,
                CCommandNotifications.KeyManualMode, this.IsManualMode
            );
        }
    }
}
