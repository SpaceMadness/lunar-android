package com.spacemadness.lunar.utils;

import android.os.Build;

/**
 * Created by alementuev on 7/8/15.
 */
public class RuntimeUtils
{
    public static boolean isAndroidVersionAvailable(int version)
    {
        return Build.VERSION.SDK_INT >= version;
    }
}
