package com.spacemadness.lunar.core;

import java.lang.reflect.Array;

public class IntList extends PrimitiveList
{
    public IntList()
    {
        this(DEFAULT_CAPACITY);
    }

    public IntList(int capacity)
    {
        super(int.class, capacity);
    }

    public void add(int element)
    {
        ensureCapacity();
        incSize();
        set(size() - 1, element);
    }

    public int get(int index)
    {
        checkIndex(index);
        return Array.getInt(getArray(), index);
    }

    public void set(int index, int element)
    {
        checkIndex(index);
        Array.setInt(getArray(), index, element);
    }

    public int[] toArray()
    {
        return (int[]) super.toArray();
    }
}
