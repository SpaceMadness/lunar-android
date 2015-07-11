package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.FastList;
import com.spacemadness.lunar.utils.FastListNode;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory
{
    private final int capacity;

    private FastList<Entry> entries;
    private Entry prevEntry;

    public CommandHistory(int capacity)
    {
        if (capacity <= 0)
        {
            throw new NullPointerException("Invalid capacity: " + capacity);
        }
        this.capacity = capacity;
        this.entries = new FastList<>();
    }

    public void Push(String line)
    {
        if (line == null)
        {
            throw new NullPointerException("Line is null");
        }

        Entry entry = findEntry(line);
        if (entry != null) // if entry already exists - move it into the beginning of the queue
        {
            entries.RemoveItem(entry);
        }
        else if (entries.Count() >= capacity) // if history overflows - reuse last item
        {
            entry = entries.RemoveFirstItem();
            entry.setCommandLine(line);
        }
        else // we can add a new one
        {
            entry = new Entry(line);
        }
        entries.AddLastItem(entry);

        reset();
    }

    public String prev()
    {
        if (prevEntry != null)
        {
            String commandLine = prevEntry.getCommandLine();
            prevEntry = prevEntry.getPrev();
            return commandLine;
        }

        return null;
    }

    public void reset()
    {
        prevEntry = entries.ListLast();
    }

    public void Clear()
    {
        entries.Clear();
    }

    public List<String> toList()
    {
        return toList(new ArrayList<String>());
    }

    public List<String> toList(List<String> outList)
    {
        for (Entry entry = entries.ListFirst(); entry != null; entry = entry.getNext())
        {
            outList.add(entry.getCommandLine());
        }
        return outList;
    }

    public void load(List<String> commands)
    {
        entries.Clear();

        for (String commandLine : commands)
        {
            Entry entry = new Entry(commandLine);
            entries.AddLastItem(entry);
        }
    }

    public int Count()
    {
        return entries.Count();
    }

    private Entry findEntry(String commandLine)
    {
        for (Entry entry = entries.ListFirst(); entry != null; entry = entry.getNext())
        {
            if (commandLine.equals(entry.getCommandLine()))
            {
                return entry;
            }
        }

        return null;
    }

    private class Entry extends FastListNode<Entry>
    {
        public String commandLine;

        public Entry(String commandLine)
        {
            if (commandLine == null)
            {
                throw new NullPointerException("Command line is null");
            }
            this.commandLine = commandLine;
        }

        public String getCommandLine()
        {
            return commandLine;
        }

        public void setCommandLine(String commandLine)
        {
            this.commandLine = commandLine;
        }

        public Entry getNext()
        {
            return ListNodeNext();
        }

        public Entry getPrev()
        {
            return ListNodePrev();
        }
    }
}
