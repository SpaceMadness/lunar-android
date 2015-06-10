package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.NotImplementedException;

/**
 * Created by weee on 5/28/15.
 */
public class TimerManager extends ITimerManager
{
    public static final ITimerManager Null = new NullTimerManager();

    private static TimerManager s_sharedInstance; // FIXME: rename

    double currentTime;

    private Timer rootTimer;

    private Timer delayedAddHeadTimer; // timers which were scheduled while iterating the list
    private Timer delayedAddTailTimer; // track tail to append at the end of the list so timers are
                                       // fired in the same order as they scheduled

    private Timer delayedFreeRootTimer; // timers which were cancelled while iterating the list

    private int timersCount;
    private boolean updating;

    //////////////////////////////////////////////////////////////////////////////
    // Shared instance

    static
    {
        s_sharedInstance = new TimerManager();
    }

    public static Timer ScheduleTimer(Runnable callback)
    {
        return ScheduleTimer(callback, 0.0f);
    }

    public static Timer ScheduleTimer(Runnable callback, float delay)
    {
        return ScheduleTimer(callback, delay, false);
    }

    public static Timer ScheduleTimer(Runnable callback, float delay, boolean repeated)
    {
        return ScheduleTimer(callback, delay, repeated, null);
    }

    public static Timer ScheduleTimer(Runnable callback, float delay, boolean repeated, String name)
    {
        return s_sharedInstance.Schedule(callback, delay, repeated, name);
    }

    public static Timer ScheduleTimer(TimerRunnable callback)
    {
        return ScheduleTimer(callback, 0.0f);
    }

    public static Timer ScheduleTimer(TimerRunnable callback, float delay)
    {
        return ScheduleTimer(callback, delay, false);
    }

    public static Timer ScheduleTimer(TimerRunnable callback, float delay, boolean repeated)
    {
        return ScheduleTimer(callback, delay, repeated, null);
    }

    public static Timer ScheduleTimer(TimerRunnable callback, float delay, boolean repeated, String name)
    {
        return s_sharedInstance.Schedule(callback, delay, repeated, name);
    }

    public static Timer ScheduleTimerOnce(Runnable callback)
    {
        return ScheduleTimerOnce(callback, 0.0f);
    }

    public static Timer ScheduleTimerOnce(Runnable callback, float delay)
    {
        return ScheduleTimerOnce(callback, delay, false);
    }

    public static Timer ScheduleTimerOnce(Runnable callback, float delay, boolean repeated)
    {
        return ScheduleTimerOnce(callback, delay, repeated, null);
    }

    public static Timer ScheduleTimerOnce(Runnable callback, float delay, boolean repeated, String name)
    {
        return s_sharedInstance.ScheduleOnce(callback, delay, repeated, name);
    }

    public static Timer ScheduleTimerOnce(TimerRunnable callback)
    {
        return ScheduleTimerOnce(callback, 0.0f);
    }

    public static Timer ScheduleTimerOnce(TimerRunnable callback, float delay)
    {
        return ScheduleTimerOnce(callback, delay, false);
    }

    public static Timer ScheduleTimerOnce(TimerRunnable callback, float delay, boolean repeated)
    {
        return ScheduleTimerOnce(callback, delay, repeated, null);
    }

    public static Timer ScheduleTimerOnce(TimerRunnable callback, float delay, boolean repeated, String name)
    {
        return s_sharedInstance.ScheduleOnce(callback, delay, repeated, name);
    }

    public static void CancelTimer(Runnable callback)
    {
        s_sharedInstance.Cancel(callback);
    }

    public static void CancelTimer(TimerRunnable callback)
    {
        s_sharedInstance.Cancel(callback);
    }

    public static void CancelTimers(Object target)
    {
        // s_sharedInstance.CancelAll(target);
        throw new NotImplementedException();
    }

    static TimerManager SharedInstance()
    {
        return s_sharedInstance;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Updatable

    @Override
    public void Update(float delta)
    {
        synchronized (this)
        {
            currentTime += delta;

            if (timersCount > 0)
            {
                updating = true;
                for (Timer t = rootTimer; t != null;)
                {
                    if (t.fireTime > currentTime)
                    {
                        break;
                    }

                    Timer timer = t;
                    t = t.next;

                    if (!timer.cancelled)
                    {
                        timer.Fire();
                    }
                }
                updating = false;
            
                // Put timers which were cancelled during this update back into the pool
                if (delayedFreeRootTimer != null)
                {
                    for (Timer t = delayedFreeRootTimer; t != null;)
                    {
                        Timer timer = t;
                        t = t.helpListNext;

                        CancelTimerInLoop(timer);
                    }
                    delayedFreeRootTimer = null;
                }

                // Add timers which were scheduled during this update
                if (delayedAddHeadTimer != null)
                {
                    for (Timer t = delayedAddHeadTimer; t != null;)
                    {
                        Timer timer = t;
                        t = t.helpListNext;

                        AddTimer(timer);
                    }
                    delayedAddHeadTimer = null;
                    delayedAddTailTimer = null;
                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Schedule

    @Override
    public Timer Schedule(Runnable callback, float delay, int numRepeats, String name)
    {
        return Schedule(callback, Timer.DefaultTimerCallback, delay, numRepeats, name);
    }

    @Override
    public Timer Schedule(TimerRunnable callback, float delay, int numRepeats, String name)
    {
        return Schedule(null, callback, delay, numRepeats, name);
    }

    private Timer Schedule(Runnable callback1, TimerRunnable callback2, float delay, int numRepeats, String name)
    {
        float timeout = delay < 0 ? 0 : delay;

        Timer timer = NextFreeTimer();
        timer.callback1 = callback1;
        timer.callback2 = callback2;
        timer.timeout = timeout;
        timer.numRepeats = numRepeats;
        timer.scheduleTime = currentTime;
        timer.fireTime = currentTime + timeout;
        timer.name = name;

        synchronized (this)
        {
            if (updating)
            {
                AddTimerDelayed(timer);
            }
            else
            {
                AddTimer(timer);
            }
        }

        return timer;
    }

    @Override
    protected Timer FindTimer(Runnable callback)
    {
        for (Timer timer = rootTimer; timer != null; timer = timer.next)
        {
            if (timer.callback1 == callback)
            {
                return timer;
            }
        }

        return null;
    }

    @Override
    protected Timer FindTimer(TimerRunnable callback)
    {
        for (Timer timer = rootTimer; timer != null; timer = timer.next)
        {
            if (timer.callback2 == callback)
            {
                return timer;
            }
        }

        return null;
    }

    @Override
    public void Cancel(Runnable callback)
    {
        synchronized (this)
        {
            for (Timer timer = rootTimer; timer != null;)
            {
                Timer t = timer;
                timer = timer.next;

                if (t.callback1 == callback)
                {
                    t.Cancel();
                }
            }
        }
    }

    @Override
    public void Cancel(TimerRunnable callback)
    {
        synchronized (this)
        {
            for (Timer timer = rootTimer; timer != null;)
            {
                Timer t = timer;
                timer = timer.next;

                if (t.callback2 == callback)
                {
                    t.Cancel();
                }
            }
        }
    }

    @Override
    public void Cancel(String name)
    {
        synchronized (this)
        {
            for (Timer timer = rootTimer; timer != null;)
            {
                Timer t = timer;
                timer = timer.next;

                if (t.name == name)
                {
                    t.Cancel();
                }
            }
        }
    }

    @Override
    public void CancelAll()
    {
        synchronized (this)
        {
            for (Timer timer = rootTimer; timer != null;)
            {
                Timer t = timer;
                timer = timer.next;

                t.Cancel();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void Destroy()
    {
        CancelAll();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Timer List

    private Timer NextFreeTimer()
    {
        Timer timer = Timer.NextFreeTimer();
        timer.manager = this;
        return timer;
    }

    private void AddTimerDelayed(Timer timer)
    {
        if (delayedAddHeadTimer == null)
        {
            delayedAddHeadTimer = timer; // beginning of the list
        }

        if (delayedAddTailTimer != null)
        {
            delayedAddTailTimer.helpListNext = timer;
        }
        delayedAddTailTimer = timer;
    }

    private void AddFreeTimerDelayed(Timer timer)
    {
        timer.helpListNext = delayedFreeRootTimer;
        delayedFreeRootTimer = timer;
    }

    private void AddFreeTimer(Timer timer)
    {
        Timer.AddFreeTimer(timer);
    }

    private void AddTimer(Timer timer)
    {
        Assert.AreSame(this, timer.manager);
        ++timersCount;

        if (rootTimer != null)
        {
            // if timer has the least remaining time - it goes first
            if (timer.fireTime < rootTimer.fireTime)
            {
                timer.next = rootTimer;
                rootTimer.prev = timer;
                rootTimer = timer;

                return;
            }

            // try to insert in a sorted order
            Timer tail = rootTimer;
            for (Timer t = rootTimer.next; t != null; tail = t, t = t.next)
            {
                if (timer.fireTime < t.fireTime)
                {
                    Timer prev = t.prev;
                    Timer next = t;

                    timer.prev = prev;
                    timer.next = next;

                    next.prev = timer;
                    prev.next = timer;

                    return;
                }
            }

            // add timer at the end of the list
            tail.next = timer;
            timer.prev = tail;
        }
        else
        {
            rootTimer = timer; // timer is root now
        }
    }

    private void RemoveTimer(Timer timer)
    {
        Assert.AreSame(this, timer.manager);
        Assert.Greater(timersCount, 0);
        --timersCount;

        Timer prev = timer.prev;
        Timer next = timer.next;

        if (prev != null)
            prev.next = next;
        else
            rootTimer = next;

        if (next != null)
            next.prev = prev;
    }

    void CancelTimer(Timer timer)
    {   
        synchronized (this)
        {
            if (updating)
            {
                AddFreeTimerDelayed(timer);
            }
            else
            {
                CancelTimerInLoop(timer);
            }
        }
    }

    private void CancelTimerInLoop(Timer timer)
    {
        RemoveTimer(timer);
        AddFreeTimer(timer);
    }

    @Override
    public int Count()
    {
        synchronized (this)
        {
            return timersCount;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    protected Timer RootTimer()
    {
        return rootTimer;
    }

    protected Timer DelayedFreeHeadTimer()
    {
        return delayedFreeRootTimer;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Unit testing

    protected static void CancelTimers()
    {
        s_sharedInstance.CancelAll();
    }

    protected static void RunUpdate(float delta)
    {
        s_sharedInstance.Update(delta);
    }
}