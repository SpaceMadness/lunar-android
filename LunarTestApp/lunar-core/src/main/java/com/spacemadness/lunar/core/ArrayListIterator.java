package com.spacemadness.lunar.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by alementuev on 6/15/15.
 */
public class ArrayListIterator<E> implements Iterator<E>
{
    private ArrayList<E> list;
    private int index;

    public ArrayListIterator(ArrayList<E> list)
    {
        if (list == null)
        {
            throw new NullPointerException("Array list is null");
        }
        this.list = list;
        this.index = -1;
    }

    @Override
    public boolean hasNext()
    {
        return index < list.size() - 1;
    }

    @Override
    public E next()
    {
        if (hasNext())
        {
            return list.get(++index);
        }

        throw new NoSuchElementException("No more elements");
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException("Operation is not supported yet");
    }

    public E current()
    {
        return list.get(index);
    }

    public int getPosition()
    {
        return index;
    }

    public void setPosition(int index)
    {
        this.index = index; // FIXME: check if position is valid
    }
}
