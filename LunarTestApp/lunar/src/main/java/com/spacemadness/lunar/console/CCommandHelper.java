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

package com.spacemadness.lunar.console;

public class CCommandHelper
{
    public static String CreateCommandLine(String[] args, int startIndex = 0)
    {
        StringBuilder buffer = StringBuilderPool.NextAutoRecycleBuilder();
        for (int i = startIndex; i < args.Length; ++i)
        {
            buffer.Append(StringUtils.Arg(args[i]));
            if (i < args.Length - 1)
            {
                buffer.Append(' ');
            }
        }

        return buffer.ToString();
    }

    public static String GetConfigPath(String filename)
    {
        String path = FileUtils.ChangeExtension(filename, ".cfg");
        if (FileUtils.IsPathRooted(path))
        {
            return path;
        }

        return System.IO.Path.Combine(ConfigPath, path);
    }

    public static String ConfigPath
    {
        get { return System.IO.Path.Combine(FileUtils.DataPath, "configs"); }
    }
}