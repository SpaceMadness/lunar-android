package com.spacemadness.lunar.core;

import com.spacemadness.lunar.TestCaseEx;

import java.util.List;

/**
 * Created by alementuev on 6/9/15.
 */
public class UpdatableListTest extends TestCaseEx
{
    public void testAdd()
    {
        UpdatableList list = new UpdatableList();
        list.Add(new Entry(getResultList(), "1"));
        list.Add(new Entry(getResultList(), "2"));
        list.Add(new Entry(getResultList(), "3"));

        assertEquals(3, list.Count());

        list.Update(0);

        assertResult("1", "2", "3");
    }

    public void testRemove()
    {
        UpdatableList list = new UpdatableList();
        Entry e1 = new Entry(getResultList(), "1");
        Entry e2 = new Entry(getResultList(), "2");
        Entry e3 = new Entry(getResultList(), "3");

        list.Add(e1);
        list.Add(e2);
        list.Add(e3);

        assertEquals(3, list.Count());

        list.Remove(e2);
        assertEquals(2, list.Count());

        list.Update(0);

        assertResult("1", "3");
    }

    public void testClear()
    {
        UpdatableList list = new UpdatableList();
        Entry e1 = new Entry(getResultList(), "1");
        Entry e2 = new Entry(getResultList(), "2");
        Entry e3 = new Entry(getResultList(), "3");

        list.Add(e1);
        list.Add(e2);
        list.Add(e3);
        assertEquals(3, list.Count());

        list.Clear();
        assertEquals(0, list.Count());

        list.Update(0);
        assertResult();
    }

    public void testRemoveWhileIteration()
    {
        final UpdatableList list = new UpdatableList();

        final Entry e1 = new Entry(getResultList(), "1");
        final Entry e3 = new Entry(getResultList(), "3");
        final Entry e2 = new Entry(getResultList(), "2")
        {
            @Override
            public void Update(float dt)
            {
                super.Update(dt);

                list.Remove(e3);
                assertEquals(2, list.Count());
            }
        };

        list.Add(e1);
        list.Add(e2);
        list.Add(e3);

        list.Update(0);

        assertResult("1", "2");
    }

    public void testAddWhileIteration()
    {
        final UpdatableList list = new UpdatableList();

        final Entry e1 = new Entry(getResultList(), "1");
        final Entry e3 = new Entry(getResultList(), "3");
        final Entry e2 = new Entry(getResultList(), "2")
        {
            @Override
            public void Update(float dt)
            {
                super.Update(dt);

                if (!list.Contains(e3))
                {
                    list.Add(e3);
                    assertEquals(3, list.Count());
                }
            }
        };

        list.Add(e1);
        list.Add(e2);

        list.Update(0);
        assertResult("1", "2");

        clearResult();

        list.Update(0);
        assertResult("1", "2", "3");
    }

    public void testAddAndRemoveWhileIteration()
    {
        final UpdatableList list = new UpdatableList();

        final Entry e1 = new Entry(getResultList(), "1");
        final Entry e2 = new Entry(getResultList(), "2");
        final Entry e3 = new Entry(getResultList(), "3");
        final Entry e4 = new Entry(getResultList(), "4");

        final Entry addRemoveEntry = new Entry(getResultList(), "add_remove")
        {
            @Override
            public void Update(float dt)
            {
                super.Update(dt);

                list.Remove(e1);
                list.Remove(e2);
                assertEquals(1, list.Count());

                list.Add(e3);
                list.Add(e4);
                assertEquals(3, list.Count());

                list.Remove(this);
                assertEquals(2, list.Count());
            }
        };

        list.Add(e1);
        list.Add(e2);
        list.Add(addRemoveEntry);

        list.Update(0);
        assertResult("1", "2", "add_remove");

        clearResult();

        list.Update(0);
        assertResult("3", "4");
    }

    public void testClearWhileIteration()
    {
        final UpdatableList list = new UpdatableList();

        final Entry e1 = new Entry(getResultList(), "1");
        final Entry e2 = new Entry(getResultList(), "2");
        final Entry e3 = new Entry(getResultList(), "3");
        final Entry clearEntry = new Entry(getResultList(), "clear")
        {
            @Override
            public void Update(float dt)
            {
                super.Update(dt);
                list.Clear();
                assertEquals(0, list.Count());
            }
        };

        list.Add(e1);
        list.Add(clearEntry);
        list.Add(e2);
        list.Add(e3);

        list.Update(0);
        assertResult("1", "clear");

        clearResult();

        list.Update(0);
        assertResult();
    }

    private static class Entry implements IUpdatable
    {
        private final List<String> result;
        private final String name;

        Entry(List<String> result, String name)
        {
            this.result = result;
            this.name = name;
        }

        @Override
        public void Update(float dt)
        {
            result.add(name);
        }
    }
}