package com.spacemadness.lunar.core;

import java.lang.reflect.Array;

abstract class PrimitiveList
{
    protected static final int DEFAULT_CAPACITY = 12;
    private static final int MIN_CAPACITY_INCREMENT = 12;

    private final Class<?> elementType;

    private Object array;
    private int size;
    private int capacity;

    protected PrimitiveList(Class<?> elementType, int capacity)
    {
        if (elementType == null)
        {
            throw new NullPointerException("Element type is null");
        }

        if (capacity < 0)
        {
            throw new IllegalArgumentException("Invalid capacity: " + capacity);
        }

        this.elementType = elementType;
        this.capacity = capacity;
        this.array = Array.newInstance(elementType, capacity);
    }

    public void clear()
    {
        size = 0;
    }

    protected Object toArray()
    {
        Object temp = Array.newInstance(elementType, size);
        System.arraycopy(array, 0, temp, 0, size);
        return temp;
    }

    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    protected Object getArray()
    {
        return array;
    }

    protected void ensureCapacity()
    {
        if (size == capacity)
        {
            resize(size + (size < (MIN_CAPACITY_INCREMENT / 2) ? MIN_CAPACITY_INCREMENT : size >> 1));
        }
    }

    protected void incSize()
    {
        ++size;
    }

    protected void checkIndex(int index)
    {
        if (index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds 0.." + size);
        }
    }

    private void resize(int capacity)
    {
        Object temp = Array.newInstance(elementType, capacity);
        System.arraycopy(array, 0, temp, 0, size);
        this.array = temp;
        this.capacity = capacity;
    }
}
