package com.spacemadness.lunar.ui;

public class ConsoleViewCellEntry
{
    private Object data;

    public ConsoleViewCellEntry(String line)
    {
        if (line == null)
        {
            throw new NullPointerException("Line is null");
        }

        this.data = line;
    }

    public ConsoleViewCellEntry(String[] lines)
    {
        if (lines == null)
        {
            throw new NullPointerException("Lines is null");
        }

        this.data = lines;
    }

    public boolean isPlain()
    {
        return data instanceof String;
    }

    public boolean isTable()
    {
        return data instanceof String[];
    }

    public String getLine()
    {
        return (String) data;
    }

    public String[] getLines()
    {
        return (String[]) data;
    }
}
