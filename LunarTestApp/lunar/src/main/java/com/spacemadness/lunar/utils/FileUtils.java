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

package com.spacemadness.lunar.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils
{
    public static List<String> Read(String path) throws IOException
    {
        return Read(path, DEFAULT_FILTER);
    }

    public static List<String> Read(String path, ReadFilter filter) throws IOException
    {
        return Read(path, new ArrayList<String>(), filter);
    }

    public static List<String> Read(String path, List<String> outList) throws IOException
    {
        return Read(path, outList, DEFAULT_FILTER);
    }

    public static List<String> Read(String path, List<String> outList, ReadFilter filter) throws IOException
    {
        return Read(new File(path), outList, filter);
    }

    public static List<String> Read(File file) throws IOException
    {
        return Read(file, DEFAULT_FILTER);
    }

    public static List<String> Read(File file, ReadFilter filter) throws IOException
    {
        return Read(file, new ArrayList<String>(), filter);
    }

    public static List<String> Read(File file, List<String> outList) throws IOException
    {
        return Read(file, outList, DEFAULT_FILTER);
    }

    public static List<String> Read(File file, List<String> outList, ReadFilter filter) throws IOException
    {
        if (file == null)
        {
            throw new NullPointerException("File is null");
        }

        if (!file.exists())
        {
            throw new FileNotFoundException("File doesn't exist: " + file);
        }

        if (outList == null)
        {
            throw new NullPointerException("Out list is null");
        }

        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null)
            {
                if (filter.accept(line))
                {
                    outList.add(line);
                }
            }

            return outList;
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }
    }

    public static void Write(String path, List<String> lines) throws IOException
    {
        Write(new File(path), lines);
    }

    public static void Write(File file, List<String> lines) throws IOException
    {
        if (file == null)
        {
            throw new NullPointerException("File is null");
        }

        if (lines == null)
        {
            throw new NullPointerException("Lines list is null");
        }

        PrintStream stream = null;
        try
        {
            stream = new PrintStream(file);
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

    public static boolean FileExists(String path)
    {
        if (path == null)
        {
            throw new NullPointerException("Path is null");
        }

        return new File(path).exists();
    }

    public static String getFilenameNoExt(String filename)
    {
        if (filename == null)
        {
            throw new NullPointerException("Filename is null");
        }

        int dotIndex = filename.lastIndexOf('.');
        return dotIndex != -1 ? filename.substring(0, dotIndex) : filename;
    }

    public static String getFileExt(String filename)
    {
        if (filename == null)
        {
            throw new NullPointerException("Filename is null");
        }

        int dotIndex = filename.lastIndexOf('.');
        return dotIndex != -1 ? filename.substring(dotIndex) : "";
    }

    public static String ChangeExtension(String filename, String ext)
    {
        if (filename == null)
        {
            throw new NullPointerException("File name is null");
        }

        if (ext == null)
        {
            throw new NullPointerException("Extension is null");
        }

        String oldExt = getFileExt(filename);
        if (oldExt.equals(ext))
        {
            return filename;
        }

        int index = filename.length() - oldExt.length();
        return filename.substring(0, index) + ext;
    }

    public static boolean IsPathRooted(String path)
    {
        throw new NotImplementedException();
    }

    private static final ReadFilter DEFAULT_FILTER = new ReadFilter()
    {
        @Override
        public boolean accept(String line)
        {
            return true;
        }
    };

    public interface ReadFilter
    {
        boolean accept(String line);
    }
}
