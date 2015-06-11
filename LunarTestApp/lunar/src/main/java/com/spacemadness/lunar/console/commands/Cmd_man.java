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
import com.spacemadness.lunar.console.CVarCommand;
import com.spacemadness.lunar.console.ListCommandsFilter;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.List;

@Command(Name="man", Description="Prints command usage")
public class Cmd_man extends CCommand
{
    boolean execute(String command)
    {
        CCommand cmd = CRegistery.FindCommand(command);
        if (cmd == null)
        {
            PrintError("%s: command not found \"%s\"", this.Name, command);
            return false;
        }
        
        cmd.Delegate(this.Delegate());
        cmd.PrintUsage(true);
        cmd.Delegate(null);
        
        return true;
    }

    @Override
    protected String[] AutoCompleteArgs(String commandLine, final String token)
    {
        // TODO: add unit tests

        List<CCommand> commands = CRegistery.ListCommands(new ListCommandsFilter<CCommand>()
        {
            @Override
            public boolean accept(CCommand command)
            {
                return !(command instanceof CVarCommand) &&
                        !(command instanceof CAliasCommand) &&
                        CRegistery.ShouldListCommand(command, token);
            }
        });

        if (commands.size() == 0)
        {
            return null;
        }
        
        String[] values = new String[commands.size()];
        for (int i = 0; i < commands.size(); ++i)
        {
            final CCommand command = commands.get(i);
            values[i] = StringUtils.C(command.Name, ((CCommand) command).ColorCode());
        }
        return values;
    }
}
