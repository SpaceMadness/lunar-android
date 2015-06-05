package com.spacemadness.lunar.utils;

/**
 * Created by alementuev on 6/1/15.
 */
public class ObjectUtils
{
    public static <T> boolean areEqual(T o1, T o2)
    {
        return o1 != null && o1.equals(o2) || o1 == null && o2 == null;
    }
}
