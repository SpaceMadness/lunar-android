package com.spacemadness.lunar.utils;

import java.lang.reflect.Array;
import java.util.List;

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

    public static <T> T[] toArray(List<T> list, Class<T> cls)
    {
        if (list == null)
        {
            throw new NullPointerException("List is null");
        }

        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        T[] array = (T[]) Array.newInstance(cls, list.size());
        return list.toArray(array);
    }

    public static Object clone(Object array, Class<?> componentType)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }

        if (!array.getClass().isArray())
        {
            throw new IllegalArgumentException("Unexpected class: " + array.getClass());
        }

        if (componentType == null)
        {
            throw new NullPointerException("Component type is null");
        }

        int length = Array.getLength(array);
        Object copy = Array.newInstance(componentType, length);
        System.arraycopy(array, 0, copy, 0, length);
        return copy;
    }
}
