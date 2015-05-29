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

    public void AddFirstItem(T item)
    {
        InsertItem(item, null, m_listFirst);
    }

    public void AddLastItem(T item)
    {
        InsertItem(item, m_listLast, null);
    }

    public void InsertBeforeItem(T node, T item)
    {
        InsertItem(item, node != null ? node.m_listPrev : null, node);
    }

    public void InsertAfterItem(T node, T item)
    {
        InsertItem(item, node, node != null ? node.m_listNext : null);
    }

    public void RemoveItem(T item)
    {
        Assert.Greater(m_size, 0);
        Assert.AreSame(this, item.m_list);

        T prev = item.m_listPrev;
        T next = item.m_listNext;

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

    public T RemoveFirstItem()
    {
        T node = ListFirst();
        if (node != null)
        {
            RemoveItem(node);
        }

        return node;
    }

    public T RemoveLastItem()
    {
        T node = ListLast;
        if (node != null)
        {
            RemoveItem(node);
        }

        return node;
    }

    public bool ContainsItem(T item)
    {
        if (item.m_list != this)
        {
            return false;
        }

        for (T t = m_listFirst; t != null; t = t.m_listNext)
        {
            if (t == item)
            {
                return true;
            }
        }

        return false;
    }

    protected void InsertItem(T item, T prev, T next)
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

    public void Clear()
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

    public int Count()
    {
        return m_size;
    }

    public T ListFirst()
    {
        return m_listFirst;
    }

    protected void ListFirst(T item)
    {
        m_listFirst = item;
    }

    public T ListLast()
    {
        return m_listLast;
    }

    protected void ListLast(T item)
    {
        m_listLast = item;
    }
}