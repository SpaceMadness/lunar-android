package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.CycleArray;

/**
 * Created by alementuev on 6/16/15.
 */
public class CommandHistory
{
    private CycleArray<String> m_entries;
    private int m_currentIndex;

    public CommandHistory(int capacity)
    {
        m_entries = new CycleArray<String>(String.class, capacity);
        m_currentIndex = -1;
    }

    public String get(int index)
    {
        int entryIndex = m_entries.HeadIndex() + index;
        return m_entries.get(entryIndex);
    }

    public void Push(String line)
    {
        if (m_entries.Length() == 0 || m_entries.get(m_entries.Length() - 1) != line)
        {
            m_entries.Add(line);
        }

        Reset();
    }

    public void Reset()
    {
        m_currentIndex = m_entries.Length();
    }

    public String Next()
    {
        int nextIndex = m_currentIndex + 1;
        if (nextIndex < m_entries.Length())
        {
            m_currentIndex = nextIndex;
            return m_entries.get(m_currentIndex);
        }

        return null;
    }

    public String Prev()
    {
        int prevIndex = m_currentIndex - 1;
        if (prevIndex >= m_entries.HeadIndex())
        {
            m_currentIndex = prevIndex;
            return m_entries.get(m_currentIndex);
        }

        return null;
    }

    public void Clear()
    {
        m_currentIndex = -1;
        m_entries.Clear();
    }

    public int Count()
    {
        return m_entries.RealLength();
    }
}
