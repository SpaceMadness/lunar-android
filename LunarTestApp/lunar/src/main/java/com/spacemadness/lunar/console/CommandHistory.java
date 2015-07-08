package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.CycleArray;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory
{
    private CycleArray<String> entries;

    public CommandHistory(int capacity)
    {
        entries = new CycleArray<>(String.class, capacity);
    }

    public void Push(String line)
    {
        if (!entries.contains(line))
        {
            entries.Add(line);
        }
    }

    public void Clear()
    {
        entries.Clear();
    }

    public List<String> list()
    {
        return list(new ArrayList<String>());
    }

    public List<String> list(List<String> outList)
    {
        for (int i = entries.HeadIndex(); i < entries.Length(); ++i)
        {
            outList.add(entries.get(i));
        }
        return outList;
    }

    public int Count()
    {
        return entries.RealLength();
    }
}
