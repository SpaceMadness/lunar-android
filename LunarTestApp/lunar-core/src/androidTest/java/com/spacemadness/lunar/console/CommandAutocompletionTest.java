package com.spacemadness.lunar.console;

import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.console.commands.Cmd_alias;
import com.spacemadness.lunar.utils.StringUtils;

public class CommandAutocompletionTest extends CCommandTestCase
{
    //////////////////////////////////////////////////////////////////////////////
    // Commands

    public void testCommandsSuggestion()
    {
        assertSuggestions("¶", //
                "Alias1",//
                "Alias2",//
                "Alias3",//
                "Var1",  //
                "Var12", //
                "Var2",  //
                "test1", //
                "test2", //
                "test3"
        );
    }

    public void testCommandsSuggestionFiltered1()
    {
        assertSuggestions("a¶", //
                "Alias1",//
                "Alias2",//
                "Alias3"
        );
    }

    public void testCommandsSuggestionFiltered2()
    {
        assertSuggestions("t¶", //
                "test1", //
                "test2", //
                "test3"
        );
    }

    public void testCommandsSuggestionFiltered3()
    {
        assertSuggestions("v¶", //
                "Var1",  //
                "Var12", //
                "Var2"
        );
    }

    public void testCommandsSuggestionFiltered4()
    {
        assertSuggestions("var1¶", //
                "Var1",  //
                "Var12"
        );
    }

    public void testCommandsSuggestionFiltered5()
    {
        assertSuggestions("var12¶", //
                "Var12"
        );
    }

    public void testCommandsSuggestionFiltered6()
    {
        assertSuggestions("Var123¶");
    }

    //////////////////////////////////////////////////////////////////////////////
    // Options

    public void testOptionsSuggestion()
    {
        assertSuggestions("test1 --¶", //
                "--boolOpt1", //
                "--boolOpt12", //
                "--boolOpt2", //
                "--op1", //
                "--op11", //
                "--op111", //
                "--op112", //
                "--op113", //
                "--op12", //
                "--op13"
        );
    }

    public void testOptionsSuggestionMultiple()
    {
        assertSuggestions("test1 --boolOpt1 --¶", //
                "--boolOpt1", //
                "--boolOpt12", //
                "--boolOpt2", //
                "--op1", //
                "--op11", //
                "--op111", //
                "--op112", //
                "--op113", //
                "--op12", //
                "--op13"
        );
    }

    public void testOptionsSuggestionMultipleWithValue()
    {
        assertSuggestions("test1 --boolOpt1 --op1 value1 --¶", //
                "--boolOpt1", //
                "--boolOpt12", //
                "--boolOpt2", //
                "--op1", //
                "--op11", //
                "--op111", //
                "--op112", //
                "--op113", //
                "--op12", //
                "--op13"
        );
    }

    public void testOptionsSuggestionMultipleWithIncorrectValue()
    {
        assertSuggestions("test1 --boolOpt1 --op1 foo --¶", //
                "--boolOpt1", //
                "--boolOpt12", //
                "--boolOpt2", //
                "--op1", //
                "--op11", //
                "--op111", //
                "--op112", //
                "--op113", //
                "--op12", //
                "--op13"
        );
    }

    public void testOptionsSuggestionMultipleWithArg()
    {
        assertSuggestions("test1 --boolOpt1 --op12 arg --¶", //
                "--boolOpt1", //
                "--boolOpt12", //
                "--boolOpt2", //
                "--op1", //
                "--op11", //
                "--op111", //
                "--op112", //
                "--op113", //
                "--op12", //
                "--op13"
        );
    }

    public void testOptionsSuggestionFiltered1()
    {
        assertSuggestions("test1 --b¶", //
                "--boolOpt1", //
                "--boolOpt12", //
                "--boolOpt2"
        );
    }

    public void testOptionsSuggestionFiltered2()
    {
        assertSuggestions("test1 --boolOpt1¶", //
                "--boolOpt1", //
                "--boolOpt12"
        );
    }

    public void testOptionsSuggestionFiltered3()
    {
        assertSuggestions("test1 --boolOpt12¶", //
                "--boolOpt12"
        );
    }

    public void testOptionsSuggestionFiltered4()
    {
        assertSuggestions("test2 --boolOpt ¶");
    }

    public void testOptionsSuggestionFiltered5()
    {
        assertSuggestions("test1 --boolOpt123¶");
    }

    public void testOptionsSuggestionFilteredWithArgs()
    {
        assertSuggestions("test1 --boolOpt12 ¶", //
                "foo", //
                "val1", //
                "val12", //
                "val123", //
                "val2" //
        );
    }

    public void testOptionValuesSuggestion()
    {
        assertSuggestions("test3 --opt ¶", //
                "a2", "aa1", "aa11", "aa111", "aa112", "aa113", "aa12", "aa13", "b"
        );
    }

    public void testOptionValuesSuggestionFiltered1()
    {
        assertSuggestions("test3 --opt a¶", //
                "a2", "aa1", "aa11", "aa111", "aa112", "aa113", "aa12", "aa13"
        );
    }

    public void testOptionValuesSuggestionFiltered2()
    {
        assertSuggestions("test3 --opt aa111¶", //
                "aa111"
        );
    }

    public void testOptionValuesSuggestionFilteredWithArgs1()
    {
        assertSuggestions("test3 --opt aa111 ¶",
                "arg1", //
                "arg12", //
                "arg2"
        );
    }

    public void testOptionValuesSuggestionFilteredWithArgs2()
    {
        assertSuggestions("test3 --opt aa111 arg1¶",
                "arg1", //
                "arg12"
        );
    }

    //////////////////////////////////////////////////////////////////////////////
    // Lifecycle

    @Override
    protected void runSetup()
    {
        super.runSetup();

        StringUtils.colorsDisabled = true;

        new CVar("Var1", false);
        new CVar("Var12", false);
        new CVar("Var2", false);

        registerCommand(Cmd_alias.class);

        registerCommand(Cmd_test1.class, false);
        registerCommand(Cmd_test2.class, false);
        registerCommand(Cmd_test3.class, false);


        execute("alias Alias1 test1");
        execute("alias Alias2 test2");
        execute("alias Alias3 test3");
    }

    @Override
    protected void tearDown() throws Exception
    {
        StringUtils.colorsDisabled = false;

        super.tearDown();
    }


    //////////////////////////////////////////////////////////////////////////////
    // Custom commands

    static class Cmd_test1 extends CCommand
    {
        @CommandOption(ShortName = "o1", Values = "value1,value12,value2")
        public String op1;

        @CommandOption(ShortName = "o11")
        public String op11;

        @CommandOption(ShortName = "o12")
        public String op12;

        @CommandOption(ShortName = "o13")
        public String op13;

        @CommandOption(ShortName = "o111")
        public String op111;

        @CommandOption(ShortName = "o112")
        public String op112;

        @CommandOption(ShortName = "o113")
        public String op113;

        @CommandOption(ShortName = "b1")
        public boolean boolOpt1;

        @CommandOption(ShortName = "b12")
        public boolean boolOpt12;

        @CommandOption(ShortName = "b2")
        public boolean boolOpt2;

        public Cmd_test1()
        {
            RuntimeResolver.ResolveOptions(this);

            this.Values("foo", "val1", "val123", "val12", "val2");
        }

        void Execute()
        {
        }
    }

    static class Cmd_test2 extends CCommand
    {
        @CommandOption(ShortName = "b")
        public boolean boolOpt;

        public Cmd_test2()
        {
            RuntimeResolver.ResolveOptions(this);
        }

        void Execute()
        {
        }
    }

    static class Cmd_test3 extends CCommand
    {
        @CommandOption(ShortName = "o", Values = "aa1,aa11,aa12,aa13,aa111,aa112,aa113,a2,b")
        public String opt;

        @CommandOption(ShortName = "a", Values = "aa1,aa11,aa12,aa13,aa111,aa112,aa113,a2,b")
        public String act;

        public Cmd_test3()
        {
            RuntimeResolver.ResolveOptions(this);

            this.Values("arg1", "arg12", "arg2");
        }
    }
}