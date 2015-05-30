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

    public Timer Schedule(Action callback, float delay = 0.0f, boolean repeated = false, String name = null)
    {
        return Schedule(callback, delay, repeated ? 0 : 1, name);
    }

    public Timer Schedule(Action<Timer> callback, float delay = 0.0f, boolean repeated = false, String name = null)
    {
        return Schedule(callback, delay, repeated ? 0 : 1, name);
    }

    public Timer ScheduleOnce(Action callback, float delay = 0.0f, boolean repeated = false, String name = null)
    {
        return ScheduleOnce(callback, delay, repeated ? 0 : 1, name);
    }

    public Timer ScheduleOnce(Action<Timer> callback, float delay = 0.0f, boolean repeated = false, String name = null)
    {
        return ScheduleOnce(callback, delay, repeated ? 0 : 1, name);
    }

    public Timer ScheduleOnce(Action callback, float delay, int numRepeats, String name = null)
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

    public Timer ScheduleOnce(Action<Timer> callback, float delay, int numRepeats, String name = null)
    {
        lock (this)
        {
            Timer timer = FindTimer(callback);
            if (timer != null)
            {
                return timer;
            }

            return Schedule(callback, delay, numRepeats, name);
        }
    }

    public abstract Timer Schedule(Action callback, float delay, int numRepeats, String name = null);
    public abstract Timer Schedule(Action<Timer> callback, float delay, int numRepeats, String name = null);

    public abstract void Cancel(Action callback);
    public abstract void Cancel(Action<Timer> callback);

    public abstract void Cancel(String name);
    public abstract void CancelAll();
    public abstract void CancelAll(Object target);

    protected abstract Timer FindTimer(Action callback);
    protected abstract Timer FindTimer(Action<Timer> callback);

    public abstract int Count();
}