package com.spacemadness.lunar.core;

import android.os.Looper;

/**
 * Created by weee on 5/28/15.
 */
public class ThreadUtils
{
    private static final Object mutex = new Object();

    private static boolean waitFlag;

    public static void Wait()
    {
        synchronized (mutex)
        {
            waitFlag = true;
            while (waitFlag)
            {
                try
                {
                    mutex.wait();
                }
                catch (InterruptedException e)
                {
                }
            }
        }
    }

    public static void Notify()
    {
        synchronized (mutex)
        {
            waitFlag = false;
            mutex.notifyAll();
        }
    }

    public static boolean IsUnityThread() // FIXME: rename
    {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
