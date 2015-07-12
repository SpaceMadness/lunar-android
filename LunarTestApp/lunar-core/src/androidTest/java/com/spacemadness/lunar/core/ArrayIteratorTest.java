package com.spacemadness.lunar.core;

import junit.framework.TestCase;

import java.util.Iterator;

/**
 * Created by alementuev on 6/8/15.
 */
public class ArrayIteratorTest extends TestCase
{
    public void testHasNext() throws Exception
    {
        String[] arr = { "1", "2", "3" };
        Iterator<String> iter = new ArrayIterator<String>(arr);

        assertTrue(iter.hasNext());
    }

    public void testNext() throws Exception
    {
        String[] arr = { "1", "2", "3" };
        Iterator<String> iter = new ArrayIterator<String>(arr);

        assertTrue(iter.hasNext());
        assertEquals("1", iter.next());

        assertTrue(iter.hasNext());
        assertEquals("2", iter.next());

        assertTrue(iter.hasNext());
        assertEquals("3", iter.next());

        assertFalse(iter.hasNext());
    }

    public void testRemove() throws Exception
    {
        String[] arr = { "1", "2", "3" };
        Iterator<String> iter = new ArrayIterator<String>(arr);
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