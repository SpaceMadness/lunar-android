package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
class NullTimerManager extends ITimerManager
{
    @Override
    public Timer Schedule(Runnable callback, float delay, int numRepeats, String name)
    {
        throw new InvalidOperationException("Can't schedule timer on a 'null' timer manager");
    }

    @Override
    public Timer Schedule(TimerRunnable callback, float delay, int numRepeats, String name)
    {
        throw new InvalidOperationException("Can't schedule timer on a 'null' timer manager");
    }

    @Override
    public void Cancel(Runnable callback)
    {   
    }

    @Override
    public void Cancel(TimerRunnable callback)
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
    protected Timer FindTimer(Runnable callback)
    {
        return null;
    }

    @Override
    protected Timer FindTimer(TimerRunnable callback)
    {
        return null;
    }

    @Override
    public int Count()
    {
        return 0;
    }
}