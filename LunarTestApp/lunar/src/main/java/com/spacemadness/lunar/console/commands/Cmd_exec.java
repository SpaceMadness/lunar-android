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

@Command("exec", Description="Executes a config file.")
public class Cmd_exec extends CCommand
{
    bool Execute(string filename)
    {
        string path = CCommandHelper.GetConfigPath(filename);
        if (!FileUtils.FileExists(path))
        {
            if (this.IsManualMode)
            {
                PrintError("Can't exec config: file not found");
            }
            return false;
        }
        
        IList<string> lines = FileUtils.Read(path);
        if (lines == null)
        {
            if (this.IsManualMode)
            {
                PrintError("Can't exec config: error reading file");
            }
            return false;
        }
        
        foreach (string line in lines)
        {
            string trim = line.Trim();
            if (trim.Length == 0 || trim.StartsWith("//"))
            {
                continue;
            }
            
            ExecCommand(line);
        }
        
        PostNotification(CCommandNotifications.ConfigLoaded);
        return true;
    }
}