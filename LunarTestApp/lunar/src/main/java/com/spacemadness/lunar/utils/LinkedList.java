package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.console.CCommand;

import java.util.Iterator;

/**
 * Created by alementuev on 6/1/15.
 */
public class LinkedList<T> implements Iterable<T>
{
    private LinkedListNode<T> first;
    private LinkedListNode<T> last;
    private int length;

    public LinkedListNode<T> First()
    {
        return first;
    }

    public LinkedListNode<T> Last()
    {
        return last;
    }

    public void clear()
    {
        for (LinkedListNode node = first; node != null;)
        {
            LinkedListNode next = node.next;
            node.prev = node.next = null;
            node.value = null;
            node = next;
        }
        length = 0;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iter(first);
    }

    public void AddBefore(LinkedListNode<T> node, T item)
    {
        throw new NotImplementedException();
    }

    public void AddLast(T item)
    {
        throw new NotImplementedException();
    }

    public boolean Remove(T item)
    {
        throw new NotImplementedException();
    }

    private class Iter implements Iterator<T>
    {
        private LinkedListNode<T> node;

        public Iter(LinkedListNode<T> node)
        {
            this.node = node;
        }

        @Override
        public boolean hasNext()
        {
            return node.next != null;
        }

        @Override
        public T next()
        {
            node = node.next;
            return node.value;
        }

        @Override
        public void remove()
        {

        }
    }

}
