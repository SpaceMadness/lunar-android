package com.spacemadness.lunar.core;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alementuev on 6/9/15.
 */
public class TimerManagerTest extends TestCase
{
    private List<Runnable> callbacks = new ArrayList<Runnable>();

    @Override
    protected void setUp()
    {
        callbacks.clear();
        Timer.freeRoot = null;
    }

    public void testSortingTimers1()
    {
        TestTimerManager manager = new TestTimerManager();
        manager.Schedule(TimerCallback1, 0.0f,  "timer1");
        manager.Schedule(TimerCallback1, 0.25f, "timer2");
        manager.Schedule(TimerCallback1, 0.25f, "timer3");
        manager.Schedule(TimerCallback1, 0.5f,  "timer4");
        manager.Schedule(TimerCallback1, 0.75f, "timer5");

        Timer timer = manager.RootTimer();
        Assert.assertEquals(timer.name, "timer1"); timer = NextTimer(timer);
        Assert.assertEquals(timer.name, "timer2"); timer = NextTimer(timer);
        Assert.assertEquals(timer.name, "timer3"); timer = NextTimer(timer);
        Assert.assertEquals(timer.name, "timer4"); timer = NextTimer(timer);
        Assert.assertEquals(timer.name, "timer5"); timer = NextTimer(timer);
        Assert.assertNull(timer);
    }

    public void testSortingTimers2()
    {
        TestTimerManager manager = new TestTimerManager();
        manager.Schedule(TimerCallback1, 0.75f, "timer1");
        manager.Schedule(TimerCallback1, 0.5f,  "timer2");
        manager.Schedule(TimerCallback1, 0.25f, "timer3");
        manager.Schedule(TimerCallback1, 0.25f, "timer4");
        manager.Schedule(TimerCallback1, 0.0f,  "timer5");

        Timer timer = manager.RootTimer();
        Assert.assertEquals(timer.name, "timer5"); timer = NextTimer(timer);
        Assert.assertEquals(timer.name, "timer3"); timer = NextTimer(timer);
        Assert.assertEquals(timer.name, "timer4"); timer = NextTimer(timer);
        Assert.assertEquals(timer.name, "timer2"); timer = NextTimer(timer);
        Assert.assertEquals(timer.name, "timer1"); timer = NextTimer(timer);
        Assert.assertNull(timer);
    }

    public void testScheduleTimer()
    {
        TestTimerManager manager = new TestTimerManager();
        manager.Schedule(TimerCallback1, 0.5f);

        manager.Update(0.25f);
        AssertCallbacks();
        Assert.assertEquals(1, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback1);
        Assert.assertEquals(0, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback1);
        Assert.assertEquals(0, manager.Count());
    }

    public void testScheduleTimers()
    {
        TestTimerManager manager = new TestTimerManager();
        manager.Schedule(TimerCallback1, 0.75f);
        manager.Schedule(TimerCallback2, 0.5f);

        manager.Update(0.25f);
        AssertCallbacks();
        Assert.assertEquals(2, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback2);
        Assert.assertEquals(1, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback2, TimerCallback1);
        Assert.assertEquals(0, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback2, TimerCallback1);
        Assert.assertEquals(0, manager.Count());
    }

    public void testScheduleMoreTimers()
    {
        TestTimerManager manager = new TestTimerManager();
        manager.Schedule(TimerCallback1, 0.75f);
        manager.Schedule(TimerCallback2, 0.5f);
        manager.Schedule(TimerCallback3, 0.5f);
        manager.Schedule(TimerCallback4, 1.0f);

        manager.Update(0.25f);
        AssertCallbacks();
        Assert.assertEquals(4, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback2, TimerCallback3);
        Assert.assertEquals(2, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback2, TimerCallback3, TimerCallback1);
        Assert.assertEquals(1, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback2, TimerCallback3, TimerCallback1, TimerCallback4);
        Assert.assertEquals(0, manager.Count());
    }

    public void testScheduleMoreTimersLater()
    {
        TestTimerManager manager = new TestTimerManager();
        manager.Schedule(TimerCallback1, 0.75f);
        manager.Schedule(TimerCallback2, 0.5f);

        manager.Update(0.25f); // 0.25
        AssertCallbacks();
        Assert.assertEquals(2, manager.Count());

        manager.Update(0.25f); // 0.5
        AssertCallbacks(TimerCallback2);
        Assert.assertEquals(1, manager.Count());

        manager.Schedule(TimerCallback3, 1.0f); // fires at 1.5
        Assert.assertEquals(2, manager.Count());

        manager.Update(0.25f); // 0.75
        AssertCallbacks(TimerCallback2, TimerCallback1);
        Assert.assertEquals(1, manager.Count());

        manager.Update(0.25f); // 1.0
        AssertCallbacks(TimerCallback2, TimerCallback1);
        Assert.assertEquals(1, manager.Count());

        manager.Schedule(TimerCallback4, 0.15f); // fires at 1.15
        Assert.assertEquals(2, manager.Count());

        manager.Update(0.25f); // 1.25
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4);
        Assert.assertEquals(1, manager.Count());

        manager.Update(0.25f); // 1.5
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4, TimerCallback3);
        Assert.assertEquals(0, manager.Count());

        manager.Update(0.25f); // 1.75
        AssertCallbacks(TimerCallback2, TimerCallback1, TimerCallback4, TimerCallback3);
        Assert.assertEquals(0, manager.Count());
    }

    public void testCancelTimer()
    {
        TestTimerManager manager = new TestTimerManager();
        manager.Schedule(TimerCallback1, 0.75f);
        manager.Schedule(TimerCallback2, 0.5f);

        manager.Update(0.25f); // 0.25
        AssertCallbacks();
        Assert.assertEquals(2, manager.Count());

        manager.Update(0.25f); // 0.5
        AssertCallbacks(TimerCallback2);
        Assert.assertEquals(1, manager.Count());

        manager.Schedule(TimerCallback3, 1.0f); // fires at 1.5
        Assert.assertEquals(2, manager.Count());

        manager.Cancel(TimerCallback1);

        manager.Update(0.25f); // 0.75
        AssertCallbacks(TimerCallback2);
        Assert.assertEquals(1, manager.Count());

        manager.Update(0.25f); // 1.0
        AssertCallbacks(TimerCallback2);
        Assert.assertEquals(1, manager.Count());

        manager.Schedule(TimerCallback4, 0.15f); // fires at 1.15
        Assert.assertEquals(2, manager.Count());

        manager.Cancel(TimerCallback3);

        manager.Update(0.25f); // 1.25
        AssertCallbacks(TimerCallback2, TimerCallback4);
        Assert.assertEquals(0, manager.Count());

        manager.Update(0.25f); // 1.5
        AssertCallbacks(TimerCallback2, TimerCallback4);
        Assert.assertEquals(0, manager.Count());

        manager.Update(0.25f); // 1.75
        AssertCallbacks(TimerCallback2, TimerCallback4);
        Assert.assertEquals(0, manager.Count());
    }

    public void testCancelTimerInLoop()
    {
        final TestTimerManager manager = new TestTimerManager();

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
                Assert.assertEquals(0, manager.FreeDelayedCount());

                manager.Cancel(TimerCallback1);
                Assert.assertEquals(1, manager.FreeDelayedCount());

                manager.Cancel(TimerCallback2);
                Assert.assertEquals(2, manager.FreeDelayedCount());
            }
        };

        manager.Schedule(callback, 0.25f);
        manager.Schedule(TimerCallback1, 0.25f);
        manager.Schedule(TimerCallback2, 0.25f);
        manager.Schedule(TimerCallback3, 0.25f);

        manager.Update(0.25f);
        Assert.assertEquals(0, manager.FreeDelayedCount());

        AssertCallbacks(dummyCallback, TimerCallback3);
        Assert.assertEquals(0, manager.Count());
    }

    public void testAddTimerOnce()
    {
        TestTimerManager manager = new TestTimerManager();
        manager.ScheduleOnce(TimerCallback1, 0.25f);
        manager.ScheduleOnce(TimerCallback2, 0.25f);
        manager.ScheduleOnce(TimerCallback3, 0.25f);
        manager.ScheduleOnce(TimerCallback1, 0.25f);
        manager.ScheduleOnce(TimerCallback2, 0.25f);

        Assert.assertEquals(3, manager.Count());

        manager.Update(0.25f);
        AssertCallbacks(TimerCallback1, TimerCallback2, TimerCallback3);
        Assert.assertEquals(0, manager.Count());
    }

    public void testAddDelayedTimer()
    {
        final TestTimerManager manager = new TestTimerManager();

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
                manager.Schedule(TimerCallback1, 0.25f);
                manager.Schedule(TimerCallback2, 0.25f);
                manager.Schedule(TimerCallback3, 0.25f);
            }
        };

        manager.Schedule(callback, 0.25f);
        Assert.assertEquals(1, manager.Count());

        manager.Update(0.25f); // 0.25
        AssertCallbacks(dummyCallback);
        Assert.assertEquals(3, manager.Count());

        manager.Update(0.25f); // 0.5
        AssertCallbacks(dummyCallback, TimerCallback1, TimerCallback2, TimerCallback3);
        Assert.assertEquals(0, manager.Count());
    }

    public void testWithException()
    {
        TestTimerManager manager = new TestTimerManager();

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

        manager.Schedule(callback, 0.25f);
        Assert.assertEquals(1, manager.Count());

        manager.Update(0.25f); // 0.25

        AssertCallbacks(dummyCallback);
        Assert.assertEquals(0, manager.Count());

        manager.Update(0.25f); // 0.25
        AssertCallbacks(dummyCallback);
        Assert.assertEquals(0, manager.Count());
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
        Assert.assertEquals(expected.length, actual.length);

        String message = "";
        for (int i = 0; i < expected.length; ++i)
        {
            if (expected[i] != actual[i])
            {
                message += expected[i] + "!=" + actual[i];
            }
        }

        Assert.assertTrue(message, message.length() == 0);
    }

    private Runnable[] GetCallbacks()
    {
        return callbacks.toArray(new Runnable[callbacks.size()]);
    }

    private static Timer NextTimer(Timer timer)
    {
        return timer.next;
    }
}

class TestTimerManager extends TimerManager
{
    public Timer Schedule(Runnable callback, float delay, String name)
    {
        return Schedule(callback, delay, false, name);
    }

    public int FreeDelayedCount()
    {
        int count = 0;
        for (Timer t = DelayedFreeHeadTimer(); t != null; t = Timer.NextHelperListTimer(t))
        {
            ++count;
        }

        return count;
    }
}