package com.spacemadness.lunar.core;

import android.os.HandlerThread;

public class MockTimerManager extends TimerManagerImp
{
    private final HandlerThread handlerThread;
    private boolean waitFlag;

    private MockTimerManager(HandlerThread handlerThread)
    {
        super(handlerThread.getLooper());
        this.handlerThread = handlerThread;
    }

    public static MockTimerManager create()
    {
        Timer.drainPool();
        Timer.instanceCount = 0;

        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();

        return new MockTimerManager(handlerThread);
    }

    public void sleep() throws InterruptedException
    {
        sleep(0);
    }

    public void sleep(long timeout) throws InterruptedException
    {
        final Object lock = new Object();
        waitFlag = true;

        handler.postDelayed(new Runnable()
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
        handlerThread.getLooper().quit();
        handlerThread.join();
    }
}
