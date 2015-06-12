package com.spacemadness.lunar.console;

import junit.framework.TestCase;

import static com.spacemadness.lunar.console.CCommandUtils.CanInvokeMethodWithArgsCount;

public class CCommandUtilsTest extends TestCase {

    public void testCanInvokeMethodWithArgsCount() throws Exception
    {
        Class<Dummy> dummyClass = Dummy.class;

        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute"), 0));
        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", String.class), 1));
        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", String.class, String.class), 2));

        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", String[].class), 0));
        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", String[].class), 1));
        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", String[].class), 2));

        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", int.class, String[].class), 1));
        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", int.class, String[].class), 2));
        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", int.class, String[].class), 3));

        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", boolean.class, String[].class), 1));
        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", boolean.class, String[].class), 2));
        assertTrue(CanInvokeMethodWithArgsCount(dummyClass.getDeclaredMethod("canExecute", boolean.class, String[].class), 3));
    }

    public void testInvoke() throws Exception {

    }

    public void testGetMethodParamsUsage() throws Exception {

    }

    public void testResolveInvokeParameters() throws Exception {

    }

    public void testNextIntArg() throws Exception {

    }

    public void testNextFloatArg() throws Exception {

    }

    public void testNextBoolArg() throws Exception {

    }

    public void testNextArg() throws Exception {

    }

    public void testIsValidArg() throws Exception {

    }

    private static class Dummy
    {
        boolean canExecute()
        {
            return false;
        }

        boolean canExecute(String arg)
        {
            return false;
        }

        boolean canExecute(String arg1, String arg2)
        {
            return false;
        }

        boolean canExecute(String[] args)
        {
            return false;
        }

        boolean canExecute(int arg1, String[] args)
        {
            return false;
        }

        boolean canExecute(boolean arg1, String... args)
        {
            return false;
        }
    }
}