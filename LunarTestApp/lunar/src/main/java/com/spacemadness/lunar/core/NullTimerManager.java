package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
class NullTimerManager extends ITimerManager
{
    @Override
    public Timer Schedule(Action callback, float delay, int numRepeats, String name = null)
    {
        throw new InvalidOperationException("Can't schedule timer on a 'null' timer manager");
    }

    @Override
    public Timer Schedule(Action<Timer> callback, float delay, int numRepeats, String name = null)
    {
        throw new InvalidOperationException("Can't schedule timer on a 'null' timer manager");
    }

    @Override
    public void Cancel(Action callback)
    {   
    }

    @Override
    public void Cancel(Action<Timer> callback)
    {
    }

    @Override
    public void Cancel(String name)
    {
    }

    @Override
    public void CancelAll()
    {
    }

    @Override
    public void CancelAll(Object target)
    {
    }

    @Override
    protected Timer FindTimer(Action callback)
    {
        return null;
    }

    @Override
    protected Timer FindTimer(Action<Timer> callback)
    {
        return null;
    }

    @Override
    public int Count()
    {
        return 0;
    }
}