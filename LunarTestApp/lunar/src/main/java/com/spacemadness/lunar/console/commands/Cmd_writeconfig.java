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
import com.spacemadness.lunar.console.CCommandHelper;
import com.spacemadness.lunar.console.CFlags;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.CVarCommand;
import com.spacemadness.lunar.console.ListCommandsFilter;
import com.spacemadness.lunar.console.annotations.Arg;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.utils.FileUtils;
import com.spacemadness.lunar.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Command(Name="writeconfig", Description="Writes a config file.")
public class Cmd_writeconfig extends CCommand
{
    boolean execute(@Arg("filename") String filename)
    {
        try
        {
            File configFile = CCommandHelper.getConfigFile(filename, true);

            List<String> lines = new ArrayList<String>();

            // cvars
            ListCvars(lines);

            // aliases
            ListAliases(lines);

            FileUtils.Write(configFile, lines);
            return true;
        }
        catch (IOException e)
        {
            if (this.IsManualMode)
            {
                PrintError("Can't write config: %s", e.getMessage());
            }
            return false;
        }
    }
    
    private static void ListCvars(List<String> lines)
    {
        List<CVar> cvars = CRegistery.ListVars(new ListCommandsFilter<CVarCommand>()
        {
            @Override
            public boolean accept(CVarCommand command) {
                return !command.IsDefault() && !command.HasFlag(CFlags.NoArchive);
            }
        });
        
        if (cvars.size() > 0)
        {
            lines.add("// cvars");
        }

        for (CVar var : cvars)
        {
            if (var.Value() != null)
            {
                lines.add(String.format("%s %s", var.Name(), StringUtils.Arg(var.Value())));
            }
            else
            {
                lines.add("null " + var.Name());
            }
        }
    }
    
    private static void ListAliases(List<String> lines)
    {
        lines.add("");
        lines.add("// aliases");
        Cmd_alias.ListAliasesConfig(lines);
    }
}
