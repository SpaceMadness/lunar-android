package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.ArrayUtils;

import junit.framework.TestCase;

public class CommandHistoryTest extends TestCase
{
    public void testAddingItems()
    {
        CommandHistory history = new CommandHistory(5);

        history.Push("1");
        assertHistory(history, "1");

        history.Push("2");
        assertHistory(history, "1", "2");

        history.Push("3");
        assertHistory(history, "1", "2", "3");

        history.Push("4");
        assertHistory(history, "1", "2", "3", "4");

        history.Push("5");
        assertHistory(history, "1", "2", "3", "4", "5");

        history.Push("6");
        assertHistory(history, "2", "3", "4", "5", "6");

        history.Push("7");
        assertHistory(history, "3", "4", "5", "6", "7");

        history.Push("8");
        assertHistory(history, "4", "5", "6", "7", "8");

        history.Push("9");
        assertHistory(history, "5", "6", "7", "8", "9");

        history.Push("10");
        assertHistory(history, "6", "7", "8", "9", "10");

        history.Push("11");
        assertHistory(history, "7", "8", "9", "10", "11");
    }

    private void assertHistory(CommandHistory history, String... expected)
    {
        assertEquals(ArrayUtils.toList(expected), history.list());
    }
}