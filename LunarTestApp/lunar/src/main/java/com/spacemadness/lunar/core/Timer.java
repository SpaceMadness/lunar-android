package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.StringUtils;

/**
 * Created by weee on 5/28/15.
 */
public final class Timer
{
    static final Object mutex = new Object();

    static final TimerRunnable DefaultTimerCallback = new TimerRunnable()
    {
        @Override
        public void run(Timer timer)
        {
            timer.callback1.run();
        }
    };

    protected static Timer freeRoot;

    boolean cancelled;

    Runnable callback1;
    TimerRunnable callback2;

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
                callback2.run(this);

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
                Log.logException(e, "Exception while firing timer");
                Cancel();
            }
        }
    }

    public <T> T UserData(Class<? extends T> cls)
    {
        return ClassUtils.cast(userData, cls);
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
        Object callback = callback2 != DefaultTimerCallback ? callback2 : callback1;
        return StringUtils.TryFormat("Method=%s, IsRepeated=%s, Timeout=%f, Elapsed=%f]",
                callback,
                this.IsRepeated(),
                this.Timeout(),
                this.manager != null ? this.Elapsed() : 0);
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