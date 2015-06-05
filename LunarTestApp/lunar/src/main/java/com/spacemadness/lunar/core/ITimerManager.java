package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
abstract class ITimerManager implements IUpdatable, IDestroyable // not a good idea to name abstract class as interface, but what you gonna do
{
    public void Update(float delta)
    {
    }

    public void Destroy()
    {
    }

    // FIXME: public Timer Schedule(Runnable callback, float delay = 0.0f, boolean repeated = false, String name = null)
    public Timer Schedule(Runnable callback, float delay, boolean repeated, String name)
    {
        return Schedule(callback, delay, repeated ? 0 : 1, name);
    }

    // FIXME: public Timer Schedule(TimerRunnable callback, float delay = 0.0f, boolean repeated = false, String name = null)
    public Timer Schedule(TimerRunnable callback, float delay, boolean repeated, String name)
    {
        return Schedule(callback, delay, repeated ? 0 : 1, name);
    }

    // FIXME: public Timer ScheduleOnce(Runnable callback, float delay = 0.0f, boolean repeated = false, String name = null)
    public Timer ScheduleOnce(Runnable callback, float delay, boolean repeated, String name)
    {
        return ScheduleOnce(callback, delay, repeated ? 0 : 1, name);
    }

    // FIXME: public Timer ScheduleOnce(TimerRunnable callback, float delay = 0.0f, boolean repeated = false, String name = null)
    public Timer ScheduleOnce(TimerRunnable callback, float delay, boolean repeated, String name)
    {
        return ScheduleOnce(callback, delay, repeated ? 0 : 1, name);
    }

    public Timer ScheduleOnce(Runnable callback, float delay, int numRepeats)
    {
        return ScheduleOnce(callback, delay, numRepeats, null);
    }

    public Timer ScheduleOnce(Runnable callback, float delay, int numRepeats, String name)
    {
        synchronized (this)
        {
            Timer timer = FindTimer(callback);
            if (timer != null)
            {
                return timer;
            }

            return Schedule(callback, delay, numRepeats, name);
        }
    }

    public Timer ScheduleOnce(TimerRunnable callback, float delay, int numRepeats)
    {
        return ScheduleOnce(callback, delay, numRepeats, null);
    }

    public Timer ScheduleOnce(TimerRunnable callback, float delay, int numRepeats, String name)
    {
        synchronized (this)
        {
            Timer timer = FindTimer(callback);
            if (timer != null)
            {
                return timer;
            }

            return Schedule(callback, delay, numRepeats, name);
        }
    }

    public abstract Timer Schedule(Runnable callback, float delay, int numRepeats, String name);
    public abstract Timer Schedule(TimerRunnable callback, float delay, int numRepeats, String name);

    public abstract void Cancel(Runnable callback);
    public abstract void Cancel(TimerRunnable callback);

    public abstract void Cancel(String name);
    public abstract void CancelAll();

    protected abstract Timer FindTimer(Runnable callback);
    protected abstract Timer FindTimer(TimerRunnable callback);

    public abstract int Count();
}