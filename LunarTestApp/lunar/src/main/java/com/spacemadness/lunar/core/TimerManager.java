package com.spacemadness.lunar.core;

public abstract class TimerManager implements IDestroyable
{
    public Timer ScheduleOnce(Runnable target)
    {
        return ScheduleOnce(target, 0L);
    }

    public abstract Timer ScheduleOnce(Runnable target, long delayMillis);

    public Timer Schedule(Runnable target)
    {
        return Schedule(target, 0L);
    }

    public abstract Timer Schedule(Runnable target, long delayMillis);

    public abstract Timer Cancel(Runnable target);

    public abstract void cancelAll();
}
