package com.spacemadness.lunar.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by alementuev on 6/2/15.
 */
public class ArrayIterator<T> implements Iterator<T>
{
    private T[] array;
    private int index;

    public ArrayIterator(T[] array)
    {
        if (array == null)
        {
            throw new NullPointerException("Array is null");
        }
        this.array = array;
        this.index = -1;
    }

    @Override
    public boolean hasNext()
    {
        return index < array.length - 1;
    }

    @Override
    public T next()
    {
        if (hasNext())
        {
            return array[++index];
        }

        throw new NoSuchElementException("No more elements in the array");
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException("Can't remove element from the array");
    }
}
