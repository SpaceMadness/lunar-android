package com.spacemadness.lunar.utils;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

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

    public void testlistFields()
    {
        List<Field> fields;

        fields = ClassUtils.listFields(A.class);

        assertEquals(4, fields.size());
        assertEquals("a1", fields.get(0).getName());
        assertEquals("a2", fields.get(1).getName());
        assertEquals("a3", fields.get(2).getName());
        assertEquals("a4", fields.get(3).getName());

        fields = ClassUtils.listFields(B.class);

        assertEquals(4, fields.size());
        assertEquals("b1", fields.get(0).getName());
        assertEquals("b2", fields.get(1).getName());
        assertEquals("b3", fields.get(2).getName());
        assertEquals("b4", fields.get(3).getName());

        fields = ClassUtils.listFields(C.class);

        assertEquals(4, fields.size());
        assertEquals("c1", fields.get(0).getName());
        assertEquals("c2", fields.get(1).getName());
        assertEquals("c3", fields.get(2).getName());
        assertEquals("c4", fields.get(3).getName());
    }

    public void testlistFieldsRecursive()
    {
        List<Field> fields;

        fields = ClassUtils.listFields(A.class, true);

        assertEquals(4, fields.size());
        assertEquals("a1", fields.get(0).getName());
        assertEquals("a2", fields.get(1).getName());
        assertEquals("a3", fields.get(2).getName());
        assertEquals("a4", fields.get(3).getName());

        fields = ClassUtils.listFields(B.class, true);

        assertEquals(8, fields.size());
        assertEquals("b1", fields.get(0).getName());
        assertEquals("b2", fields.get(1).getName());
        assertEquals("b3", fields.get(2).getName());
        assertEquals("b4", fields.get(3).getName());
        assertEquals("a1", fields.get(4).getName());
        assertEquals("a2", fields.get(5).getName());
        assertEquals("a3", fields.get(6).getName());
        assertEquals("a4", fields.get(7).getName());

        fields = ClassUtils.listFields(C.class, true);

        assertEquals(12, fields.size());
        assertEquals("c1", fields.get(0).getName());
        assertEquals("c2", fields.get(1).getName());
        assertEquals("c3", fields.get(2).getName());
        assertEquals("c4", fields.get(3).getName());
        assertEquals("b1", fields.get(4).getName());
        assertEquals("b2", fields.get(5).getName());
        assertEquals("b3", fields.get(6).getName());
        assertEquals("b4", fields.get(7).getName());
        assertEquals("a1", fields.get(8).getName());
        assertEquals("a2", fields.get(9).getName());
        assertEquals("a3", fields.get(10).getName());
        assertEquals("a4", fields.get(11).getName());
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

    static class A
    {
        private int a1;
        int a2;
        protected int a3;
        public int a4;
    }

    static class B extends A
    {
        private int b1;
        int b2;
        protected int b3;
        public int b4;
    }

    static class C extends B
    {
        private int c1;
        int c2;
        protected int c3;
        public int c4;
    }
}