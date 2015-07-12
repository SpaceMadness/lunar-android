package com.spacemadness.lunar.console;

import android.support.annotation.NonNull;

import com.spacemadness.lunar.TestCaseEx;
import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.List;

public class CommandAutoCompleteTest extends TestCaseEx
{
    //////////////////////////////////////////////////////////////////////////////
    // Options single tab

    public void testSingleTabEmptyOptionsSingleChoice()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_SingleOptionsTest cmd = new cmd_SingleOptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --boolOpt ", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabEmptyOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --o";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op1", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabOptions2()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op1";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op1", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabOptions3()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op11";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op11", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabOptions4()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op111";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op111 ", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabOptions5()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op111 ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabOptions6()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op111  ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op111 ", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabBoolOption()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --bool";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --boolOpt", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabBoolOptionSingleChoice()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --boolOpt2";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --boolOpt2 ", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testValuesSingleTabNoOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testValuesSingleTabPartialName()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test v";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test val", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testValuesSingleTabPartialNameSingleChoice()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test f";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test foo ", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testValuesSingleTabPartialNameNoChoice()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test t";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, false));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Options double tab

    public void testDoubleTabEmptyOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "--boolOpt1", "--boolOpt12", "--boolOpt2", "--op1", "--op11", "--op111", "--op112", "--op113", "--op12", "--op13");
    }

    public void testDoubleTabOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --o";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op1", cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "--op1", "--op11", "--op111", "--op112", "--op113", "--op12", "--op13");
    }

    public void testDoubleTabOptions2()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op1";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op1", cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "--op1", "--op11", "--op111", "--op112", "--op113", "--op12", "--op13");
    }

    public void testDoubleTabOptions3()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op11";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op11", cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "--op11", "--op111", "--op112", "--op113");
    }

    public void testDoubleTabOptions4()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op111";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op111 ", cmd.AutoComplete(commandLine, tokens, true));
        Assert.IsNull(del.table);
    }

    public void testDoubleTabOptions5()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op111 ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, true));
        Assert.IsNull(del.table);
    }

    public void testDoubleTabOptions6()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --op111  ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --op111 ", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testDoubleTabBoolOption()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --bool";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --boolOpt", cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "--boolOpt1", "--boolOpt12", "--boolOpt2");
    }

    public void testDoubleTabBoolOptionSingleChoice()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test --boolOpt2";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test --boolOpt2 ", cmd.AutoComplete(commandLine, tokens, true));
    }

    public void testValuesDoubleTabNoOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "foo", "val1", "val12", "val123", "val2");
    }

    public void testValuesDoubleTabPartialName()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test v";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test val", cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "val1", "val12", "val123", "val2");
    }

    public void testValuesDoubleTabPartialNameSingleChoice()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test f";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test foo ", cmd.AutoComplete(commandLine, tokens, true));
        Assert.IsNull(del.table);
    }

    public void testValuesDoubleTabPartialNameNoChoice()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test t";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, true));
        Assert.IsNull(del.table);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Option single short tab

    public void testSingleTabEmptyShortOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabShortOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o1", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabShortOptions2()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o1";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o1", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabShortOptions3()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o11";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o11", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabShortOptions4()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o111";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o111 ", cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabShortOptions5()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o111 ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, false));
    }

    public void testSingleTabShortOptions6()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o111  ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o111 ", cmd.AutoComplete(commandLine, tokens, false));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Options double short tab

    public void testDoubleTabEmptyShortOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "-b1", "-b12", "-b2", "-o1", "-o11", "-o111", "-o112", "-o113", "-o12", "-o13");
    }

    public void testDoubleTabShortOptions()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o1", cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "-o1", "-o11", "-o111", "-o112", "-o113", "-o12", "-o13");
    }

    public void testDoubleTabShortOptions2()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o1";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o1", cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "-o1", "-o11", "-o111", "-o112", "-o113", "-o12", "-o13");
    }

    public void testDoubleTabShortOptions3()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o11";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o11", cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "-o11", "-o111", "-o112", "-o113");
    }

    public void testDoubleTabShortOptions4()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o111";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o111 ", cmd.AutoComplete(commandLine, tokens, true));
        Assert.IsNull(del.table);
    }

    public void testDoubleTabShortOptions5()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o111 ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, true));
        Assert.IsNull(del.table);
    }

    public void testDoubleTabShortOptions6()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsTest cmd = new cmd_OptionsTest();
        cmd.Delegate(del);

        String commandLine = "test -o111  ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        assertEquals("test -o111 ", cmd.AutoComplete(commandLine, tokens, false));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Autocomple options

    public void testOptionsValueSingleTab()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsCompleteTest cmd = new cmd_OptionsCompleteTest();
        cmd.Delegate(del);

        String commandLine = "test --opt ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, false));
        Assert.IsNull(del.table);
    }

    public void testOptionsValueDoubleTab()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsCompleteTest cmd = new cmd_OptionsCompleteTest();
        cmd.Delegate(del);

        String commandLine = "test --opt ";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "a2", "aa1", "aa11", "aa111", "aa112", "aa113", "aa12", "aa13", "b");
    }

    public void testOptionsValueSingleTab2()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsCompleteTest cmd = new cmd_OptionsCompleteTest();
        cmd.Delegate(del);

        String commandLine = "test --opt a";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, false));
        Assert.IsNull(del.table);
    }

    public void testOptionsValueDoubleTab2()
    {
        CommandDelegate del = new CommandDelegate();

        cmd_OptionsCompleteTest cmd = new cmd_OptionsCompleteTest();
        cmd.Delegate(del);

        String commandLine = "test --opt a";
        List<String> tokens = CommandTokenizer.Tokenize(commandLine);

        Assert.IsNull(cmd.AutoComplete(commandLine, tokens, true));
        assertResult(del.table, "a2", "aa1", "aa11", "aa111", "aa112", "aa113", "aa12", "aa13");
    }

    class cmd_OptionsTest extends CCommand
    {
        @CommandOption(ShortName = "o1")
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

        public cmd_OptionsTest()
        {
            RuntimeResolver.ResolveOptions(this);

            this.Values(new String[]{
                    "foo",
                    "val1",
                    "val123",
                    "val12",
                    "val2"
            });
        }

        void Execute()
        {
        }
    }

    class cmd_SingleOptionsTest extends CCommand
    {
        @CommandOption(ShortName = "b")
        public boolean boolOpt;

        public cmd_SingleOptionsTest()
        {
            RuntimeResolver.ResolveOptions(this);
        }

        void Execute()
        {
        }
    }

    class cmd_OptionsCompleteTest extends CCommand
    {
        @CommandOption(ShortName = "o", Values = "aa1,aa11,aa12,aa13,aa111,aa112,aa113,a2,b")
        public String opt;

        @CommandOption(ShortName = "a", Values = "aa1,aa11,aa12,aa13,aa111,aa112,aa113,a2,b")
        public String act;

        public cmd_OptionsCompleteTest()
        {
            RuntimeResolver.ResolveOptions(this);
        }
    }

    class CommandDelegate implements ICCommandDelegate
    {
        public String message;
        public String[] table;

        public CommandDelegate Reset()
        {
            message = null;
            table = null;

            return this;
        }

        //////////////////////////////////////////////////////////////////////////////
        // ICCommandDelegate implementation

        @Override
        public void LogTerminal(String message)
        {
            this.message = StringUtils.RemoveRichTextTags(message);
        }

        @Override
        public void LogTerminal(String[] t)
        {
            table = new String[t.length];
            for (int i = 0; i < t.length; ++i)
            {
                table[i] = StringUtils.RemoveRichTextTags(t[i]);
            }
        }

        @Override
        public void LogTerminal(Throwable e, String message)
        {
            throw new NotImplementedException();
        }

        @Override
        public void LogTerminal(TerminalEntry entry)
        {
            throw new NotImplementedException();
        }

        @Override
        public boolean ExecuteCommandLine(String commandLine, boolean manualMode)
        {
            return false;
        }

        @Override
        public boolean IsPromptEnabled()
        {
            return false;
        }
    }
}
