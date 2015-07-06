package com.spacemadness.lunar.core;

import com.spacemadness.lunar.TestCaseEx;

import java.util.ArrayList;
import java.util.List;

public class TimerManagerTest extends TestCaseEx
{
    private List<Runnable> callbacks = new ArrayList<Runnable>();
    private MockTimerManager timerManager;

    public void testTimerReuse() throws Exception
    {
        timerManager.Schedule(TimerCallback1, 250);

        assertEquals(1, Timer.instanceCount);
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250);
        assertEquals(1, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback1, 250);

        assertEquals(0, Timer.getTimerPoolSize());
        assertEquals(1, Timer.instanceCount);

        sleep(250);
        assertEquals(1, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback1, 250);
        assertEquals(0, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback2, 250);
        assertEquals(0, Timer.getTimerPoolSize());

        assertEquals(2, Timer.instanceCount);

        sleep(250);
        assertEquals(2, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback1, 250);
        assertEquals(2, Timer.instanceCount);

        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250);
        assertEquals(2, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback1, 500);
        assertEquals(1, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback2, 500);
        assertEquals(0, Timer.getTimerPoolSize());

        assertEquals(2, Timer.instanceCount);

        sleep(250);
        assertEquals(0, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback1, 250);
        assertEquals(0, Timer.getTimerPoolSize());
        assertEquals(3, Timer.instanceCount);
        sleep(500);

        assertEquals(3, Timer.getTimerPoolSize());
    }

    public void testScheduleTimer() throws Exception
    {
        timerManager.Schedule(TimerCallback1, 500);

        sleep(250);
        AssertCallbacks();
        assertEquals(1, timerManager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback1);
        assertEquals(0, timerManager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback1);
        assertEquals(0, timerManager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());
    }

    public void testScheduleTimers() throws Exception
    {
        timerManager.Schedule(TimerCallback1, 750);
        timerManager.Schedule(TimerCallback2, 500);

        sleep(250);
        AssertCallbacks();
        assertEquals(2, timerManager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2);
        assertEquals(1, timerManager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback1);
        assertEquals(0, timerManager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback1);
        assertEquals(0, timerManager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());
    }

    public void testScheduleMoreTimers() throws Exception
    {
        timerManager.Schedule(TimerCallback1, 750);
        timerManager.Schedule(TimerCallback2, 500);
        timerManager.Schedule(TimerCallback4, 1000);
        timerManager.Schedule(TimerCallback3, 500);

        sleep(250);
        AssertCallbacks();
        assertEquals(4, timerManager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback3);
        assertEquals(2, timerManager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback3, TimerCallback1);
        assertEquals(1, timerManager.getTimersCount());
        assertEquals(3, Timer.getTimerPoolSize());

        sleep(250);
        AssertCallbacks(TimerCallback2, TimerCallback3, TimerCallback1, TimerCallback4);
        assertEquals(0, timerManager.getTimersCount());
        assertEquals(4, Timer.getTimerPoolSize());
    }

    public void testScheduleMoreTimersLater() throws Exception
    {
        timerManager.Schedule(TimerCallback1, 750);
        timerManager.Schedule(TimerCallback2, 500);

        sleep(250); // 0.250
        AssertCallbacks();
        assertEquals(2, timerManager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250); // 0.5
        AssertCallbacks(TimerCallback2);
        assertEquals(1, timerManager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback3, 1000); // fires at 1.5
        assertEquals(2, timerManager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250); // 0.750
        AssertCallbacks(TimerCallback2, TimerCallback1);
        assertEquals(1, timerManager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250); // 1.0
        AssertCallbacks(TimerCallback2, TimerCallback1);
        assertEquals(1, timerManager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        timerManager.Schedule(TimerCallback4, 15); // fires at 1.15
        assertEquals(2, timerManager.getTimersCount());
        assertEquals(0, Timer.getTimerPoolSize());

        sleep(250); // 1.250
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4);
        assertEquals(1, timerManager.getTimersCount());
        assertEquals(1, Timer.getTimerPoolSize());

        sleep(250); // 1.5
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4, TimerCallback3);
        assertEquals(0, timerManager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());

        sleep(250); // 1.750
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4, TimerCallback3);
        assertEquals(0, timerManager.getTimersCount());
        assertEquals(2, Timer.getTimerPoolSize());
    }

    public void testCancelTimer() throws Exception
    {
        timerManager.Schedule(TimerCallback1, 500);
        sleep();

        timerManager.Cancel(TimerCallback1);

        AssertCallbacks();
        assertEquals(0, timerManager.getTimersCount());

        sleep(500);
        AssertCallbacks();
        assertEquals(0, timerManager.getTimersCount());
    }

    public void testCancelTimers() throws Exception
    {
        timerManager.Schedule(TimerCallback1, 750);
        timerManager.Schedule(TimerCallback2, 500);

        sleep(250); // 0.250
        AssertCallbacks();
        assertEquals(2, timerManager.getTimersCount());

        sleep(250); // 0.5
        AssertCallbacks(TimerCallback2);
        assertEquals(1, timerManager.getTimersCount());

        timerManager.Schedule(TimerCallback3, 1000); // fires at 1.5
        assertEquals(2, timerManager.getTimersCount());

        timerManager.Cancel(TimerCallback1);

        sleep(250); // 0.750
        AssertCallbacks(TimerCallback2);
        assertEquals(1, timerManager.getTimersCount());

        sleep(250); // 1.0
        AssertCallbacks(TimerCallback2);
        assertEquals(1, timerManager.getTimersCount());

        timerManager.Schedule(TimerCallback4, 15); // fires at 1.15
        assertEquals(2, timerManager.getTimersCount());

        timerManager.Cancel(TimerCallback3);

        sleep(250); // 1.250
        AssertCallbacks(TimerCallback2, TimerCallback4);
        assertEquals(0, timerManager.getTimersCount());

        sleep(250); // 1.5
        AssertCallbacks(TimerCallback2, TimerCallback4);
        assertEquals(0, timerManager.getTimersCount());

        sleep(250); // 1.750
        AssertCallbacks(TimerCallback2, TimerCallback4);
        assertEquals(0, timerManager.getTimersCount());
    }

    public void testCancelTimerInLoop() throws Exception
    {
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
                timerManager.Cancel(TimerCallback1);
                timerManager.Cancel(TimerCallback2);
            }
        };

        timerManager.Schedule(callback, 250);
        timerManager.Schedule(TimerCallback1, 250);
        timerManager.Schedule(TimerCallback2, 250);
        timerManager.Schedule(TimerCallback3, 250);

        sleep(250);

        AssertCallbacks(dummyCallback, TimerCallback3);
        assertEquals(0, timerManager.getTimersCount());
    }

    public void testCancelAll() throws Exception
    {
        timerManager.Schedule(TimerCallback1, 100);
        timerManager.Schedule(TimerCallback2, 100);
        timerManager.Schedule(TimerCallback3, 100);

        timerManager.cancelAll();
        assertEquals(0, timerManager.getTimersCount());

        timerManager.sleep(100);

        AssertCallbacks();
        assertEquals(0, timerManager.getTimersCount());
    }

    public void testAddTimerOnce() throws Exception
    {
        timerManager.ScheduleOnce(TimerCallback1, 250);
        timerManager.ScheduleOnce(TimerCallback2, 250);
        timerManager.ScheduleOnce(TimerCallback3, 250);
        timerManager.ScheduleOnce(TimerCallback1, 250);
        timerManager.ScheduleOnce(TimerCallback2, 250);

        assertEquals(3, timerManager.getTimersCount());

        sleep(250);
        AssertCallbacks(TimerCallback1, TimerCallback2, TimerCallback3);
        assertEquals(0, timerManager.getTimersCount());
    }

    public void testAddDelayedTimer() throws Exception
    {
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
                timerManager.Schedule(TimerCallback1, 250);
                timerManager.Schedule(TimerCallback2, 250);
                timerManager.Schedule(TimerCallback3, 250);
            }
        };

        timerManager.Schedule(callback, 250);
        assertEquals(1, timerManager.getTimersCount());

        sleep(250); // 0.250
        AssertCallbacks(dummyCallback);
        assertEquals(3, timerManager.getTimersCount());

        sleep(250); // 0.5
        AssertCallbacks(dummyCallback, TimerCallback1, TimerCallback2, TimerCallback3);
        assertEquals(0, timerManager.getTimersCount());
    }

    public void testWithException() throws Exception
    {
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

        timerManager.Schedule(callback, 250);
        assertEquals(1, timerManager.getTimersCount());

        sleep(250); // 0.250

        AssertCallbacks(dummyCallback);
        assertEquals(0, timerManager.getTimersCount());

        sleep(250); // 0.250
        AssertCallbacks(dummyCallback);
        assertEquals(0, timerManager.getTimersCount());
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
        timerManager = new MockTimerManager();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        timerManager.waitUntilTimersFinished();
    }

    private void sleep() throws Exception
    {
        timerManager.sleep();
    }

    private void sleep(long timeout) throws Exception
    {
        timerManager.sleep(timeout);
    }
}