package com.spacemadness.lunar.core;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrayListIteratorTest extends TestCase
{
    public void testHasNext() throws Exception
    {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("1");
        arr.add("2");
        arr.add("3");

        Iterator<String> iter = new ArrayListIterator<>(arr);
        assertTrue(iter.hasNext());
    }

    public void testNext() throws Exception
    {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("1");
        arr.add("2");
        arr.add("3");

        Iterator<String> iter = new ArrayListIterator<>(arr);

        assertTrue(iter.hasNext());
        assertEquals("1", iter.next());

        assertTrue(iter.hasNext());
        assertEquals("2", iter.next());

        assertTrue(iter.hasNext());
        assertEquals("3", iter.next());

        assertFalse(iter.hasNext());
    }

    public void testPosition() throws Exception
    {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("1");
        arr.add("2");
        arr.add("3");

        ArrayListIterator<String> iter = new ArrayListIterator<>(arr);

        assertTrue(iter.hasNext());
        assertEquals("1", iter.next());

        int position = iter.getPosition();

        assertTrue(iter.hasNext());
        assertEquals("2", iter.next());

        assertTrue(iter.hasNext());
        assertEquals("3", iter.next());

        assertFalse(iter.hasNext());

        iter.setPosition(position);

        assertTrue(iter.hasNext());
        assertEquals("2", iter.next());

        assertTrue(iter.hasNext());
        assertEquals("3", iter.next());

        assertFalse(iter.hasNext());
    }

    public void testRemove() throws Exception
    {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("1");
        arr.add("2");
        arr.add("3");

        Iterator<String> iter = new ArrayListIterator<>(arr);
        try
        {
            iter.remove();
            fail("UnsupportedOperationException should be thrown");
        }
        catch (UnsupportedOperationException e)
        {
        }
    }
}