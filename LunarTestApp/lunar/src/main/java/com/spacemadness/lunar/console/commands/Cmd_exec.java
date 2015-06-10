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
import com.spacemadness.lunar.console.CCommandNotifications;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.utils.FileUtils;

import java.io.IOException;
import java.util.List;

@Command(Name="exec", Description="Executes a config file.")
public class Cmd_exec extends CCommand
{
    boolean Execute(String filename)
    {
        String path = CCommandHelper.GetConfigPath(filename);
        if (!FileUtils.FileExists(path))
        {
            if (this.IsManualMode)
            {
                PrintError("Can't exec config: file not found");
            }
            return false;
        }
        
        List<String> lines = null;
        try
        {
            lines = FileUtils.Read(path);
        }
        catch (IOException e)
        {
            if (this.IsManualMode)
            {
                PrintError("Can't exec config: error reading file");
            }
            return false;
        }
        
        for (String line : lines)
        {
            String trim = line.trim();
            if (trim.length() == 0 || trim.startsWith("//"))
            {
                continue;
            }
            
            ExecCommand(line);
        }
        
        PostNotification(CCommandNotifications.ConfigLoaded);
        return true;
    }
}