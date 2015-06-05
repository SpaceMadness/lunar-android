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
            Object obj = array[i];
            if (obj != null)
            {
                if (obj.equals(element))
                {
                    return i;
                }
            }
            else if (element == null)
            {
                return i;
            }
        }

        return -1;
    }
}
