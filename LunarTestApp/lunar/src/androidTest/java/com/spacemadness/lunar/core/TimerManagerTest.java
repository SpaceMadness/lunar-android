package com.spacemadness.lunar.core;

import android.os.Handler;
import android.os.HandlerThread;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TimerManagerTest extends TestCase
{
    private List<Runnable> callbacks = new ArrayList<Runnable>();
    private HandlerThread handlerThread;
    private Handler handler;
    private boolean waitFlag;

    public void testTimerReuse() throws Exception
    {
        TimerManager manager = new TimerManager(handler);

        manager.Schedule(TimerCallback1, 250);

        assertEquals(1, Timer.instanceCount);
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250);
        assertEquals(1, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback1, 250);

        assertEquals(0, Timer.getTimerPoolSize());
        assertEquals(1, Timer.instanceCount);

        sleep(250);
        assertEquals(1, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback1, 250);
        assertEquals(0, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback2, 250);
        assertEquals(0, Timer.getTimerPoolSize());

        assertEquals(2, Timer.instanceCount);

        sleep(250);
        assertEquals(2, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback1, 250);
        assertEquals(2, Timer.instanceCount);

        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250);
        assertEquals(2, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback1, 500);
        assertEquals(1, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback2, 500);
        assertEquals(0, Timer.getTimerPoolSize());

        assertEquals(2, Timer.instanceCount);

        sleep(250);
        assertEquals(0, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback1, 250);
        assertEquals(0, Timer.getTimerPoolSize());
        assertEquals(3, Timer.instanceCount);
        sleep(500);

        assertEquals(3, Timer.getTimerPoolSize());
    }

    public void testScheduleTimer() throws Exception
    {
        TimerManager manager = new TimerManager(handler);
        manager.Schedule(TimerCallback1, 500);

        sleep(250);
        AssertCallbacks();
        assertEquals(1, manager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback1);
        assertEquals(0, manager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback1);
        assertEquals(0, manager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());
    }

    public void testScheduleTimers() throws Exception
    {
        TimerManager manager = new TimerManager(handler);
        manager.Schedule(TimerCallback1, 750);
        manager.Schedule(TimerCallback2, 500);

        sleep(250);
        AssertCallbacks();
        assertEquals(2, manager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2);
        assertEquals(1, manager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback1);
        assertEquals(0, manager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback1);
        assertEquals(0, manager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());
    }

    public void testScheduleMoreTimers() throws Exception
    {
        TimerManager manager = new TimerManager(handler);
        manager.Schedule(TimerCallback1, 750);
        manager.Schedule(TimerCallback2, 500);
        manager.Schedule(TimerCallback4, 1000);
        manager.Schedule(TimerCallback3, 500);

        sleep(250);
        AssertCallbacks();
        assertEquals(4, manager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback3);
        assertEquals(2, manager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback3, TimerCallback1);
        assertEquals(1, manager.getTimersCount());
        assertEquals(3, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback3, TimerCallback1, TimerCallback4);
        assertEquals(0, manager.getTimersCount());
        assertEquals(4, Timer.getTimerPoolSize());
    }

    public void testScheduleMoreTimersLater() throws Exception
    {
        TimerManager manager = new TimerManager(handler);
        manager.Schedule(TimerCallback1, 750);
        manager.Schedule(TimerCallback2, 500);

        sleep(250); // 0.250
        AssertCallbacks();
        assertEquals(2, manager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250); // 0.5
        AssertCallbacks(TimerCallback2);
        assertEquals(1, manager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback3, 1000); // fires at 1.5
        assertEquals(2, manager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250); // 0.750
        AssertCallbacks(TimerCallback2, TimerCallback1);
        assertEquals(1, manager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250); // 1.0
        AssertCallbacks(TimerCallback2, TimerCallback1);
        assertEquals(1, manager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        manager.Schedule(TimerCallback4, 15); // fires at 1.15
        assertEquals(2, manager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250); // 1.250
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4);
        assertEquals(1, manager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250); // 1.5
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4, TimerCallback3);
        assertEquals(0, manager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());

        sleep(250); // 1.750
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4, TimerCallback3);
        assertEquals(0, manager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());
    }

    public void testCancelTimer() throws Exception
    {
        TimerManager manager = new TimerManager(handler);
        manager.Schedule(TimerCallback1, 500);
        sleep();

        manager.Cancel(TimerCallback1);

        AssertCallbacks();
        assertEquals(0, manager.getTimersCount());

        sleep(500);
        AssertCallbacks();
        assertEquals(0, manager.getTimersCount());
    }

    public void testCancelTimers() throws Exception
    {
        TimerManager manager = new TimerManager(handler);
        manager.Schedule(TimerCallback1, 750);
        manager.Schedule(TimerCallback2, 500);

        sleep(250); // 0.250
        AssertCallbacks();
        assertEquals(2, manager.getTimersCount());

        sleep(250); // 0.5
        AssertCallbacks(TimerCallback2);
        assertEquals(1, manager.getTimersCount());

        manager.Schedule(TimerCallback3, 1000); // fires at 1.5
        assertEquals(2, manager.getTimersCount());

        manager.Cancel(TimerCallback1);

        sleep(250); // 0.750
        AssertCallbacks(TimerCallback2);
        assertEquals(1, manager.getTimersCount());

        sleep(250); // 1.0
        AssertCallbacks(TimerCallback2);
        assertEquals(1, manager.getTimersCount());

        manager.Schedule(TimerCallback4, 15); // fires at 1.15
        assertEquals(2, manager.getTimersCount());

        manager.Cancel(TimerCallback3);

        sleep(250); // 1.250
        AssertCallbacks(TimerCallback2, TimerCallback4);
        assertEquals(0, manager.getTimersCount());

        sleep(250); // 1.5
        AssertCallbacks(TimerCallback2, TimerCallback4);
        assertEquals(0, manager.getTimersCount());

        sleep(250); // 1.750
        AssertCallbacks(TimerCallback2, TimerCallback4);
        assertEquals(0, manager.getTimersCount());
    }

    public void testCancelTimerInLoop() throws Exception
    {
        final TimerManager manager = new TimerManager(handler);

        final Runnable dummyCallback = new Runnable()
        {
            @Override
            public void run()
            {
            }
        };

        final Runnable callback = new Runnable()
        {
            @Override
            public void run()
            {
                callbacks.add(dummyCallback);
                manager.Cancel(TimerCallback1);
                manager.Cancel(TimerCallback2);
            }
        };

        manager.Schedule(callback, 250);
        manager.Schedule(TimerCallback1, 250);
        manager.Schedule(TimerCallback2, 250);
        manager.Schedule(TimerCallback3, 250);

        sleep(250);

        AssertCallbacks(dummyCallback, TimerCallback3);
        assertEquals(0, manager.getTimersCount());
    }

    public void testAddTimerOnce() throws Exception
    {
        TimerManager manager = new TimerManager(handler);
        manager.ScheduleOnce(TimerCallback1, 250);
        manager.ScheduleOnce(TimerCallback2, 250);
        manager.ScheduleOnce(TimerCallback3, 250);
        manager.ScheduleOnce(TimerCallback1, 250);
        manager.ScheduleOnce(TimerCallback2, 250);

        assertEquals(3, manager.getTimersCount());

        sleep(250);
        AssertCallbacks(TimerCallback1, TimerCallback2, TimerCallback3);
        assertEquals(0, manager.getTimersCount());
    }

    public void testAddDelayedTimer() throws Exception
    {
        final TimerManager manager = new TimerManager(handler);

        final Runnable dummyCallback = new Runnable()
        {
            @Override
            public void run()
            {
            }
        };

        final Runnable callback = new Runnable()
        {
            @Override
            public void run()
            {
                callbacks.add(dummyCallback);
                manager.Schedule(TimerCallback1, 250);
                manager.Schedule(TimerCallback2, 250);
                manager.Schedule(TimerCallback3, 250);
            }
        };

        manager.Schedule(callback, 250);
        assertEquals(1, manager.getTimersCount());

        sleep(250); // 0.250
        AssertCallbacks(dummyCallback);
        assertEquals(3, manager.getTimersCount());

        sleep(250); // 0.5
        AssertCallbacks(dummyCallback, TimerCallback1, TimerCallback2, TimerCallback3);
        assertEquals(0, manager.getTimersCount());
    }

    public void testWithException() throws Exception
    {
        TimerManager manager = new TimerManager(handler);

        final Runnable dummyCallback = new Runnable()
        {
            @Override
            public void run()
            {
            }
        };

        Runnable callback = new Runnable()
        {
            @Override
            public void run()
            {
                callbacks.add(dummyCallback);
                throw new RuntimeException();
            }
        };

        manager.Schedule(callback, 250);
        assertEquals(1, manager.getTimersCount());

        sleep(250); // 0.250

        AssertCallbacks(dummyCallback);
        assertEquals(0, manager.getTimersCount());

        sleep(250); // 0.250
        AssertCallbacks(dummyCallback);
        assertEquals(0, manager.getTimersCount());
    }

    private Runnable TimerCallback1 = new Runnable()
    {
        @Override
        public void run()
        {
            callbacks.add(this);
        }
    };

    private Runnable TimerCallback2 = new Runnable()
    {
        @Override
        public void run()
        {
            callbacks.add(this);
        }
    };

    private Runnable TimerCallback3 = new Runnable()
    {
        @Override
        public void run()
        {
            callbacks.add(this);
        }
    };

    private Runnable TimerCallback4 = new Runnable()
    {
        @Override
        public void run()
        {
            callbacks.add(this);
        }
    };

    private void AssertCallbacks(Runnable... expected)
    {
        Runnable[] actual = GetCallbacks();
        assertEquals(expected.length, actual.length);

        String message = "";
        for (int i = 0; i < expected.length; ++i)
        {
            if (expected[i] != actual[i])
            {
                message += expected[i] + "!=" + actual[i];
            }
        }

        assertTrue(message, message.length() == 0);
    }

    private Runnable[] GetCallbacks()
    {
        return callbacks.toArray(new Runnable[callbacks.size()]);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        Timer.drainPool();
        Timer.instanceCount = 0;

        handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        handlerThread.getLooper().quit();
        handlerThread.join();
    }

    private void sleep() throws Exception
    {
        sleep(0);
    }

    private void sleep(long timeout) throws Exception
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
}