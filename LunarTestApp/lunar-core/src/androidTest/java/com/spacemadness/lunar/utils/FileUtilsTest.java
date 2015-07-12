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

import android.test.AndroidTestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtilsTest extends AndroidTestCase
{
    private File tempFile;

    public void testGetFilenameNoExt() throws Exception
    {
        assertEquals("filename", FileUtils.getFilenameNoExt("filename.ext"));
        assertEquals("filename.more", FileUtils.getFilenameNoExt("filename.more.ext"));
        assertEquals("filename", FileUtils.getFilenameNoExt("filename."));
        assertEquals("filename", FileUtils.getFilenameNoExt("filename"));
    }

    public void testGetFileExt() throws Exception
    {
        assertEquals(".ext", FileUtils.getFileExt("filename.ext"));
        assertEquals(".ext", FileUtils.getFileExt("filename.more.ext"));
        assertEquals(".", FileUtils.getFileExt("filename."));
        assertEquals("", FileUtils.getFileExt("filename"));
    }

    public void testChangeExtension() throws Exception
    {
        String filename = "file.ext";
        String expected = "file.newExt";
        String actual = FileUtils.ChangeExtension(filename, ".newExt");

        assertEquals(expected, actual);
    }

    public void testChangeFromNoExtension() throws Exception
    {
        String filename = "file";
        String expected = "file.ext";
        String actual = FileUtils.ChangeExtension(filename, ".ext");

        assertEquals(expected, actual);
    }

    public void testChangeToNoExtension() throws Exception
    {
        String filename = "file.ext";
        String expected = "file";
        String actual = FileUtils.ChangeExtension(filename, "");

        assertEquals(expected, actual);
    }

    public void testChangeSameExtension() throws Exception
    {
        String filename = "file.ext";
        String expected = "file.ext";
        String actual = FileUtils.ChangeExtension(filename, ".ext");

        assertSame(expected, actual);
    }

    public void testReadWrite() throws Exception
    {
        List<String> expected = new ArrayList<>();
        expected.add("Some text line");
        expected.add("一些文本行");
        expected.add("Строка с текстом");

        FileUtils.Write(getTempPath(), expected);

        List<String> actual = FileUtils.Read(getTempPath());
        assertEquals(expected, actual);
    }

    public void testReadWriteFiltered() throws Exception
    {
        List<String> lines = ArrayUtils.toList(
                "Text line 1",
                "# Text line 2",
                "Text line 3",
                "# Text line 4",
                "Text line 5"
        );

        FileUtils.Write(getTempPath(), lines);

        List<String> expected = ArrayUtils.toList(
                "Text line 1",
                "Text line 3",
                "Text line 5"
        );
        List<String> actual = FileUtils.Read(getTempPath(), new FileUtils.ReadFilter()
        {
            @Override
            public boolean accept(String line)
            {
                return !line.startsWith("#");
            }
        });

        assertEquals(expected, actual);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        tempFile = new File(getContext().getCacheDir(), "lines.txt");
        tempFile.delete();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        tempFile.delete();
    }

    public String getTempPath()
    {
        return tempFile.getPath();
    }
}