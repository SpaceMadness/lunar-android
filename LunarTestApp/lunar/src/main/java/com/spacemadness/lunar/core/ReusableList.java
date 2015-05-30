package com.spacemadness.lunar.core;

import com.spacemadness.lunar.utils.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by weee on 5/28/15.
 */
public class ReusableList<T> extends ObjectsPoolEntry
{
    private List<T> m_innerList;

    public ReusableList()
    {
        m_innerList = new ArrayList<T>();
    }

    @Override
    protected void OnRecycleObject()
    {
        m_innerList.clear();

        super.OnRecycleObject();
    }

    public T[] ToArray()
    {
        return m_innerList.toArray(new T[m_innerList.size()]);
    }

    public void Sort()
    {
        // m_innerList.Sort();
        throw new NotImplementedException();
    }

    public void Sort(Comparator<T> comparator)
    {
        Collections.sort(m_innerList, comparator);
    }

    //////////////////////////////////////////////////////////////////////////////
    // List implementation

    public int IndexOf(T item)
    {
        return m_innerList.indexOf(item);
    }

    public void Insert(int index, T item)
    {
        m_innerList.add(index, item);
    }

    public void RemoveAt(int index)
    {
        m_innerList.remove(index);
    }

    public T get(int index)
    {
        return m_innerList.get(index);
    }

    public void set(int index, T item)
    {
        m_innerList.set(index, item);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Collection implementation

    public void Add(T item)
    {
        m_innerList.add(item);
    }

    public void Clear()
    {
        m_innerList.clear();
    }

    public boolean Contains(T item)
    {
        return m_innerList.contains(item);
    }

    public void CopyTo(T[] array, int arrayIndex)
    {
        // m_innerList.CopyTo(array, arrayIndex);
        throw new NotImplementedException();
    }

    public boolean Remove(T item)
    {
        return m_innerList.remove(item);
    }

    public int Count()
    {
        return m_innerList.size();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Enumerable implementation

    /*
    public IEnumerator<T> GetEnumerator()
    {
        return m_innerList.GetEnumerator();
    }
    */

    //////////////////////////////////////////////////////////////////////////////
    // IEnumerable implementation

    /*
    System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
    {
        return m_innerList.GetEnumerator();
    }
    */
}