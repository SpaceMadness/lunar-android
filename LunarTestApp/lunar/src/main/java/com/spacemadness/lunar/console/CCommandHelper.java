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

import com.spacemadness.lunar.RuntimePlatform;
import com.spacemadness.lunar.utils.FileUtils;
import com.spacemadness.lunar.utils.StringUtils;

import java.io.File;
import java.io.IOException;

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

    public static File getConfigFile(String filename)
    {
        String path = FileUtils.ChangeExtension(filename, ".cfg");

        File configFile = new File(path);
        if (configFile.isAbsolute())
        {
            return configFile;
        }

        return new File(RuntimePlatform.getConfigsDir(), path);
    }

    public static File getConfigFile(String filename, boolean createConfigDir) throws IOException
    {
        String path = FileUtils.ChangeExtension(filename, ".cfg");

        File configFile = new File(path);
        if (configFile.isAbsolute())
        {
            return configFile;
        }

        File configsDir = RuntimePlatform.getConfigsDir(createConfigDir);
        return new File(configsDir, path);
    }
}