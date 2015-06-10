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

import com.spacemadness.lunar.utils.FileUtils;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

public class CCommandHelper
{
    public static String CreateCommandLine(String[] args)
    {
        return CreateCommandLine(args, 0);
    }

    public static String CreateCommandLine(String[] args, int startIndex)
    {
        StringBuilder buffer = new StringBuilder();
        for (int i = startIndex; i < args.length; ++i)
        {
            buffer.append(StringUtils.Arg(args[i]));
            if (i < args.length - 1)
            {
                buffer.append(' ');
            }
        }

        return buffer.toString();
    }

    public static String GetConfigPath(String filename)
    {
        String path = FileUtils.ChangeExtension(filename, ".cfg");
        if (FileUtils.IsPathRooted(path))
        {
            return path;
        }

        // return System.IO.Path.Combine(ConfigPath, path);
        throw new NotImplementedException();
    }

    public static String ConfigPath()
    {
        // return System.IO.Path.Combine(FileUtils.DataPath, "configs");
        throw new NotImplementedException(); // FIXME
    }
}