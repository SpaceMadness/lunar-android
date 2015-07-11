package com.spacemadness.lunar.console;

import com.spacemadness.lunar.TestCaseEx;

import java.util.List;

import static com.spacemadness.lunar.console.CommandTokenizer.OPTION_IGNORE_MISSING_QUOTES;

public class CommandTokenizerTest extends TestCaseEx
{
    //////////////////////////////////////////////////////////////////////////////
    // Tokenizer

    public void testTokenize()
    {
        String str = "one two three";

        List<String> tokens = Tokenize(str);
        assertResult(tokens, "one", "two", "three");
    }

    public void testTokenizeWithSpaces()
    {
        String str = "\"one and a half\" two three";

        List<String> tokens = Tokenize(str);
        assertResult(tokens, "one and a half", "two", "three");

        str = "one \"two and a half\" three";
        tokens = Tokenize(str);
        assertResult(tokens, "one", "two and a half", "three");

        str = "one two \"three and a half\"";
        tokens = Tokenize(str);
        assertResult(tokens, "one", "two", "three and a half");
    }

    public void testTokenizeWithMultipleSpaces()
    {
        String str = "  one  two   three  ";

        List<String> tokens = Tokenize(str);
        assertResult(tokens, "one", "two", "three");
    }

    public void testTokenizeWithSpacesAndEscapedQuotes()
    {
        String str = "one \"two \\\"and a\\\" half\" three";
        List<String> tokens = Tokenize(str);
        assertResult(tokens, "one", "two \\\"and a\\\" half", "three");
    }

    public void testQuotesNoSpace()
    {
        String str = "alias a1=\"test\"";
        List<String> tokens = Tokenize(str);
        assertResult(tokens, "alias", "a1=\"test\"");
    }

    public void testSingleQuotesNoSpace()
    {
        String str = "alias a1='test'";
        List<String> tokens = Tokenize(str);
        assertResult(tokens, "alias", "a1='test'");
    }

    public void testSingleQuotesWithSpaces()
    {
        String str = "alias a1='echo \"alias 1\"'";
        List<String> tokens = Tokenize(str);
        assertResult(tokens, "alias", "a1='echo \"alias 1\"'");
    }

    public void testMore()
    {
        String str = "bind t 'echo \"test-1\" && echo \"test-2\"'";
        List<String> tokens = Tokenize(str);
        assertResult(tokens, "bind", "t", "echo \"test-1\" && echo \"test-2\"");
    }

    //////////////////////////////////////////////////////////////////////////////
    // Incomplete commands

    public void testMissingQuotes()
    {
        String str = "\"one and a half two three";

        List<String> tokens = Tokenize(str, OPTION_IGNORE_MISSING_QUOTES);
        assertResult(tokens, "one and a half two three");

        str = "one \"two and a half three";
        tokens = Tokenize(str, OPTION_IGNORE_MISSING_QUOTES);
        assertResult(tokens, "one", "two and a half three");

        str = "one two \"three and a half";
        tokens = Tokenize(str, OPTION_IGNORE_MISSING_QUOTES);
        assertResult(tokens, "one", "two", "three and a half");
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    private static List<String> Tokenize(String str)
    {
        return CommandTokenizer.Tokenize(str);
    }

    private static List<String> Tokenize(String str, int options)
    {
        return CommandTokenizer.Tokenize(str, options);
    }
}