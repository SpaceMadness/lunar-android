package com.spacemadness.lunar.utils;

/**
 * Created by weee on 5/29/15.
 */
public class FastConcurrentList<T extends FastListNode> extends FastList<T>
{
    @Override
    public void RemoveItem(T item)
    {
        synchronized (this)
        {
            super.RemoveItem(item);
        }
    }

    @Override
    public T RemoveFirstItem()
    {
        synchronized (this)
        {
            return super.RemoveFirstItem();
        }
    }

    @Override
    public T RemoveLastItem()
    {
        synchronized (this)
        {
            return super.RemoveLastItem();
        }
    }

    @Override
    public boolean ContainsItem(T item)
    {
        synchronized (this)
        {
            return super.ContainsItem(item);
        }
    }

    @Override
    protected void InsertItem(T item, T prev, T next)
    {
        synchronized (this)
        {
            super.InsertItem(item, prev, next);
        }
    }

    @Override
    public void Clear()
    {
        synchronized (this)
        {
            super.Clear();
        }
    }
}