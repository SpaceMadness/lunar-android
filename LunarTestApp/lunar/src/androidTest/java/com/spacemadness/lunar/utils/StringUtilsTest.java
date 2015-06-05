package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.ColorCode;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by alementuev on 5/28/15.
 */
public class StringUtilsTest extends TestCase
{
    //////////////////////////////////////////////////////////////////////////////
    // Args

    public void testArgWithNoSpace()
    {
        String arg = "arg";
        String escaped = arg;
        Assert.assertEquals(escaped, StringUtils.Arg(arg));
        Assert.assertEquals(arg, StringUtils.UnArg(escaped));
    }

    public void testArgWithSpace()
    {
        String arg = "some arg with spaces";
        String escaped = '"' + arg + '"';
        Assert.assertEquals(escaped, StringUtils.Arg(arg));
        Assert.assertEquals(arg, StringUtils.UnArg(escaped));
    }

    public void testArgWithQuotes()
    {
        String arg = "\"quotes\"";
        String escaped = "\\\"quotes\\\"";
        Assert.assertEquals(escaped, StringUtils.Arg(arg));
        Assert.assertEquals(arg, StringUtils.UnArg(escaped));
    }

    public void testArgWithQuotesAndSpaces()
    {
        String arg = "spaces and \"quotes\"";
        String escaped = "\"spaces and \\\"quotes\\\"\"";
        Assert.assertEquals(escaped, StringUtils.Arg(arg));
        Assert.assertEquals(arg, StringUtils.UnArg(escaped));
    }

    public void testArgWithSpaceAndSingleQuotes()
    {
        String arg = "some 'arg with' spaces";
        String escaped = "\"some \\'arg with\\' spaces\"";
        Assert.assertEquals(escaped, StringUtils.Arg(arg));
        Assert.assertEquals(arg, StringUtils.UnArg(escaped));
    }

    public void testArgWithSingleQuotes()
    {
        String arg = "'quotes'";
        String escaped = "\\'quotes\\'";
        Assert.assertEquals(escaped, StringUtils.Arg(arg));
        Assert.assertEquals(arg, StringUtils.UnArg(escaped));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Words

    public void testFindingWordStart()
    {
        String value = "abcd";
        AssertWordStartIndex(value, "abcd");

        value = "abcd efgh";
        AssertWordStartIndex(value, "abcd ");
        AssertWordStartIndex(value, "efgh");

        value = " a b c ";
        AssertWordStartIndex(value, "a ");
        AssertWordStartIndex(value, "b ");
        AssertWordStartIndex(value, "c ");

        value = "a def g hij  k";
        AssertWordStartIndex(value, "a ");
        AssertWordStartIndex(value, "def ");
        AssertWordStartIndex(value, "g ");
        AssertWordStartIndex(value, "hij  ");
        AssertWordStartIndex(value, "k");

        value = "a";
        AssertWordStartIndex(value, "a");

        value = "\n";
        AssertWordStartIndex(value, "\n");
    }

    public void testFindingWordEnd()
    {
        String value = "abcd";
        AssertWordEndIndex(value, "abcd");

        value = "abcd efgh";
        AssertWordEndIndex(value, "abcd");
        AssertWordEndIndex(value, " efgh");

        value = " a b c ";
        AssertWordEndIndex(value, " a");
        AssertWordEndIndex(value, " b");
        AssertWordEndIndex(value, " c");

        value = "a def g hij  k";
        AssertWordEndIndex(value, "a");
        AssertWordEndIndex(value, " def");
        AssertWordEndIndex(value, " g");
        AssertWordEndIndex(value, " hij");
        AssertWordEndIndex(value, "  k");

        value = "a";
        AssertWordEndIndex(value, "a");

        value = "\n";
        AssertWordEndIndex(value, "\n");
    }

    private static void AssertWordStartIndex(String value, String word)
    {
        int startIndex = value.indexOf(word);
        Assert.assertTrue(-1 != startIndex);

        int endIndex = startIndex + word.length();
        for (int i = startIndex + 1; i <= endIndex; ++i)
        {
            Assert.assertEquals("\"" + word + "\":" + i, startIndex, StringUtils.StartOfTheWord(value, i));
        }
    }

    private static void AssertWordEndIndex(String value, String word)
    {
        int startIndex = value.indexOf(word);
        Assert.assertTrue(-1 == startIndex);

        int endIndex = startIndex + word.length();
        for (int i = startIndex; i < endIndex; ++i)
        {
            Assert.assertEquals("\"" + word + "\":" + i, endIndex, StringUtils.EndOfTheWord(value, i));
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Tags

    public void testRemoveBoldStyleTag()
    {
        String expected = "text";
        String formatted = StringUtils.B(expected);
        Assert.assertEquals(expected, StringUtils.RemoveRichTextTags(formatted));
    }

    public void testRemoveItalicStyleTag()
    {
        String expected = "text";
        String formatted = StringUtils.I(expected);
        Assert.assertEquals(expected, StringUtils.RemoveRichTextTags(formatted));
    }

    public void testRemoveColorTag()
    {
        String expected = "text";
        String formatted = StringUtils.C(expected, ColorCode.Clear);
        Assert.assertEquals(expected, StringUtils.RemoveRichTextTags(formatted));
    }

    public void testRemoveColorMultipleTags()
    {
        String expected = "red green blue";
        String formatted = String.format("{0} {1} {2}",
                StringUtils.C("red", ColorCode.Clear),
                StringUtils.C("green", ColorCode.Error),
                StringUtils.C("blue", ColorCode.LevelDebug)
        );
        Assert.assertEquals(expected, StringUtils.RemoveRichTextTags(formatted));
    }

    public void testRemoveColorMultipleInnerTags()
    {
        String expected = "red green blue";
        String formatted = StringUtils.C(String.format("{0} {1} {2}",
                StringUtils.C("red", ColorCode.Clear),
                StringUtils.C("green", ColorCode.Error),
                StringUtils.C("blue", ColorCode.LevelDebug)
        ), ColorCode.LevelError);
        Assert.assertEquals(expected, StringUtils.RemoveRichTextTags(formatted));
    }

    public void testRemoveColorMultipleInnerStyleTags()
    {
        String expected = "red green blue";
        String formatted = StringUtils.B(String.format("{0} {1} {2}",
                StringUtils.C("red", ColorCode.Clear),
                StringUtils.C("green", ColorCode.Error),
                StringUtils.C("blue", ColorCode.LevelDebug)
        ));
        Assert.assertEquals(expected, StringUtils.RemoveRichTextTags(formatted));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Colors

    public void testInsertColors()
    {
        Color[] lookup =
        {
            Color.red,
            Color.green,
            Color.blue
        };

        String formatted = String.format("{0} {1} {2}",
                StringUtils.C("red", (ColorCode) 0),
                StringUtils.C("green", (ColorCode) 1),
                StringUtils.C("blue", (ColorCode)2)
        );

        String expected = String.format("{0} {1} {2}",
                StringUtils.C("red", lookup[0]),
                StringUtils.C("green", lookup[1]),
                StringUtils.C("blue", lookup[2])
        );

        String actual = StringUtils.SetColors(formatted, lookup);
        Assert.assertEquals(expected, actual);
    }

    public void testInsertColorsWrongLookup()
    {
        Color[] lookup =
        {
            Color.red,
            Color.green,
            Color.blue
        };

        String formatted = String.format("{0} {1} {2}",
                StringUtils.C("red", (ColorCode) 0),
                StringUtils.C("green", (ColorCode) 1),
                StringUtils.C("blue", (ColorCode) 2),
                StringUtils.C("yellow", (ColorCode)3)
        );

        String expected = String.format("{0} {1} {2}",
                StringUtils.C("red", lookup[0]),
                StringUtils.C("green", lookup[1]),
                StringUtils.C("blue", lookup[2]),
                StringUtils.C("yellow", (ColorCode)3)
        );

        String actual = StringUtils.SetColors(formatted, lookup);
        Assert.assertEquals(expected, actual);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Lines

    public void testCountLineBreaksSingleLine()
    {
        String line = "line";
        Assert.assertEquals(0, StringUtils.LinesBreaksCount(line));
    }

    public void testCountLineBreaksTwoLines()
    {
        String line = "line1\n" +
                      "line2";

        Assert.assertEquals(1, StringUtils.LinesBreaksCount(line));
    }

    public void testCountLineBreaksThreeLines()
    {
        String line = "line1\n" +
                      "line2\n" +
                      "line3";

        Assert.assertEquals(2, StringUtils.LinesBreaksCount(line));
    }

    public void testCountLineBreaksNullLine()
    {
        String line = null;
        Assert.assertEquals(0, StringUtils.LinesBreaksCount(line));
    }

    public void testMoveLineUpAscending()
    {
        String value = "\n" +
                       "a\n" +
                       "cd\n" +
                       "fgh\n" +
                       "jklm";

        int index = 0;
        Assert.assertEquals(index, StringUtils.MoveLineUp(value, index));

        // second line
        index = value.indexOf('a');
        Assert.assertEquals(0, StringUtils.MoveLineUp(value, index));

        index = value.indexOf('a') + 1;
        Assert.assertEquals(0, StringUtils.MoveLineUp(value, index));

        // third line
        index = value.indexOf('c');
        Assert.assertEquals(value.indexOf('a'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('d');
        Assert.assertEquals(value.indexOf('a') + 1, StringUtils.MoveLineUp(value, index));

        index = value.indexOf('d') + 1;
        Assert.assertEquals(value.indexOf('a') + 1, StringUtils.MoveLineUp(value, index));

        // fourth line
        index = value.indexOf('f');
        Assert.assertEquals(value.indexOf('c'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('g');
        Assert.assertEquals(value.indexOf('d'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('h');
        Assert.assertEquals(value.indexOf('d') + 1, StringUtils.MoveLineUp(value, index));

        index = value.indexOf('h') + 1;
        Assert.assertEquals(value.indexOf('d') + 1, StringUtils.MoveLineUp(value, index));

        // fifth line
        index = value.indexOf('j');
        Assert.assertEquals(value.indexOf('f'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('k');
        Assert.assertEquals(value.indexOf('g'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('l');
        Assert.assertEquals(value.indexOf('h'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('m');
        Assert.assertEquals(value.indexOf('h') + 1, StringUtils.MoveLineUp(value, index));

        index = value.indexOf('m') + 1;
        Assert.assertEquals(value.indexOf('h') + 1, StringUtils.MoveLineUp(value, index));
    }

    public void testMoveLineDownAscending()
    {
        String value = "\n" +
                       "a\n" +
                       "cd\n" +
                       "fgh\n" +
                       "jklm";

        // first line
        int index = 0;
        Assert.assertEquals(value.indexOf('a'), StringUtils.MoveLineDown(value, index));

        // second line
        index = value.indexOf('a');
        Assert.assertEquals(value.indexOf('c'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('a') + 1;
        Assert.assertEquals(value.indexOf('d'), StringUtils.MoveLineDown(value, index));

        // third line
        index = value.indexOf('c');
        Assert.assertEquals(value.indexOf('f'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('d');
        Assert.assertEquals(value.indexOf('g'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('d') + 1;
        Assert.assertEquals(value.indexOf('h'), StringUtils.MoveLineDown(value, index));

        // fourth line
        index = value.indexOf('f');
        Assert.assertEquals(value.indexOf('j'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('g');
        Assert.assertEquals(value.indexOf('k'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('h');
        Assert.assertEquals(value.indexOf('l'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('h') + 1;
        Assert.assertEquals(value.indexOf('m'), StringUtils.MoveLineDown(value, index));

        // fifth line
        index = value.indexOf('j');
        Assert.assertEquals(index, StringUtils.MoveLineDown(value, index));

        index = value.indexOf('k');
        Assert.assertEquals(index, StringUtils.MoveLineDown(value, index));

        index = value.indexOf('l');
        Assert.assertEquals(index, StringUtils.MoveLineDown(value, index));

        index = value.indexOf('m');
        Assert.assertEquals(index, StringUtils.MoveLineDown(value, index));
    }

    public void testMoveLineUpDescending()
    {
        String value = "abcd\n" +
                       "fgh\n" +
                       "jk\n" +
                       "m\n" +
                       "\n" +
                       "";

        int index = value.indexOf('a');

        // first line
        Assert.assertEquals(index, StringUtils.MoveLineUp(value, index));

        index = value.indexOf('b');
        Assert.assertEquals(index, StringUtils.MoveLineUp(value, index));

        index = value.indexOf('c');
        Assert.assertEquals(index, StringUtils.MoveLineUp(value, index));

        index = value.indexOf('d');
        Assert.assertEquals(index, StringUtils.MoveLineUp(value, index));

        index = value.indexOf('d') + 1;
        Assert.assertEquals(index, StringUtils.MoveLineUp(value, index));

        // second line
        index = value.indexOf('f');
        Assert.assertEquals(value.indexOf('a'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('g');
        Assert.assertEquals(value.indexOf('b'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('h');
        Assert.assertEquals(value.indexOf('c'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('h') + 1;
        Assert.assertEquals(value.indexOf('d'), StringUtils.MoveLineUp(value, index));

        // third line
        index = value.indexOf('j');
        Assert.assertEquals(value.indexOf('f'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('k');
        Assert.assertEquals(value.indexOf('g'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('k') + 1;
        Assert.assertEquals(value.indexOf('h'), StringUtils.MoveLineUp(value, index));

        // fourth line
        index = value.indexOf('m');
        Assert.assertEquals(value.indexOf('j'), StringUtils.MoveLineUp(value, index));

        index = value.indexOf('m') + 1;
        Assert.assertEquals(value.indexOf('k'), StringUtils.MoveLineUp(value, index));

        // fifth line
        index = value.lastIndexOf('\n');
        Assert.assertEquals(value.indexOf('m'), StringUtils.MoveLineUp(value, index));

        index = value.lastIndexOf('\n') + 1;
        Assert.assertEquals(value.lastIndexOf('\n'), StringUtils.MoveLineUp(value, index));
    }

    public void testMoveLineDownDescending()
    {
        String value = "abcd\n" +
                       "fgh\n" +
                       "jk\n" +
                       "m\n" +
                       "\n" +
                       "";

        // first line
        int index = value.indexOf('a');
        Assert.assertEquals(value.indexOf('f'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('b');
        Assert.assertEquals(value.indexOf('g'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('c');
        Assert.assertEquals(value.indexOf('h'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('d');
        Assert.assertEquals(value.indexOf('h') + 1, StringUtils.MoveLineDown(value, index));

        index = value.indexOf('d') + 1;
        Assert.assertEquals(value.indexOf('h') + 1, StringUtils.MoveLineDown(value, index));

        // second line
        index = value.indexOf('f');
        Assert.assertEquals(value.indexOf('j'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('g');
        Assert.assertEquals(value.indexOf('k'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('h');
        Assert.assertEquals(value.indexOf('k') + 1, StringUtils.MoveLineDown(value, index));

        index = value.indexOf('h') + 1;
        Assert.assertEquals(value.indexOf('k') + 1, StringUtils.MoveLineDown(value, index));

        // third line
        index = value.indexOf('j');
        Assert.assertEquals(value.indexOf('m'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('k');
        Assert.assertEquals(value.indexOf('m') + 1, StringUtils.MoveLineDown(value, index));

        index = value.indexOf('k') + 1;
        Assert.assertEquals(value.indexOf('m') + 1, StringUtils.MoveLineDown(value, index));

        // fourth line
        index = value.indexOf('m');
        Assert.assertEquals(value.lastIndexOf('\n'), StringUtils.MoveLineDown(value, index));

        index = value.indexOf('m') + 1;
        Assert.assertEquals(value.lastIndexOf('\n'), StringUtils.MoveLineDown(value, index));

        // fifth line
        index = value.lastIndexOf('\n');
        Assert.assertEquals(value.lastIndexOf('\n') + 1, StringUtils.MoveLineDown(value, index));

        index = value.lastIndexOf('\n') + 1;
        Assert.assertEquals(value.lastIndexOf('\n') + 1, StringUtils.MoveLineDown(value, index));
    }

    public void testMoveLineUpEmpty()
    {
        String value = "\n" +
                       "\n" +
                       "";

        // first line
        Assert.assertEquals(0, StringUtils.MoveLineUp(value, 0));

        // second line
        Assert.assertEquals(0, StringUtils.MoveLineUp(value, 1));

        // third line
        Assert.assertEquals(1, StringUtils.MoveLineUp(value, 2));
    }

    public void testMoveLineDownEmpty()
    {
        String value = "\n" +
                       "\n" +
                       "";

        // first line
        Assert.assertEquals(1, StringUtils.MoveLineDown(value, 0));

        // second line
        Assert.assertEquals(2, StringUtils.MoveLineDown(value, 1));

        // third line
        Assert.assertEquals(2, StringUtils.MoveLineDown(value, 2));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Text suggestions

    public void testNullTextSuggestions()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "b"
        };

        Assert.assertNull(StringUtils.GetSuggestedText(null, values));
    }

    public void testEmptyTextSuggestions()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "b"
        };

        Assert.assertNull(StringUtils.GetSuggestedText("", values));
    }

    public void testOnlyTextSuggestions()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "b"
        };

        Assert.assertEquals("b", StringUtils.GetSuggestedText("b", values));
    }

    public void testTextSuggestions()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "aa2", "b"
        };

        Assert.assertEquals("aa", StringUtils.GetSuggestedText("a", values));
    }

    public void testTextSuggestions2()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "aa2", "b"
        };

        Assert.assertEquals("aa", StringUtils.GetSuggestedText("aa", values));
    }

    public void testTextSuggestions3()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "aa2", "b"
        };

        Assert.assertEquals("aa1", StringUtils.GetSuggestedText("aa1", values));
    }

    public void testTextSuggestions4()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "aa2", "b"
        };

        Assert.assertEquals("aa11", StringUtils.GetSuggestedText("aa11", values));
    }

    public void testTextSuggestions5()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "aa2", "b"
        };

        Assert.assertEquals("aa111", StringUtils.GetSuggestedText("aa111", values));
    }

    public void testTextSuggestions6()
    {
        String[] values =
        {
            "aa1", "aa11", "aa12", "aa13", "aa111", "aa112", "aa113", "aa2", "b"
        };

        Assert.assertNull(StringUtils.GetSuggestedText("aa1111", values));
    }
}