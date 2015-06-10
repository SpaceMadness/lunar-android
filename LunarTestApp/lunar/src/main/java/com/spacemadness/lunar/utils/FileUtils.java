package com.spacemadness.lunar.utils;//  Copyright 2015 SpaceMadness.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weee on 6/10/15.
 */
public class FileUtils
{
    public static boolean FileExists(String path)
    {
        if (path == null)
        {
            throw new NullPointerException("Path is null");
        }

        return new File(path).exists();
    }

    public static List<String> Read(String path) throws FileNotFoundException, IOException
    {
        return Read(path, new ArrayList<String>());
    }

    public static List<String> Read(String path, List<String> outList) throws FileNotFoundException, IOException
    {
        if (path == null)
        {
            throw new NullPointerException("Path is null");
        }

        if (outList == null)
        {
            throw new NullPointerException("Out list is null");
        }

        File file = new File(path);

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null)
            {
                outList.add(line);
            }
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }

        return null;
    }

    public static void Write(String path, List<String> lines) throws IOException
    {
        if (path == null)
        {
            throw new NullPointerException("Path is null");
        }

        if (lines == null)
        {
            throw new NullPointerException("Lines list is null");
        }

        PrintStream stream = null;
        try
        {
            stream = new PrintStream(new File(path));
            for (String line : lines)
            {
                stream.println(line);
            }
        }
        finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }
    }
}
