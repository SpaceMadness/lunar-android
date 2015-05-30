package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.ClassUtils;

/**
 * Created by weee on 5/28/15.
 */
public class Timer
{
    static final Object mutex = new Object();

    static Timer freeRoot;

    boolean cancelled;

    Action callback1;
    Action<Timer> callback2;

    Timer next;
    Timer prev;

    Timer helpListNext;

    TimerManager manager;

    int numRepeats;
    int numRepeated;

    float timeout;
    double fireTime;
    double scheduleTime;

    public String name;
    public Object userData;

    public void Cancel()
    {   
        synchronized (this)
        {
            if (!cancelled)
            {
                cancelled = true;
                manager.CancelTimer(this);
            }
        }
    }

    void Fire()
    {
        synchronized (this)
        {
            try
            {
                callback2(this);

                if (!cancelled)
                {
                    ++numRepeated;
                    if (numRepeated == numRepeats)
                    {
                        Cancel();
                    }
                    else
                    {
                        fireTime = manager.currentTime + timeout;
                    }
                }
            }
            catch (Exception e)
            {
                Log.error(e, "Exception while firing timer");
                Cancel();
            }
        }
    }

    static void DefaultTimerCallback(Timer timer)
    {
        timer.callback1();
    }

    public T UserData<T>()
    {
        return ClassUtils.Cast<T>(userData);
    }

    public boolean IsRepeated()
    {
        return numRepeats != 1;
    }

    public float Timeout()
    {
        return timeout;
    }

    public float Elapsed()
    {
        return (float) (manager.currentTime - scheduleTime);
    }

    protected static Timer FreeRoot()
    {
        return freeRoot;
    }

    protected static void FreeRoot(Timer timer)
    {
        freeRoot = timer;
    }

    @Override
    public String toString()
    {
        Delegate callback = callback2 != DefaultTimerCallback ? (Delegate)callback2 : (Delegate)callback1;
        return String.Format("[Target={0}, Method={1}, IsRepeated={2}, Timeout={3}, Elapsed={4}]",
            callback != null ? callback.Target : null,
            callback != null ? callback.Method : null,
            this.IsRepeated, 
            this.Timeout, 
            this.manager != null ? this.Elapsed : 0);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Objects pool

    protected static Timer NextFreeTimer()
    {
        synchronized (mutex)
        {
            Timer timer;
            if (freeRoot != null)
            {
                timer = freeRoot;
                freeRoot = timer.next;
                timer.prev = timer.next = null;
            }
            else
            {
                timer = new Timer();
            }
        
            return timer;
        }
    }

    protected static void AddFreeTimer(Timer timer)
    {
        synchronized (mutex)
        {
            timer.Reset();

            if (freeRoot != null)
            {
                timer.next = freeRoot;
            }

            freeRoot = timer;
        }
    }

    protected static Timer NextTimer(Timer timer)
    {
        return timer.next;
    }

    protected static Timer PrevTimer(Timer timer)
    {
        return timer.prev;
    }

    protected static Timer NextHelperListTimer(Timer timer)
    {
        return timer.helpListNext;
    }

    private void Reset()
    {
        next = prev = null;
        helpListNext = null;
        manager = null;
        callback1 = null;
        callback2 = null;
        numRepeats = numRepeated = 0;
        timeout = 0;
        fireTime = 0;
        scheduleTime = 0;
        cancelled = false;
        name = null;
        userData = null;
    }
}