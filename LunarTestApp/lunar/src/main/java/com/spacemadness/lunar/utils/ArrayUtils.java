package com.spacemadness.lunar.utils;

/**
 * Created by alementuev on 6/1/15.
 */
public class ArrayUtils
{
    public static <T> int IndexOf(T[] array, T element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (ObjectUtils.areEqual(element, array[i]))
            {
                return i;
            }
        }

        return -1;
    }

    public static int IndexOf(boolean[] array, boolean element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (element == array[i])
            {
                return i;
            }
        }

        return -1;
    }

    public static int IndexOf(byte[] array, byte element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (element == array[i])
            {
                return i;
            }
        }

        return -1;
    }

    public static int IndexOf(short[] array, short element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (element == array[i])
            {
                return i;
            }
        }

        return -1;
    }

    public static int IndexOf(char[] array, char element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (element == array[i])
            {
                return i;
            }
        }

        return -1;
    }

    public static int IndexOf(int[] array, int element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (element == array[i])
            {
                return i;
            }
        }

        return -1;
    }

    public static int IndexOf(long[] array, long element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (element == array[i])
            {
                return i;
            }
        }

        return -1;
    }

    public static int IndexOf(float[] array, float element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (element == array[i])
            {
                return i;
            }
        }

        return -1;
    }

    public static int IndexOf(double[] array, double element)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        for (int i = 0; i < array.length; ++i)
        {
            if (element == array[i])
            {
                return i;
            }
        }

        return -1;
    }
}
