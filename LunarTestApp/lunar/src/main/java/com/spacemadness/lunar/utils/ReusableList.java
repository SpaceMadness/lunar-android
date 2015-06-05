package com.spacemadness.lunar.utils;

import android.support.annotation.NonNull;

import com.spacemadness.lunar.core.ObjectsPoolEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by alementuev on 5/28/15.
 */
public class ReusableList<T> extends ObjectsPoolEntry implements List<T>
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
        throw new NotImplementedException();
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
    // IList implementation

    public int IndexOf(T item)
    {
        return indexOf(item);
    } // FIXME: remove

    public void Insert(int index, T item)
    {
        add(index, item);
    }  // FIXME: remove

    public void RemoveAt(int index)
    {
        remove(index);
    }  // FIXME: remove

    @Override
    public void add(int location, T object)
    {
        m_innerList.add(location, object);
    }

    @Override
    public boolean add(T object)
    {
        return m_innerList.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends T> collection)
    {
        return m_innerList.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection)
    {
        return m_innerList.addAll(collection);
    }

    @Override
    public void clear()
    {
        m_innerList.clear();
    }

    @Override
    public boolean contains(Object object)
    {
        return m_innerList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return m_innerList.containsAll(collection);
    }

    @Override
    public T get(int index)
    {
        return m_innerList.get(index);
    }

    @Override
    public int indexOf(Object object)
    {
        return m_innerList.indexOf(object);
    }

    @Override
    public boolean isEmpty()
    {
        return m_innerList.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<T> iterator()
    {
        return m_innerList.iterator();
    }

    @Override
    public int lastIndexOf(Object object)
    {
        return m_innerList.lastIndexOf(object);
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator()
    {
        return m_innerList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int location)
    {
        return m_innerList.listIterator(location);
    }

    @Override
    public T remove(int location)
    {
        return m_innerList.remove(location);
    }

    @Override
    public boolean remove(Object object)
    {
        return m_innerList.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        return m_innerList.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        return m_innerList.retainAll(collection);
    }

    @Override
    public T set(int location, T object)
    {
        return m_innerList.set(location, object);
    }

    @Override
    public int size()
    {
        return m_innerList.size();
    }

    @NonNull
    @Override
    public List<T> subList(int start, int end)
    {
        return m_innerList.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray()
    {
        return m_innerList.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] array)
    {
        return m_innerList.toArray(array);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Collection implementation

    // FIXME: remove methods

    public void Add(T item)
    {
        add(item);
    }

    public void Clear()
    {
        clear();
    }

    public boolean Contains(T item)
    {
        return contains(item);
    }

    public void CopyTo(T[] array, int arrayIndex)
    {
        throw new NotImplementedException();
    }

    public boolean Remove(T item)
    {
        return remove(item);
    }

    public int Count() // FIXME: rename to 'size'
    {
        return size();
    }
}