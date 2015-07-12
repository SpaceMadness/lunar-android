package com.spacemadness.lunar.utils;

import junit.framework.TestCase;

public class ObjectUtilsTest extends TestCase
{
    public void testAreEqual() throws Exception
    {
        String s1 = "Some string";
        String s2 = "Some string";

        assertTrue(ObjectUtils.areEqual(s1, s2));
    }

    public void testAreEqualNullAndNotNull() throws Exception
    {
        String s1 = null;
        String s2 = "Some string";

        assertFalse(ObjectUtils.areEqual(s1, s2));
    }

    public void testAreEqualNotNullAndNull() throws Exception
    {
        String s1 = "Some string";
        String s2 = null;

        assertFalse(ObjectUtils.areEqual(s1, s2));
    }

    public void testAreEqualNullAndNull() throws Exception
    {
        String s1 = null;
        String s2 = null;

        assertTrue(ObjectUtils.areEqual(s1, s2));
    }

    public void testNotNull() throws Exception
    {
        Object obj = new Object();
        Object objDefault = new Object();

        assertSame(obj, ObjectUtils.notNullOrDefault(obj, objDefault));
        assertSame(objDefault, ObjectUtils.notNullOrDefault(null, objDefault));
    }
}