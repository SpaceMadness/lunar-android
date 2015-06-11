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
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.utils.FileUtils;

import java.io.IOException;
import java.util.List;

@Command(Name="cat", Description="Prints the content of a config file")
public class Cmd_cat extends CCommand
{
    @CommandOption(ShortName="v")
    boolean verbose;

    boolean execute()
    {
        return execute(null);
    }

    boolean execute(String filename)
    {
        String name = filename != null ? filename : "default.cfg";
        
        String path = CCommandHelper.GetConfigPath(name);
        if (!FileUtils.FileExists(path))
        {
            PrintError("Can't find config file: '%s'", path);
            return false;
        }

        if (verbose)
        {
            Print(path);
        }

        try
        {
            List<String> lines = FileUtils.Read(path);
            for (String line : lines) {
                PrintIndent(line);
            }

            return true;
        }
        catch (IOException e)
        {
            PrintError("Can't read config: %s", e.getMessage());
            return false;
        }
    }
}