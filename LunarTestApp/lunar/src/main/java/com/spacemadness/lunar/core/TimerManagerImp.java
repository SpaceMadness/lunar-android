package com.spacemadness.lunar.core;

import android.os.Handler;
import android.os.Looper;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.FastConcurrentList;
import com.spacemadness.lunar.utils.FastList;

public class TimerManagerImp extends TimerManager
{
    private final FastList<Timer> timers;

    final Handler handler;

    public TimerManagerImp(Looper looper)
    {
        this(new Handler(looper));
    }

    public TimerManagerImp(Handler handler)
    {
        if (handler == null)
        {
            throw new NullPointerException("Handler is null");
        }
        this.handler = handler;
        this.timers = new FastConcurrentList<>();
    }

    @Override
    public void Destroy()
    {
        cancelAll();
    }

    @Override
    public Timer ScheduleOnce(Runnable target, long delayMillis)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        synchronized (timers)
        {
            Timer timer = findTimer(target);
            if (timer != null)
            {
                return timer;
            }

            return Schedule(target, delayMillis);
        }
    }

    @Override
    public Timer Schedule(Runnable target, long delayMillis)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        synchronized (timers)
        {
            Timer timer = Timer.NextFreeTimer();
            addTimer(timer);
            timer.manager = this;
            timer.target = target;
            timer.delayMillis = delayMillis < 0 ? 0 : delayMillis;
            timer.schedule();
            return timer;
        }
    }

    @Override
    public Timer Cancel(Runnable target)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        synchronized (timers)
        {
            Timer timer = findTimer(target);
            if (timer != null)
            {
                timer.cancel();
            }

            return timer;
        }
    }

    @Override
    public void cancelAll()
    {
        synchronized (timers)
        {
            for (Timer t = timers.ListFirst(); t != null;)
            {
                Timer next = t.listNext();
                t.cancel();
                t = next;
            }

            Assert.IsTrue(timers.isEmpty());
        }
    }

    void addTimer(Timer timer)
    {
        synchronized (timers)
        {
            Assert.IsNull(timer.manager);
            timers.AddLastItem(timer);
        }
    }

    void removeTimer(Timer timer)
    {
        synchronized (timers)
        {
            Assert.AreSame(this, timer.manager);
            Assert.IsTrue(timers.Count() > 0);

            timers.RemoveItem(timer);
            timer.Recycle();
        }
    }

    Timer findTimer(Runnable target)
    {
        synchronized (timers)
        {
            for (Timer t = timers.ListFirst(); t != null; t = t.listNext())
            {
                if (t.target == target)
                {
                    return t;
                }
            }

            return null;
        }
    }

    protected Handler getHandler()
    {
        return handler;
    }

    boolean containsTimer(Runnable target)
    {
        return findTimer(target) != null;
    }

    public int getTimersCount()
    {
        return timers.Count();
    }
}
