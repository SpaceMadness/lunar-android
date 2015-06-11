package com.spacemadness.lunar.core;

import java.lang.reflect.Array;

public class FloatList extends PrimitiveList
{
    public FloatList()
    {
        this(DEFAULT_CAPACITY);
    }

    public FloatList(int capacity)
    {
        super(float.class, capacity);
    }

    public void add(float element)
    {
        ensureCapacity();
        incSize();
        set(size() - 1, element);
    }

    public float get(int index)
    {
        checkIndex(index);
        return Array.getFloat(getArray(), index);
    }

    public void set(int index, float element)
    {
        checkIndex(index);
        Array.setFloat(getArray(), index, element);
    }

    public float[] toArray()
    {
        return (float[]) super.toArray();
    }
}