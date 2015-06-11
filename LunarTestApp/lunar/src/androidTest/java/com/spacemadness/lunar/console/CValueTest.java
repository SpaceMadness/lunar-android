package com.spacemadness.lunar.console;

import junit.framework.TestCase;

public class CValueTest extends TestCase
{
    public void testEquals() throws Exception
    {
        CValue value1 = new CValue();
        value1.stringValue = "3.14";
        value1.intValue = 3;
        value1.floatValue = 3.14f;

        CValue value2 = new CValue();
        value2.stringValue = "3.14";
        value2.intValue = 3;
        value2.floatValue = 3.14f;

        assertEquals(value1, value2);
    }
}