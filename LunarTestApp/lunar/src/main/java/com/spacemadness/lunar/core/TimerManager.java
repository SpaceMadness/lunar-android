package com.spacemadness.lunar.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.FastConcurrentList;
import com.spacemadness.lunar.utils.FastList;

public class TimerManager
{
    private static int nextBackgroundManagerId;

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

    public static Background createInBackground()
    {
        return createInBackground(null);
    }

    public static Background createInBackground(String name)
    {
        HandlerThread handlerThread = new HandlerThread(name == null ? "Background-" + nextBackgroundManagerId++ : name);
        handlerThread.start();

        return new Background(handlerThread);
    }

    public static class Background extends TimerManager implements IDestroyable
    {
        private final HandlerThread handlerThread;

        private Background(HandlerThread handlerThread)
        {
            super(handlerThread.getLooper());
            this.handlerThread = handlerThread;
        }

        public void quit()
        {
            handlerThread.getLooper().quit();
        }

        public void join() throws InterruptedException
        {
            handlerThread.join();
        }

        @Override
        public void Destroy()
        {
            quit();
        }
    }

    private static class Holder
    {
        public static final TimerManager mainManager = new TimerManager(Looper.getMainLooper());
    }
}
