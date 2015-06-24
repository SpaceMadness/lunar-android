package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.CycleArray;

public class TerminalEntries
{
    private final CycleArray<TerminalEntry> entries;

    public TerminalEntries(int capacity)
    {
        entries = new CycleArray<TerminalEntry>(TerminalEntry.class, capacity);
    }

    public TerminalEntry add(String line)
    {
        TerminalEntry entry = new TerminalTextEntry(line);
        entries.Add(entry);
        return entry;
    }

    public TerminalEntry add(String[] lines)
    {
        TerminalEntry entry = new TerminalTableEntry(lines);
        entries.Add(entry);
        return entry;
    }

    public TerminalEntry get(int position)
    {
        return entries.get(position);
    }

    public int size()
    {
        return entries.Length();
    }
}
