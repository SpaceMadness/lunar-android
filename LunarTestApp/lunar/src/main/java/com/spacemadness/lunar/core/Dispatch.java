package com.spacemadness.lunar.core;

import static com.spacemadness.lunar.AppTerminal.*;

public class Dispatch
{
    public static void dispatchOnMainThread(Runnable r)
    {
        getTimerManager().Schedule(r);
    }

    public static void dispatchOnceOnMainThread(Runnable r)
    {
        getTimerManager().ScheduleOnce(r);
    }

    public static void dispatch(Runnable r)
    {
        getBackgroundTimerManager().Schedule(r);
    }

    public static void dispatchOnce(Runnable r)
    {
        getBackgroundTimerManager().ScheduleOnce(r);
    }
}
