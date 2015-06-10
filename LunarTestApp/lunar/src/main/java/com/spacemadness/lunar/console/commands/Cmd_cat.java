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
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;

@Command("cat", Description="Prints the content of a config file")
public class Cmd_cat extends CCommand
{
    @CommandOption(ShortName="v")
    bool verbose;

    bool Execute(string filename = null)
    {
        string name = filename != null ? filename : "default.cfg";
        
        string path = CCommandHelper.GetConfigPath(name);
        if (!FileUtils.FileExists(path))
        {
            PrintError("Can't find config file: '{0}'", path);
            return false;
        }

        if (verbose)
        {
            Print(path);
        }

        IList<string> lines = FileUtils.Read(path);
        foreach (string line in lines)
        {
            PrintIndent(line);
        }
        
        return true;
    }
}