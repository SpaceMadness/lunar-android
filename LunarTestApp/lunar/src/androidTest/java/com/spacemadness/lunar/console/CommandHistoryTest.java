package com.spacemadness.lunar.console;

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

    public void testIteratingItems()
    {
        CommandHistory history = new CommandHistory(5);

        history.Push("1");
        assertIterateBack(history, "1");
        assertIterateForward(history);

        history.Push("2");
        assertIterateBack(history, "2", "1");
        assertIterateForward(history, "2");

        history.Push("3");
        assertIterateBack(history, "3", "2", "1");
        assertIterateForward(history, "2", "3");

        history.Push("4");
        assertIterateBack(history, "4", "3", "2", "1");
        assertIterateForward(history, "2", "3", "4");

        history.Push("5");
        assertIterateBack(history, "5", "4", "3", "2", "1");
        assertIterateForward(history, "2", "3", "4", "5");

        history.Push("6");
        assertIterateBack(history, "6", "5", "4", "3", "2");
        assertIterateForward(history, "3", "4", "5", "6");

        history.Push("7");
        assertIterateBack(history, "7", "6", "5", "4", "3");
        assertIterateForward(history, "4", "5", "6", "7");

        history.Push("8");
        assertIterateBack(history, "8", "7", "6", "5", "4");
        assertIterateForward(history, "5", "6", "7", "8");

        history.Push("9");
        assertIterateBack(history, "9", "8", "7", "6", "5");
        assertIterateForward(history, "6", "7", "8", "9");

        history.Push("10");
        assertIterateBack(history, "10", "9", "8", "7", "6");
        assertIterateForward(history, "7", "8", "9", "10");

        history.Push("11");
        assertIterateBack(history, "11", "10", "9", "8", "7");
        assertIterateForward(history, "8", "9", "10", "11");
    }

    private void assertHistory(CommandHistory history, String... values)
    {
        assertEquals(values.length, history.Count());
        for (int i = 0; i < values.length; ++i)
        {
            assertEquals(values[i], history.get(i));
        }
    }

    private void assertIterateBack(CommandHistory history, String... values)
    {
        int index = 0;

        String value;
        while ((value = history.Prev()) != null)
        {
            assertEquals(values[index], value);
            ++index;
        }

        assertEquals(values.length, index);
    }

    private void assertIterateForward(CommandHistory history, String... values)
    {
        int index = 0;

        String value;
        while ((value = history.Next()) != null)
        {
            assertEquals(values[index], value);
            ++index;
        }

        assertEquals(values.length, index);
    }
}