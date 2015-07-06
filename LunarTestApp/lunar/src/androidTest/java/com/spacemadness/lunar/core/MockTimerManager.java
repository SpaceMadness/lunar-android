package com.spacemadness.lunar.core;

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
    private boolean waitFlag;

    public MockTimerManager()
    {
        Timer.drainPool();
        Timer.instanceCount = 0;

        backgroundTimerManager = BackgroundTimerManager.create();
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

    public void waitUntilTimersFinished() throws InterruptedException
    {
        backgroundTimerManager.waitUntilTimersFinished();
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
        Timer.drainPool();
    }

    public int getTimersCount()
    {
        return backgroundTimerManager.getTimersCount();
    }
}
