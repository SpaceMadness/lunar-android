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
import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.utils.StringUtils;

@Command("writeconfig", Description="Writes a config file.")
public class Cmd_writeconfig extends CCommand
{
    void Execute(String filename)
    {
        List<String> lines = ReusableLists.NextAutoRecycleList<String>();
        
        // cvars
        ListCvars(lines);
        
        // bindings
        ListBindings(lines);
        
        // aliases
        ListAliases(lines);
        
        String path = CCommandHelper.GetConfigPath(filename);
        FileUtils.Write(path, lines);
    }
    
    private static void ListCvars(List<String> lines)
    {
        List<CVar> cvars = CRegistery.ListVars(delegate(CVarCommand cmd)
        {
            return !cmd.IsDefault && !cmd.HasFlag(CFlags.NoArchive);
        });
        
        if (cvars.Count > 0)
        {
            lines.Add("// cvars");
        }
        
        for (int i = 0; i < cvars.Count; ++i)
        {
            CVar c = cvars[i];
            if (c.Value != null)
            {
                lines.Add(String.Format("{0} {1}", c.Name, StringUtils.Arg(c.Value)));
            }
            else
            {
                lines.Add("null " + c.Name);
            }
        }
    }
    
    private static void ListBindings(List<String> lines)
    {
        List<CBinding> bindings = CBindings.List();
        if (bindings.Count > 0)
        {
            lines.Add("// key bindings");
        }
        
        for (int i = 0; i < bindings.Count; ++i)
        {
            lines.Add(String.Format("bind {0} {1}", bindings[i].shortCut.ToString(), StringUtils.Arg(bindings[i].cmdKeyDown)));
        }
    }
    
    private static void ListAliases(List<String> lines)
    {
        lines.Add("");
        lines.Add("// aliases");
        Cmd_alias.ListAliasesConfig(lines);
    }
}
