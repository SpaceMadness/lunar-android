package com.spacemadness.lunar.utils;

public class ObjectUtils
{
    public static boolean areEqual(Object o1, Object o2)
    {
        return o1 != null && o1.equals(o2) || o1 == null && o2 == null;
    }

    public static <T> T notNullOrDefault(T obj, T defaultObj)
    {
        if (defaultObj == null)
        {
            throw new NullPointerException("Default object is null");
        }

        return obj != null ? obj : defaultObj;
    }
}
