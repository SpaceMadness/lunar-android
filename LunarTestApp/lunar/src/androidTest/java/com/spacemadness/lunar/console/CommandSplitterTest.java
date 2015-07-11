package com.spacemadness.lunar.console;

import com.spacemadness.lunar.TestCaseEx;

import java.util.List;

import static com.spacemadness.lunar.console.CommandSplitter.OPTION_IGNORE_MISSING_QUOTES;

public class CommandSplitterTest extends TestCaseEx
{
    //////////////////////////////////////////////////////////////////////////////
    // Single command

    public void testSingleCommandSplit()
    {
        List<String> commands = CommandSplitter.Split("test");
        assertResult(commands, "test");
    }

    public void testSingleCommandWithArgSplit()
    {
        List<String> commands = CommandSplitter.Split("test --arg");
        assertResult(commands, "test --arg");
    }

    public void testSingleCommandWithArgsSplit()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 --arg2");
        assertResult(commands, "test --arg1 --arg2");
    }

    public void testSingleCommandWithArgsAndQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 \"argument && quotes\"");
        assertResult(commands, "test --arg1 \"argument && quotes\"");
    }

    public void testSingleCommandWithArgsAndQuotesSplitMissingQuotes()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 \"argument && quotes", OPTION_IGNORE_MISSING_QUOTES);
        assertResult(commands, "test --arg1 \"argument && quotes");
    }

    public void testSingleCommandWithArgsAndSingleQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 'argument && quotes'");
        assertResult(commands, "test --arg1 'argument && quotes'");
    }

    public void testSingleCommandWithArgsAndSingleQuotesSplitMissingQuotes()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 'argument && quotes", OPTION_IGNORE_MISSING_QUOTES);
        assertResult(commands, "test --arg1 'argument && quotes");
    }

    public void testSingleCommandWithArgsQuotesAndInnerQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 \"argument \\\"&&\\\" quotes\"");
        assertResult(commands, "test --arg1 \"argument \\\"&&\\\" quotes\"");
    }

    public void testSingleCommandWithArgsSingleQuotesAndInnerQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 'argument \"&&\" quotes'");
        assertResult(commands, "test --arg1 'argument \"&&\" quotes'");
    }

    public void testSingleCommandWithArgsSingleQuotesAndInnerQuotesSplitMissingQuotes()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 'argument \"&&\" quotes", OPTION_IGNORE_MISSING_QUOTES);
        assertResult(commands, "test --arg1 'argument \"&&\" quotes");
    }

    public void testSingleCommandWithArgsQuotesAndInnerSingleQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 \"argument '&&' quotes\"");
        assertResult(commands, "test --arg1 \"argument '&&' quotes\"");
    }

    //////////////////////////////////////////////////////////////////////////////
    // Multiple commands

    public void testMultipleCommandsSplit()
    {
        List<String> commands = CommandSplitter.Split("test1 && test2");
        assertResult(commands, "test1", "test2");
    }

    public void testMultipleCommandsWithArgSplit()
    {
        List<String> commands = CommandSplitter.Split("test1 --arg1 && test2 --arg2");
        assertResult(commands, "test1 --arg1", "test2 --arg2");
    }

    public void testMultipleCommandsWithArgsSplit()
    {
        List<String> commands = CommandSplitter.Split("test1 --arg1 --arg2 && test2 --arg3 --arg4");
        assertResult(commands, "test1 --arg1 --arg2", "test2 --arg3 --arg4");
    }

    public void testMultipleCommandsWithArgsAndQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test1 --arg1 \"a1 && a2\" && test2 --arg2 \"b1 && b2\"");
        assertResult(commands, "test1 --arg1 \"a1 && a2\"", "test2 --arg2 \"b1 && b2\"");
    }

    public void testMultipleCommandsWithArgsAndSingleQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test1 --arg1 'a1 && a2' && test2 --arg2 'b1 && b2'");
        assertResult(commands, "test1 --arg1 'a1 && a2'", "test2 --arg2 'b1 && b2'");
    }

    public void testMultipleCommandsWithArgsAndSingleQuotesSplitMissingQuote()
    {
        List<String> commands = CommandSplitter.Split("test1 --arg1 'a1 && a2' && test2 --arg2 'b1 && b2", OPTION_IGNORE_MISSING_QUOTES);
        assertResult(commands, "test1 --arg1 'a1 && a2'", "test2 --arg2 'b1 && b2");
    }

    public void testMultipleCommandsWithArgsQuotesAndInnerQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test1 --arg1 \"a1 \\\"&&\\\" a2\" && test2 --arg2 \"b1 \\\"&&\\\" b2\"");
        assertResult(commands, "test1 --arg1 \"a1 \\\"&&\\\" a2\"", "test2 --arg2 \"b1 \\\"&&\\\" b2\"");
    }

    public void testMultipleCommandsWithArgsQuotesAndInnerSingleQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test --arg1 \"argument '&&' quotes\"");
        assertResult(commands, "test --arg1 \"argument '&&' quotes\"");
    }

    public void testMultipleCommandsWithArgsSingleQuotesAndInnerQuotesSplit()
    {
        List<String> commands = CommandSplitter.Split("test1 --arg1 'a1 \"&&\" a2' && test2 --arg2 'b1 \"&&\" b2'");
        assertResult(commands, "test1 --arg1 'a1 \"&&\" a2'", "test2 --arg2 'b1 \"&&\" b2'");
    }

    public void testMultipleCommandsWithNoSpaces()
    {
        List<String> commands = CommandSplitter.Split("test1&&test2");
        assertResult(commands, "test1", "test2");
    }

    public void testMultipleCommandsWithNoSpacesAndArgs()
    {
        List<String> commands = CommandSplitter.Split("test1 --arg1&&test2 --arg2");
        assertResult(commands, "test1 --arg1", "test2 --arg2");
    }

    public void testMoar()
    {
        List<String> commands = CommandSplitter.Split("bind t 'echo \"test-1\" && echo \"test-2\"'");
        assertResult(commands, "bind t 'echo \"test-1\" && echo \"test-2\"'");
    }
}