package com.spacemadness.lunar.core;

import java.lang.reflect.Array;

public class BooleanList extends PrimitiveList
{
    public BooleanList()
    {
        this(DEFAULT_CAPACITY);
    }

    public BooleanList(int capacity)
    {
        super(boolean.class, capacity);
    }

    public void add(boolean element)
    {
        ensureCapacity();
        incSize();
        set(size() - 1, element);
    }

    public boolean get(int index)
    {
        checkIndex(index);
        return Array.getBoolean(getArray(), index);
    }

    public void set(int index, boolean element)
    {
        checkIndex(index);
        Array.setBoolean(getArray(), index, element);
    }

    public boolean[] toArray()
    {
        return (boolean[]) super.toArray();
    }
}