package com.spacemadness.lunar.core;

public abstract class TimerManager implements IDestroyable
{
    private TimerListener listener;

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

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public TimerListener getListener()
    {
        return listener;
    }

    public void setListener(TimerListener listener)
    {
        this.listener = listener;
    }

    //////////////////////////////////////////////////////////////////////////////
    // TimerListener

    public interface TimerListener
    {
        void onTimerScheduled(TimerManager manager, Timer timer);

        void onTimerFired(TimerManager manager, Timer timer);

        void onTimerCancelled(TimerManager manager, Timer timer);

        void onTimerFinished(TimerManager manager, Timer timer);

        void onTimerRemoved(TimerManager manager, Timer timer);

        void onTimerSuspended(TimerManager manager, Timer timer);

        void onTimerResumed(TimerManager manager, Timer timer);
    }
}
