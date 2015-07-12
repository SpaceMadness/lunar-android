package com.spacemadness.lunar.debug;

public class Log
{
    private static final String TAG = "Lunar";

    public static void d(String format, Object... params) { d(TAG, format, params); }

    public static void v(String format, Object... params)
    {
        v(TAG, format, params);
    }

    public static void v(String tag, String format, Object... params)
    {
        String message = String.format(format, params);
        android.util.Log.v(tag, message);
    }

    public static void d(String tag, String format, Object... params)
    {
        String message = String.format(format, params);
        android.util.Log.d(tag, message);
    }

    public static void e(String format, Object... params)
    {
        String message = String.format(format, params);
        android.util.Log.e(TAG, message);
    }

    public static void c(String format, Object... params)
    {
        String message = String.format(format, params);
        android.util.Log.e(TAG, message);
    }

    public static void logException(Exception e)
    {
        logException(e, "");
    }

    public static void logException(Exception e, String format, Object... params)
    {
        String message = String.format(format, params);
        android.util.Log.e(TAG, message, e);
    }
}
