package com.spacemadness.lunar.console;

import com.spacemadness.lunar.Result;
import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.utils.ArrayUtils;

import java.util.List;

public class CommandOptionsTest extends CCommandTestCase
{
    //////////////////////////////////////////////////////////////////////////////
    // List options

    public void testListOptions()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListOptions(cmd);
        assertResult(names, "op1", "op12", "op123");
    }

    public void testListOptionsWithToken()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListOptions(cmd, "o");
        assertResult(names, "op1", "op12", "op123");
    }

    public void testListOptionsWithToken2()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListOptions(cmd, "op");
        assertResult(names, "op1", "op12", "op123");
    }

    public void testListOptionsWithToken3()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListOptions(cmd, "op1");
        assertResult(names, "op1", "op12", "op123");
    }

    public void testListOptionsWithToken4()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListOptions(cmd, "op12");
        assertResult(names, "op12", "op123");
    }

    public void testListOptionsWithToken5()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListOptions(cmd, "op123");
        assertResult(names, "op123");
    }

    public void testListOptionsWithToken6()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListOptions(cmd, "op1234");
        assertResult(names);
    }

    public void testListOptionsWithToken7()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListOptions(cmd, "");
        assertResult(names, "op1", "op12", "op123");
    }

    //////////////////////////////////////////////////////////////////////////////
    // List short options

    public void testListShortOptions()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListShortOptions(cmd);
        assertResult(names, "o1", "o12", "o123");
    }

    public void testListShortOptionsWithToken()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListShortOptions(cmd, "o");
        assertResult(names, "o1", "o12", "o123");
    }

    public void testListShortOptionsWithToken2()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListShortOptions(cmd, "o1");
        assertResult(names, "o1", "o12", "o123");
    }

    public void testListShortOptionsWithToken3()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListShortOptions(cmd, "o12");
        assertResult(names, "o12", "o123");
    }

    public void testListShortOptionsWithToken4()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListShortOptions(cmd, "o123");
        assertResult(names, "o123");
    }

    public void testListShortOptionsWithToken5()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListShortOptions(cmd, "o1234");
        assertResult(names);
    }

    public void testListShortOptionsWithToken6()
    {
        CCommand cmd = new cmd_test_list();
        String[] names = ListShortOptions(cmd, "");
        assertResult(names, "o1", "o12", "o123");
    }

    //////////////////////////////////////////////////////////////////////////////
    // Settings options

    public void testOptions()
    {
        List<String> argsList = ArrayUtils.toList(
            "test",
            "--privateStr", "test",
            "--privateInt", "10",
            "--privateFloat", "3.14",
            "--privateBool",
            "--publicStr", "test 2",
            "--publicInt", "20",
            "--publicFloat", "6.28",
            "--publicBool",
            "--ints", "1", "2",
            "--floats", "3.14", "-3.14",
            "--bools", "yes", "no",
            "--strings", "A", "B",
            "-s", "test 3"
        );

        final Result delegateCalled = new Result();
        CCommand cmd = new cmd_test(new CommandActionEx()
        {
            @Override
            public void onExecute(CCommand command, String[] args)
            {
                cmd_test test = (cmd_test)command;
                assertEquals(test.privateStr(), "test");
                assertEquals(test.privateInt(), 10);
                assertEquals(test.privateFloat(), 3.14f);
                assertEquals(test.privateBool(), true);
                assertEquals(test.publicStr, "test 2");
                assertEquals(test.publicInt, 20);
                assertEquals(test.publicFloat, 6.28f);
                assertEquals(test.publicBool, true);
                assertResult(test.ints, 1, 2);
                assertResult(test.floats, 3.14f, -3.14f);
                assertResult(test.bools, true, false);
                assertResult(test.strings, "A", "B");
                assertEquals(test.shortie, "test 3");
                delegateCalled.value = true;
            }
        });
        cmd.ExecuteTokens(argsList);
        assertTrue(delegateCalled.value);
    }

    public void testEmptyOptionValue()
    {
        List<String> argsList = ArrayUtils.toList(
            "test",
            "-s", "",
            "arg1 with space",
            "arg2",
            "arg3"
        );

        final Result delegateCalled = new Result();
        CCommand cmd = new cmd_test(new CommandActionEx()
        {
            @Override
            public void onExecute(CCommand command, String[] args)
            {
                cmd_test test = (cmd_test)command;
                assertEquals(test.shortie, "");
                assertResult(args, "arg1 with space", "arg2", "arg3");
                delegateCalled.value = true;
            }
        });
        cmd.ExecuteTokens(argsList);
        assertTrue(delegateCalled.value);
    }

    public void testDefaultOptionsValues()
    {
        List<String> argsList = ArrayUtils.toList("test_default");

        final Result delegateCalled = new Result();
        cmd_test_default cmd = new cmd_test_default();
        cmd.ExecutionDelegate = new CommandActionEx()
        {
            @Override
            public void onExecute(CCommand command, String[] args)
            {
                cmd_test_default test = (cmd_test_default)command;
                assertEquals(test.s, "String");
                assertEquals(test.i, 10);
                assertEquals(test.f, 3.14f);
                assertEquals(test.b, true);
                assertResult(test.ints, 10, 20);
                assertResult(test.floats, 3.14f, -3.14f);
                assertResult(test.bools, true, false);
                assertResult(test.strings, "one", "two");
                delegateCalled.value = true;
            }
        };

        cmd.ExecuteTokens(argsList);
        assertTrue(delegateCalled.value);

        delegateCalled.value = false;
        cmd.ExecuteTokens(argsList);

        assertTrue(delegateCalled.value);
    }

    public void testOverrideDefaultOptions()
    {
        cmd_test_default cmd = new cmd_test_default();

        List<String> argsList = ArrayUtils.toList(
            "test_default",
            "--s", "foo",
            "--i", "20",
            "--f", "6.28",
            "--ints", "30", "40",
            "--floats", "6.28", "-6.28",
            "--bools", "false", "true",
            "--strings", "three", "four"
        );

        final Result delegateCalled = new Result();
        cmd.ExecutionDelegate = new CommandActionEx()
        {
            @Override
            public void onExecute(CCommand command, String[] args)
            {
                cmd_test_default test = (cmd_test_default)command;
                assertEquals(test.s, "foo");
                assertEquals(test.i, 20);
                assertEquals(test.f, 6.28f);
                assertResult(test.ints, 30, 40);
                assertResult(test.floats, 6.28f, -6.28f);
                assertResult(test.bools, false, true);
                assertResult(test.strings, "three", "four");
                delegateCalled.value = true;
            }
        };

        cmd.ExecuteTokens(argsList);
        assertTrue(delegateCalled.value);
        argsList = ArrayUtils.toList("test_default");
        cmd.ExecutionDelegate = new CommandActionEx()
        {
            @Override
            public void onExecute(CCommand command, String[] args)
            {
                cmd_test_default test = (cmd_test_default)command;
                assertEquals(test.s, "String");
                assertEquals(test.i, 10);
                assertEquals(test.f, 3.14f);
                assertEquals(test.b, true);
                assertResult(test.ints, 10, 20);
                assertResult(test.floats, 3.14f, -3.14f);
                assertResult(test.bools, true, false);
                assertResult(test.strings, "one", "two");
                delegateCalled.value = true;
            }
        };

        delegateCalled.value = false;
        cmd.ExecuteTokens(argsList);
        assertTrue(delegateCalled.value);

        argsList = ArrayUtils.toList(
            "test_default",
            // "--s", "foo",
            "--i", "20",
            // "--f", "6.28",
            "--ints", "30", "40",
            // "--floats", "6.28", "-6.28",
            "--bools", "false", "true"
            // "--strings", "three", "four"
        );

        cmd.ExecutionDelegate = new CommandActionEx()
        {
            @Override
            public void onExecute(CCommand command, String[] args)
            {
                cmd_test_default test = (cmd_test_default)command;
                assertEquals(test.s, "String");
                assertEquals(test.i, 20);
                assertEquals(test.f, 3.14f);
                assertResult(test.ints, 30, 40);
                assertResult(test.floats, 3.14f, -3.14f);
                assertResult(test.bools, false, true);
                assertResult(test.strings, "one", "two");
                delegateCalled.value = true;
            }
        };

        delegateCalled.value = false;
        cmd.ExecuteTokens(argsList);
        assertTrue(delegateCalled.value);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Find options

    public void testFindNonAmbiguousOption1()
    {
        CCommand cmd = new cmd_test_list();
        assertNull(cmd.FindNonAmbiguousOption("op1", false));
    }

    public void testFindNonAmbiguousOption2()
    {
        CCommand cmd = new cmd_test_list();
        assertNull(cmd.FindNonAmbiguousOption("op12", false));
    }

    public void testFindNonAmbiguousOption3()
    {
        CCommand cmd = new cmd_test_list();
        CCommand.Option opt = cmd.FindNonAmbiguousOption("op123", false);

        assertEquals("op123", opt.Name);
    }

    public void testFindNonAmbiguousShortOption1()
    {
        CCommand cmd = new cmd_test_list();
        assertNull(cmd.FindNonAmbiguousOption("o1", true));
    }

    public void testFindNonAmbiguousShortOption2()
    {
        CCommand cmd = new cmd_test_list();
        assertNull(cmd.FindNonAmbiguousOption("o12", true));
    }

    public void testFindNonAmbiguousShortOption3()
    {
        CCommand cmd = new cmd_test_list();
        CCommand.Option opt = cmd.FindNonAmbiguousOption("o123", true);

        assertEquals("o123", opt.ShortName);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    private String[] ListOptions(CCommand cmd)
    {
        return ListOptions(cmd, null);
    }

    private String[] ListOptions(CCommand cmd, String token)
    {
        List<CCommand.Option> options = cmd.ListOptions(token);
        String[] names = new String[options.size()];
        for (int i = 0; i < options.size(); ++i)
        {
            names[i] = options.get(i).Name;
        }

        return names;
    }

    private String[] ListShortOptions(CCommand cmd)
    {
        return ListShortOptions(cmd, null);
    }

    private String[] ListShortOptions(CCommand cmd, String token)
    {
        List<CCommand.Option> options = cmd.ListShortOptions(token);
        String[] names = new String[options.size()];
        for (int i = 0; i < options.size(); ++i)
        {
            names[i] = options.get(i).ShortName;
        }

        return names;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Private classes

    interface CommandActionEx
    {
        void onExecute(CCommand cmd, String[] args);
    }

    class cmd_test extends CCommand
    {
        private CommandActionEx m_delegate;

        @CommandOption(Name = "privateStr")
        private String m_privateStr;

        @CommandOption(Name = "privateInt")
        private int m_privateInt;

        @CommandOption(Name = "privateFloat")
        private float m_privateFloat;

        @CommandOption(Name = "privateBool")
        private boolean m_privateBool;

        @CommandOption()
        public String publicStr;

        @CommandOption()
        public int publicInt;

        @CommandOption()
        public float publicFloat;

        @CommandOption()
        public boolean publicBool;

        @CommandOption()
        public int[] ints = new int[2];

        @CommandOption()
        public float[] floats = new float[2];

        @CommandOption()
        public boolean[] bools = new boolean[2];

        @CommandOption()
        public String[] strings = new String[2];

        @CommandOption(ShortName="s")
        public String shortie;

        public cmd_test(CommandActionEx del)
        {
            m_delegate = del;
            RuntimeResolver.ResolveOptions(this);
        }

        void execute(String[] args)
        {
            m_delegate.onExecute(this, args);
        }

        public String privateStr() { return m_privateStr; }
        public int privateInt() { return m_privateInt; }
        public float privateFloat() { return m_privateFloat; }
        public boolean privateBool() { return m_privateBool; }
    }

    class cmd_test_default extends CCommand
    {
        @CommandOption()
        public String s = "String";

        @CommandOption()
        public int i = 10;

        @CommandOption()
        public float f = 3.14f;

        @CommandOption()
        public boolean b = true;

        @CommandOption()
        public int[] ints = { 10, 20 };

        @CommandOption()
        public float[] floats = { 3.14f, -3.14f };

        @CommandOption()
        public boolean[] bools = { true, false };

        @CommandOption()
        public String[] strings = { "one", "two" };

        public cmd_test_default()
        {
            RuntimeResolver.ResolveOptions(this);
        }

        void execute(String[] args)
        {
            ExecutionDelegate.onExecute(this, args);
        }

        public CommandActionEx ExecutionDelegate;
    }

    class cmd_test_list extends CCommand
    {
        @CommandOption(ShortName="o1")
        public String op1;

        @CommandOption(ShortName="o12")
        public String op12;

        @CommandOption(ShortName="o123")
        public String op123;

        public cmd_test_list()
        {
            RuntimeResolver.ResolveOptions(this);
        }

        void execute()
        {
        }
    }
}
