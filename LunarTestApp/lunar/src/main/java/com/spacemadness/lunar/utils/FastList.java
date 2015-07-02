package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.debug.Assert;

/**
 * Created by weee on 5/29/15.
 */
public class FastList<T extends FastListNode> implements IFastList
{
    T m_listFirst;
    T m_listLast;

    private int m_size;

    public void AddFirstItem(T item) // FIXME: rename
    {
        InsertItem(item, null, m_listFirst);
    }

    public void AddLastItem(T item) // FIXME: rename
    {
        InsertItem(item, m_listLast, null);
    }

    public void InsertBeforeItem(T node, T item) // FIXME: rename
    {
        InsertItem(item, node != null ? (T) node.m_listPrev : null, node);
    }

    public void InsertAfterItem(T node, T item) // FIXME: rename
    {
        InsertItem(item, node, node != null ? (T) node.m_listNext : null);
    }

    public void RemoveItem(T item) // FIXME: rename
    {
        Assert.IsTrue(m_size > 0);
        Assert.AreSame(this, item.m_list);

        T prev = (T) item.m_listPrev;
        T next = (T) item.m_listNext;

        if (prev != null)
        {
            prev.m_listNext = next;
        }
        else
        {
            m_listFirst = next;
        }

        if (next != null)
        {
            next.m_listPrev = prev;
        }
        else
        {
            m_listLast = prev;
        }

        item.m_listNext = item.m_listPrev = null;
        item.m_list = null;
        --m_size;
    }

    public T RemoveFirstItem() // FIXME: rename
    {
        T node = ListFirst();
        if (node != null)
        {
            RemoveItem(node);
        }

        return node;
    }

    public T RemoveLastItem() // FIXME: rename
    {
        T node = ListLast();
        if (node != null)
        {
            RemoveItem(node);
        }

        return node;
    }

    public boolean ContainsItem(T item) // FIXME: rename
    {
        if (item.m_list != this)
        {
            return false;
        }

        for (FastListNode t = m_listFirst; t != null; t = t.m_listNext)
        {
            if (t == item)
            {
                return true;
            }
        }

        return false;
    }

    protected void InsertItem(T item, T prev, T next) // FIXME: rename
    {
        Assert.IsNull(item.m_list);

        if (next != null)
        {
            next.m_listPrev = item;
        }
        else
        {
            m_listLast = item;
        }

        if (prev != null)
        {
            prev.m_listNext = item;
        }
        else
        {
            m_listFirst = item;
        }

        item.m_listPrev = prev;
        item.m_listNext = next;
        item.m_list = this;
        ++m_size;
    }

    public void Clear() // FIXME: rename
    {
        for (FastListNode t = m_listFirst; t != null; )
        {
            FastListNode next = t.m_listNext;
            t.m_listPrev = t.m_listNext = null;
            t.m_list = null;
            t = next;
        }

        m_listFirst = m_listLast = null;
        m_size = 0;
    }

    public int Count() // FIXME: rename
    {
        return m_size;
    }

    public boolean isEmpty()
    {
        return Count() == 0;
    }

    public T ListFirst() // FIXME: rename
    {
        return m_listFirst;
    }

    protected void ListFirst(T item) // FIXME: rename
    {
        m_listFirst = item;
    }

    public T ListLast() // FIXME: rename
    {
        return m_listLast;
    }

    protected void ListLast(T item) // FIXME: rename
    {
        m_listLast = item;
    }
}