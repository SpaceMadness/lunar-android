package com.spacemadness.lunar.utils;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weee on 6/9/15.
 */
public class ArrayUtilsTest extends TestCase
{
    public void testIndexOf() throws Exception
    {
        String[] array = { "1", null, "1", "3" };

        assertEquals(0, ArrayUtils.IndexOf(array, "1"));
        assertEquals(1, ArrayUtils.IndexOf(array, null));
        assertEquals(3, ArrayUtils.IndexOf(array, "3"));
    }

    public void testIndexOfInt() throws Exception
    {
        int[] array = { 1, 2, 3 };

        assertEquals(0, ArrayUtils.IndexOf(array, 1));
        assertEquals(1, ArrayUtils.IndexOf(array, 2));
        assertEquals(2, ArrayUtils.IndexOf(array, 3));
    }

    public void testToArray() throws Exception
    {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");

        String[] expected = { "1", "2", "3" };
        String[] actual = ArrayUtils.toArray(list, String.class);

        assertTrue(Arrays.equals(expected, actual));
    }
}