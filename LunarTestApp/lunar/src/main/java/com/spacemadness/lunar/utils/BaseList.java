package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.debug.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weee on 5/28/15.
 */
public class BaseList<T> implements IBaseCollection<T>
{
    protected List<T> list;

    protected T nullElement;
    protected int removedCount;

    protected BaseList(T nullElement)
    {
        this(nullElement, 0);
    }

    protected BaseList(T nullElement, int capacity)
    {
        this(new ArrayList<T>(capacity), nullElement);
    }

    protected BaseList(List<T> list, T nullElement)
    {
        this.list = list;
        this.nullElement = nullElement;
    }

    public boolean Add(T e) // FIXME: rename
    {
        Assert.NotContains(e, list);
        list.add(e);
        return true;
    }

    public boolean Remove(T e) // FIXME: rename
    {
        int index = list.indexOf(e);
        if (index != -1)
        {
            RemoveAt(index);
            return true;
        }

        return false;
    }

    public T Get(int index) // FIXME: rename
    {
        return list.get(index);
    }

    public int IndexOf(T e) // FIXME: rename
    {
        return list.indexOf(e);
    }

    public void RemoveAt(int index)
    {
        ++removedCount;
        list.set(index, nullElement);
    }

    public void Clear() // FIXME: rename
    {
        list.clear();
    }

    public boolean Contains(T e) // FIXME: rename
    {
        return list.contains(e);
    }

    public boolean IsNull() // FIXME: rename
    {
        return false;
    }

    protected void ClearRemoved() // FIXME: rename
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

    public int Count() // FIXME: rename
    {
        return list.size() - removedCount;
    }
}
