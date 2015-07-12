package com.spacemadness.lunar.utils;

/**
 * Created by weee on 5/29/15.
 */
public class FastConcurrentList<T extends FastListNode> extends FastList<T>
{
    @Override
    public synchronized void RemoveItem(T item)
    {
        super.RemoveItem(item);
    }

    @Override
    public synchronized T RemoveFirstItem()
    {
        return super.RemoveFirstItem();
    }

    @Override
    public synchronized T RemoveLastItem()
    {
        return super.RemoveLastItem();
    }

    @Override
    public synchronized boolean ContainsItem(T item)
    {
        return super.ContainsItem(item);
    }

    @Override
    protected synchronized void InsertItem(T item, T prev, T next)
    {
        super.InsertItem(item, prev, next);
    }

    @Override
    public synchronized void Clear()
    {
        super.Clear();
    }
}