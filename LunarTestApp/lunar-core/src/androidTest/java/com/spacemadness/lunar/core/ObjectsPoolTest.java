package com.spacemadness.lunar.core;

import junit.framework.TestCase;

/**
 * Created by alementuev on 6/8/15.
 */
public class ObjectsPoolTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        Entry.reset();
    }

    public void testNextObject() throws Exception
    {
        ObjectsPool<Entry> pool = new ObjectsPool<>(Entry.class);
        Entry e0 = pool.NextObject();
        assertEquals(0, e0.instanceId);
        assertEquals(1, Entry.instancesCount());
        assertEquals(0, pool.size());

        Entry e1 = pool.NextObject();
        assertEquals(1, e1.instanceId);
        assertEquals(2, Entry.instancesCount());
        assertEquals(0, pool.size());

        Entry e2 = pool.NextObject();
        assertEquals(2, e2.instanceId);
        assertEquals(3, Entry.instancesCount());
        assertEquals(0, pool.size());
    }

    public void testRecycle1() throws Exception
    {
        ObjectsPool<Entry> pool = new ObjectsPool<>(Entry.class);

        Entry e0 = pool.NextObject();
        assertEquals(0, e0.instanceId);
        assertEquals(1, Entry.instancesCount());
        assertEquals(0, pool.size());
        e0.Recycle();

        assertEquals(1, pool.size());

        Entry e1 = pool.NextObject();
        assertEquals(0, e1.instanceId);
        assertEquals(1, Entry.instancesCount());
        assertEquals(0, pool.size());
        assertSame(e0, e1);
        e1.Recycle();

        assertEquals(1, pool.size());

        Entry e2 = pool.NextObject();
        assertEquals(0, e2.instanceId);
        assertEquals(1, Entry.instancesCount());
        assertEquals(0, pool.size());
        assertSame(e1, e2);
        e1.Recycle();

        assertEquals(1, pool.size());
    }

    public void testRecycle2() throws Exception
    {
        ObjectsPool<Entry> pool = new ObjectsPool<>(Entry.class);

        Entry e0 = pool.NextObject();
        assertEquals(0, e0.instanceId);
        assertEquals(1, Entry.instancesCount());
        assertEquals(0, pool.size());

        Entry e1 = pool.NextObject();
        assertEquals(1, e1.instanceId);
        assertEquals(2, Entry.instancesCount());
        assertEquals(0, pool.size());

        Entry e2 = pool.NextObject();
        assertEquals(2, e2.instanceId);
        assertEquals(3, Entry.instancesCount());
        assertEquals(0, pool.size());

        e2.Recycle();
        assertEquals(1, pool.size());

        e1.Recycle();
        assertEquals(2, pool.size());

        e0.Recycle();
        assertEquals(3, pool.size());

        Entry e3 = pool.NextObject();
        assertEquals(0, e3.instanceId);
        assertEquals(3, Entry.instancesCount());
        assertEquals(2, pool.size());
        assertSame(e0, e3);

        Entry e4 = pool.NextObject();
        assertEquals(1, e4.instanceId);
        assertEquals(3, Entry.instancesCount());
        assertEquals(1, pool.size());
        assertSame(e1, e4);

        Entry e5 = pool.NextObject();
        assertEquals(2, e5.instanceId);
        assertEquals(3, Entry.instancesCount());
        assertEquals(0, pool.size());
        assertSame(e2, e5);
    }

    public void testDestroy() throws Exception
    {
        ObjectsPool<Entry> pool = new ObjectsPool<>(Entry.class);
        Entry e0 = pool.NextObject();
        Entry e1 = pool.NextObject();
        Entry e2 = pool.NextObject();

        e0.Recycle();
        e1.Recycle();
        e2.Recycle();

        assertEquals(3, pool.size());

        pool.Destroy();
        assertEquals(0, pool.size());
    }

    private static class Entry extends ObjectsPoolEntry
    {
        private static int instancesCount;
        public final int instanceId;

        public Entry()
        {
            instanceId = instancesCount++;
        }

        public static int instancesCount()
        {
            return instancesCount;
        }

        public static void reset()
        {
            instancesCount = 0;
        }
    }
}