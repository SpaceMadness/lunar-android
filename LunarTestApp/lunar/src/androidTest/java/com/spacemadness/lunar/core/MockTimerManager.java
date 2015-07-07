package com.spacemadness.lunar.core;

import junit.framework.AssertionFailedError;

public class MockTimerManager extends TimerManager
{
    public final static TimerManager Null = new TimerManager()
    {
        @Override
        public Timer ScheduleOnce(Runnable target, long delayMillis)
        {
            return null;
        }

        @Override
        public Timer Schedule(Runnable target, long delayMillis)
        {
            return null;
        }

        @Override
        public Timer Cancel(Runnable target)
        {
            return null;
        }

        @Override
        public void cancelAll()
        {
        }

        @Override
        public void Destroy()
        {
        }
    };

    private final BackgroundTimerManager backgroundTimerManager;
    private final Object timerWaitLock = new Object();

    private boolean waitFlag;

    public MockTimerManager()
    {
        Timer.drainPool();
        Timer.instanceCount = 0;

        backgroundTimerManager = BackgroundTimerManager.create();
        backgroundTimerManager.setListener(new TimerListener()
        {
            @Override
            public void onTimerScheduled(TimerManager manager, Timer timer)
            {
            }

            @Override
            public void onTimerFired(TimerManager manager, Timer timer)
            {
            }

            @Override
            public void onTimerCancelled(TimerManager manager, Timer timer)
            {
            }

            @Override
            public void onTimerFinished(TimerManager manager, Timer timer)
            {
            }

            @Override
            public void onTimerRemoved(TimerManager manager, Timer timer)
            {
                synchronized (timerWaitLock)
                {
                    timerWaitLock.notifyAll();
                }
            }

            @Override
            public void onTimerSuspended(TimerManager manager, Timer timer)
            {
            }

            @Override
            public void onTimerResumed(TimerManager manager, Timer timer)
            {
            }
        });
    }

    public void sleep() throws InterruptedException
    {
        sleep(0);
    }

    public void sleep(long timeout) throws InterruptedException
    {
        final Object lock = new Object();
        waitFlag = true;

        backgroundTimerManager.getHandler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (lock)
                {
                    waitFlag = false;
                    lock.notifyAll();
                }
            }
        }, timeout);

        synchronized (lock)
        {
            while (waitFlag)
            {
                lock.wait();
            }
        }
    }

    @Override
    public Timer ScheduleOnce(Runnable target, long delayMillis)
    {
        return backgroundTimerManager.ScheduleOnce(target, delayMillis);
    }

    @Override
    public Timer Schedule(Runnable target, long delayMillis)
    {
        return backgroundTimerManager.Schedule(target, delayMillis);
    }

    @Override
    public Timer Cancel(Runnable target)
    {
        return backgroundTimerManager.Cancel(target);
    }

    @Override
    public void cancelAll()
    {
        backgroundTimerManager.cancelAll();
    }

    @Override
    public void Destroy()
    {
        backgroundTimerManager.Destroy();
        try
        {
            backgroundTimerManager.join();
        }
        catch (InterruptedException e)
        {
            throw new AssertionFailedError(e.getMessage());
        }
        finally
        {
            Timer.drainPool();
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    public void waitUntilTimersFinished() throws InterruptedException
    {
        synchronized (timerWaitLock)
        {
            while (getTimersCount() > 0)
            {
                timerWaitLock.wait();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public int getTimersCount()
    {
        return backgroundTimerManager.getTimersCount();
    }
}
