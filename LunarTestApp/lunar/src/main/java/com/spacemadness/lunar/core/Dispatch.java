package com.spacemadness.lunar.core;

public class Dispatch
{
    public static void dispatchOnMainThread(Runnable r)
    {
        getMainManager().Schedule(r);
    }

    public static void dispatchOnceOnMainThread(Runnable r)
    {
        getMainManager().ScheduleOnce(r);
    }

    public static void dispatch(Runnable r)
    {
        getBackgroundManager().Schedule(r);
    }

    public static void dispatchOnce(Runnable r)
    {
        getBackgroundManager().ScheduleOnce(r);
    }

    private static TimerManager getMainManager()
    {
        return TimerManager.getMainManager();
    }

    private static TimerManager.Background getBackgroundManager()
    {
        return BackgroundHolder.backgroundManager;
    }

    private static class BackgroundHolder
    {
        public static final TimerManager.Background backgroundManager = TimerManager.createInBackground("background");
    }
}
