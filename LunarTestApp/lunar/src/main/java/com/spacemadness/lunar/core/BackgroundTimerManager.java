package com.spacemadness.lunar.core;

import android.os.HandlerThread;

public class BackgroundTimerManager extends TimerManagerImp implements IDestroyable
{
    private static int nextInstanceId;

    private final HandlerThread handlerThread;

    public static BackgroundTimerManager create()
    {
        return create(null);
    }

    public static BackgroundTimerManager create(String name)
    {
        HandlerThread handlerThread = new HandlerThread(name == null ? "Background-" + nextInstanceId++ : name);
        handlerThread.start();

        return new BackgroundTimerManager(handlerThread);
    }

    protected BackgroundTimerManager(HandlerThread handlerThread)
    {
        super(handlerThread.getLooper());
        this.handlerThread = handlerThread;
    }

    public void waitUntilTimersFinished() throws InterruptedException
    {
        quit();
        join();
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
