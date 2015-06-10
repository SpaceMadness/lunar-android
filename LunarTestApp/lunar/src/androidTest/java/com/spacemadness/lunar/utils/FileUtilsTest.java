package com.spacemadness.lunar.utils;

import junit.framework.TestCase;
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

/**
 * Created by weee on 6/10/15.
 */
public class FileUtilsTest extends TestCase
{
    public void testReadWrite()
    {

    }

    public void testGetFilenameNoExt()
    {
        assertEquals("filename", FileUtils.getFilenameNoExt("filename.ext"));
        assertEquals("filename.more", FileUtils.getFilenameNoExt("filename.more.ext"));
        assertEquals("filename", FileUtils.getFilenameNoExt("filename."));
        assertEquals("filename", FileUtils.getFilenameNoExt("filename"));
    }

    public void testGetFileExt()
    {
        assertEquals(".ext", FileUtils.getFileExt("filename.ext"));
        assertEquals(".ext", FileUtils.getFileExt("filename.more.ext"));
        assertEquals(".", FileUtils.getFileExt("filename."));
        assertEquals("", FileUtils.getFileExt("filename"));
    }
}