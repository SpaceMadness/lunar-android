package com.spacemadness.lunar.core;

import android.os.Handler;
import android.os.Looper;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.FastConcurrentList;
import com.spacemadness.lunar.utils.FastList;

public class TimerManager
{
    private final Object lock = new Object();
    private final FastList<Timer> timers;

    final Handler handler;

    public TimerManager(Looper looper)
    {
        this(new Handler(looper));
    }

    public TimerManager(Handler handler)
    {
        if (handler == null)
        {
            throw new NullPointerException("Handler is null");
        }
        this.handler = handler;
        this.timers = new FastConcurrentList<>();
    }

    public Timer ScheduleOnce(Runnable target)
    {
        return ScheduleOnce(target, 0L);
    }

    public Timer ScheduleOnce(Runnable target, long delayMillis)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        synchronized (lock)
        {
            Timer timer = findTimer(target);
            if (timer != null)
            {
                return timer;
            }

            return Schedule(target, delayMillis);
        }
    }

    public Timer Schedule(Runnable target)
    {
        return Schedule(target, 0L);
    }

    public Timer Schedule(Runnable target, long delayMillis)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        synchronized (lock)
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

    public Timer Cancel(Runnable target)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        synchronized (lock)
        {
            Timer timer = findTimer(target);
            if (timer != null)
            {
                timer.cancel();
            }

            return timer;
        }
    }

    void addTimer(Timer timer)
    {
        synchronized (lock)
        {
            Assert.IsNull(timer.manager);
            timers.AddLastItem(timer);
        }
    }

    void removeTimer(Timer timer)
    {
        synchronized (lock)
        {
            Assert.AreSame(this, timer.manager);
            Assert.IsTrue(timers.Count() > 0);

            timers.RemoveItem(timer);
            timer.Recycle();
        }
    }

    Timer findTimer(Runnable target)
    {
        synchronized (lock)
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

    boolean containsTimer(Runnable target)
    {
        return findTimer(target) != null;
    }

    public int getTimersCount()
    {
        return timers.Count();
    }

    public static TimerManager getMainManager()
    {
        return Holder.mainManager;
    }

    private static class Holder
    {
        public static final TimerManager mainManager = new TimerManager(Looper.getMainLooper());
    }
}
