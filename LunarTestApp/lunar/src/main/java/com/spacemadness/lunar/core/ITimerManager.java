package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
abstract class ITimerManager implements IUpdatable, IDestroyable // FIXME: remove to AbstractTimerManager
{
    @Override
    public void Update(float delta)
    {
    }

    @Override
    public void Destroy()
    {
    }

    public Timer Schedule(Runnable callback)
    {
        return Schedule(callback, 0.0f);
    }

    public Timer Schedule(Runnable callback, float delay)
    {
        return Schedule(callback, delay, false);
    }

    public Timer Schedule(Runnable callback, float delay, boolean repeated)
    {
        return Schedule(callback, delay, repeated, null);
    }

    public Timer Schedule(Runnable callback, float delay, boolean repeated, String name)
    {
        return Schedule(callback, delay, repeated ? 0 : 1, name);
    }

    public Timer Schedule(TimerRunnable callback)
    {
        return Schedule(callback, 0.0f);
    }

    public Timer Schedule(TimerRunnable callback, float delay)
    {
        return Schedule(callback, delay, false);
    }

    public Timer Schedule(TimerRunnable callback, float delay, boolean repeated)
    {
        return Schedule(callback, delay, repeated, null);
    }

    public Timer Schedule(TimerRunnable callback, float delay, boolean repeated, String name)
    {
        return Schedule(callback, delay, repeated ? 0 : 1, name);
    }

    public Timer ScheduleOnce(Runnable callback)
    {
        return ScheduleOnce(callback, 0.0f);
    }

    public Timer ScheduleOnce(Runnable callback, float delay)
    {
        return ScheduleOnce(callback, delay, false);
    }

    public Timer ScheduleOnce(Runnable callback, float delay, boolean repeated)
    {
        return ScheduleOnce(callback, delay, repeated, null);
    }

    public Timer ScheduleOnce(Runnable callback, float delay, boolean repeated, String name)
    {
        return ScheduleOnce(callback, delay, repeated ? 0 : 1, name);
    }

    public Timer ScheduleOnce(TimerRunnable callback)
    {
        return ScheduleOnce(callback, 0.0f);
    }

    public Timer ScheduleOnce(TimerRunnable callback, float delay)
    {
        return ScheduleOnce(callback, delay, false);
    }

    public Timer ScheduleOnce(TimerRunnable callback, float delay, boolean repeated)
    {
        return ScheduleOnce(callback, delay, repeated, null);
    }

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