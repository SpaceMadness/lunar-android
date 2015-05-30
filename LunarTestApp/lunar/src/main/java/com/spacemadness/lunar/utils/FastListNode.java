package com.spacemadness.lunar.utils;

/**
 * Created by weee on 5/29/15.
 */
public class FastListNode<T extends FastListNode>
{
    T m_listPrev;
    T m_listNext;
    IFastList m_list;

    protected T ListNodePrev() // FIXME: rename
    {
        return m_listPrev;
    }

    protected T ListNodeNext() // FIXME: rename
    {
        return m_listNext;
    }

    /* For Unit testing */
    protected void DetachFromList() // FIXME: rename
    {
        m_list = null;
        m_listPrev = m_listNext = null;
    }
}