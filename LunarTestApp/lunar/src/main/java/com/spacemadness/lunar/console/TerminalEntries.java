package com.spacemadness.lunar.console;

import com.spacemadness.lunar.console.entries.TerminalExceptionEntry;
import com.spacemadness.lunar.console.entries.TerminalTableEntry;
import com.spacemadness.lunar.console.entries.TerminalTextEntry;
import com.spacemadness.lunar.utils.CycleArray;

public class TerminalEntries
{
    private final CycleArray<TerminalEntry> entries;

    public TerminalEntries(int capacity)
    {
        entries = new CycleArray<>(TerminalEntry.class, capacity);
    }

    public TerminalEntry add(String line)
    {
        return addEntry(new TerminalTextEntry(line));
    }

    public TerminalEntry add(String[] lines)
    {
        return addEntry(new TerminalTableEntry(lines));
    }

    public TerminalEntry add(Throwable e, String message)
    {
        return addEntry(new TerminalExceptionEntry(e, message));
    }

    public TerminalEntry add(TerminalEntry entry)
    {
        if (entry == null)
        {
            throw new NullPointerException("Entry is null");
        }

        return addEntry(entry);
    }

    private TerminalEntry addEntry(TerminalEntry entry)
    {
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
