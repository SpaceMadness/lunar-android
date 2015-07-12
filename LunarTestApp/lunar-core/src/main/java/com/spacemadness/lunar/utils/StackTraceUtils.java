package com.spacemadness.lunar.utils;

public class StackTraceUtils
{
    public static String getStackTrace(Exception e)
    {
        if (e == null)
        {
            throw new NullPointerException("Exception is null");
        }

        StackTraceElement[] stackTraceElements = e.getStackTrace();
        return StringUtils.Join(stackTraceElements, "\n");
    }
}
