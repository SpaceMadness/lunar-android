package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;

public class Timer extends ObjectsPoolEntry<Timer>
{
    private static final ObjectsPool<Timer> timersPool = new ObjectsPoolConcurrent<>(Timer.class);

    private final Runnable wrapperRunnable;

    private boolean scheduled;
    private boolean running;
    private boolean firstTimeSchedule;
    private boolean canceled;
    private boolean repeated;
    private boolean suspended;

    long delayMillis;
    private long currentDelayMillis;
    private long scheduledTimestamp;
    private long remainAfterSuspendMillis;

    private boolean ticksWhenSuspended;
    private boolean firesWhenSuspended;

    Runnable target;

    TimerManagerImp manager;

    static int instanceCount;

    public Timer()
    {
        wrapperRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (Timer.this)
                {
                    Assert.IsFalse(running);

                    running = true;
                    scheduled = false;

                    if (!canceled && !suspended)
                    {
                        try
                        {
                            target.run();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace(); // TODO: better handling
                        }
                        notifyFired();

                        if (canceled)
                        {
                            notifyCancelled();
                            remove();
                        }
                        else if (!repeated)
                        {
                            notifyFinished();
                            remove();
                        }
                        else if (!suspended)
                        {
                            schedule(wrapperRunnable, delayMillis);
                        }
                    }

                    running = false;
                }
            }
        };

        ++instanceCount;
    }

    synchronized void schedule()
    {
        Assert.IsFalse(scheduled);
        if (!scheduled)
        {
            if (!firstTimeSchedule)
            {
                firstTimeSchedule = true;
                notifyScheduled();
            }

            schedule(wrapperRunnable, delayMillis);
        }
    }

    private synchronized void remove()
    {
        manager.removeTimer(this);
    }

    public synchronized void reset()
    {
        if (scheduled)
        {
            removeCallback(wrapperRunnable);
            schedule(wrapperRunnable, delayMillis);
        }
    }

    public synchronized void cancel()
    {
        removeCallback(wrapperRunnable);

        if (scheduled)
        {
            notifyCancelled();
        }

        if (running)
        {
            canceled = true;
            scheduled = false;
            suspended = false;
        }
        else
        {
            remove();
        }
    }

    public synchronized void suspend()
    {
        if (scheduled && !suspended && !firesWhenSuspended)
        {
            suspended = true;
            remainAfterSuspendMillis = getRemainingMillis();

            removeCallback(wrapperRunnable);
            notifySuspended();
        }
    }

    public synchronized void resume()
    {
        if (scheduled && suspended)
        {
            suspended = false;

            long delay = ticksWhenSuspended ? getRemainingMillis() : remainAfterSuspendMillis;

            postCallback(wrapperRunnable, delay);
            notifyResumed();
        }
    }

    public boolean isSuspended()
    {
        return suspended;
    }

    public boolean isScheduled()
    {
        return scheduled;
    }

    public boolean isTicksWhenSuspended()
    {
        return ticksWhenSuspended;
    }

    public void setTicksWhenSuspended(boolean ticksWhenSuspended)
    {
        this.ticksWhenSuspended = ticksWhenSuspended;
    }

    public boolean isFiresWhenSuspended()
    {
        return firesWhenSuspended;
    }

    public void setFiresWhenSuspended(boolean firesWhenSuspended)
    {
        this.firesWhenSuspended = firesWhenSuspended;
    }

    public Runnable getTarget()
    {
        return target;
    }

    public long getRemainingMillis()
    {
        long remains = currentDelayMillis - (System.currentTimeMillis() - scheduledTimestamp);
        return remains > 0 ? remains : 0;
    }

    public long getDelayMillis()
    {
        return delayMillis;
    }

    private synchronized void schedule(Runnable runnable, long delay)
    {
        boolean succeed = postCallback(runnable, delay);
        Assert.IsTrue(succeed, "Unable to queue runnable object");

        canceled = false;
        scheduled = true;
    }

    private boolean postCallback(Runnable runnable, long delay)
    {
        currentDelayMillis = delay;
        scheduledTimestamp = System.currentTimeMillis();
        return manager.handler.postDelayed(runnable, delay);
    }

    private void removeCallback(Runnable runnable)
    {
        manager.handler.removeCallbacks(runnable);
    }

    ////////////////////////////////////////////////////////////////
    // Object pool

    @Override
    protected void OnRecycleObject()
    {
        // wrapperRunnable = null; keep the wrapper runnable
        manager                   = null;
        target                    = null;

        scheduled                 = false;
        firstTimeSchedule         = false;
        canceled                  = false;
        repeated                  = false;
        suspended                 = false;
        ticksWhenSuspended        = false;
        running                   = false;

        delayMillis               = 0L;
        currentDelayMillis        = 0L;
        scheduledTimestamp        = 0L;
        remainAfterSuspendMillis  = 0L;
    }

    Timer listNext()
    {
        return ListNodeNext();
    }

    Timer listPrev()
    {
        return ListNodePrev();
    }

    static int getTimerPoolSize()
    {
        return timersPool.size();
    }

    static Timer NextFreeTimer()
    {
        return timersPool.NextObject();
    }

    static void drainPool()
    {
        timersPool.clear();
    }

    ////////////////////////////////////////////////////////////////
    // Notifications

    private void notifyScheduled()
    {
        manager.timerScheduled(this);
    }

    private void notifyFired()
    {
        manager.timerFired(this);
    }

    private void notifyCancelled()
    {
        manager.timerCancelled(this);
    }

    private void notifyFinished()
    {
        manager.timerFinished(this);
    }

    private void notifySuspended()
    {
        manager.timerSuspended(this);
    }

    private void notifyResumed()
    {
        manager.timerResumed(this);
    }
}