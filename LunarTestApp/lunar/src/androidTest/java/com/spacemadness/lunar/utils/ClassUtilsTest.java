package com.spacemadness.lunar.utils;

import junit.framework.TestCase;

import java.lang.reflect.Method;

public class ClassUtilsTest extends TestCase
{
    public void testAs() throws Exception
    {
        Object o1 = new Object();
        Object o2 = new String();

        assertNull(ClassUtils.as(o1, String.class));
        assertSame(o2, ClassUtils.as(o2, String.class));
    }

    public void testTryNewInstance() throws Exception
    {
        assertNotNull(ClassUtils.tryNewInstance(Dummy.class));
        assertNull(ClassUtils.tryNewInstance(DummyWithArg.class));
    }

    public void testListInstanceMethods() throws Exception
    {
        Class<Dummy> dummyClass = Dummy.class;

        Method[] methods = ClassUtils.ListInstanceMethods(dummyClass, new ClassUtils.MethodFilter()
        {
            @Override
            public boolean accept(Method method)
            {
                return method.getName().equals("execute");
            }
        });

        assertEquals(3, methods.length);
        assertEquals(dummyClass.getDeclaredMethod("execute", String.class), methods[0]);
        assertEquals(dummyClass.getDeclaredMethod("execute", String[].class), methods[1]);
        assertEquals(dummyClass.getDeclaredMethod("execute"), methods[2]);
    }

    static class Dummy
    {
        protected void execute(String arg)
        {
        }

        public void execute(String[] args)
        {
        }

        private void execute()
        {
        }

        void bar()
        {
        }

        void foo(Object arg)
        {

        }
    }

    static class DummyWithArg
    {
        public DummyWithArg(int arg)
        {
        }
    }
}