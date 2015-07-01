package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.debug.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weee on 5/28/15.
 */
public class BaseList<T> implements IBaseCollection<T> // TODO: need a better name
{
    protected List<T> list; // TODO: reduce visibility

    protected T nullElement; // TODO: reduce visibility
    protected int removedCount; // TODO: reduce visibility
    protected boolean locked; // TODO: reduce visibility

    protected BaseList(T nullElement)
    {
        this(nullElement, 0);
    }

    protected BaseList(T nullElement, int capacity)
    {
        this(new ArrayList<T>(capacity), nullElement);

        if (nullElement == null)
        {
            throw new NullPointerException("Null element is null");
        }
    }

    protected BaseList(List<T> list, T nullElement)
    {
        this.list = list;
        this.nullElement = nullElement;
    }

    public synchronized boolean Add(T e)
    {
        if (e == null)
        {
            throw new NullPointerException("Element is null");
        }

        Assert.NotContains(e, list);
        list.add(e);

        return true;
    }

    public synchronized boolean Remove(T e)
    {
        int index = list.indexOf(e);
        if (index != -1)
        {
            RemoveAt(index);
            return true;
        }

        return false;
    }

    public synchronized T Get(int index) // FIXME: rename
    {
        return list.get(index);
    }

    public synchronized int IndexOf(T e) // FIXME: rename
    {
        return list.indexOf(e);
    }

    public synchronized void RemoveAt(int index)
    {
        if (locked)
        {
            ++removedCount;
            list.set(index, nullElement);
        }
        else
        {
            list.remove(index);
        }
    }

    public synchronized void Clear() // FIXME: rename
    {
        if (locked)
        {
            for (int i = 0; i < list.size(); ++i)
            {
                list.set(i, nullElement);
            }
            removedCount = list.size();
        }
        else
        {
            list.clear();
            removedCount = 0;
        }
    }

    public synchronized boolean Contains(T e) // FIXME: rename
    {
        return list.contains(e);
    }

    protected synchronized void ClearRemoved() // FIXME: rename
    {
        for (int i = list.size() - 1; removedCount > 0 && i >= 0; --i)
        {
            if (list.get(i) == nullElement)
            {
                list.remove(i);
                --removedCount;
            }
        }
    }

    public synchronized int Count() // FIXME: rename
    {
        return list.size() - removedCount;
    }
}