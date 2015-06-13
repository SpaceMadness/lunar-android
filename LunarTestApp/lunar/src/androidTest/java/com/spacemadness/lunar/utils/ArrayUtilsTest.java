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

    public void testToArrayEmpty() throws Exception
    {
        List<String> list = new ArrayList<String>();
        String[] expected = { };
        String[] actual = ArrayUtils.toArray(list, String.class);

        assertTrue(Arrays.equals(expected, actual));
    }

    public void testToList() throws Exception
    {
        List<String> expected = new ArrayList<>();
        expected.add("1");
        expected.add("2");
        expected.add("3");

        List<String> actual = ArrayUtils.toList("1", "2", "3");
        assertEquals(expected, actual);
    }

    public void testToListEmpty() throws Exception
    {
        List<String> expected = new ArrayList<>();
        List<String> actual = ArrayUtils.toList(new String[0]);
        assertEquals(expected, actual);
    }

    public void testClone()
    {
        int[] intArrayExpected = { 1, 2, 3 };
        int[] intArrayActual = (int[]) ArrayUtils.clone(intArrayExpected, int.class);
        assertTrue(Arrays.equals(intArrayExpected, intArrayActual));

        String[] stringArrayExpected = { "1", "2", "3" };
        String[] stringArrayActual = (String[]) ArrayUtils.clone(stringArrayExpected, String.class);
        assertTrue(Arrays.equals(stringArrayExpected, stringArrayActual));

        String[] stringArrayExpectedEmpty = {};
        String[] stringArrayActualEmpty = (String[]) ArrayUtils.clone(stringArrayExpectedEmpty, String.class);
        assertTrue(Arrays.equals(stringArrayExpectedEmpty, stringArrayActualEmpty));
    }
}