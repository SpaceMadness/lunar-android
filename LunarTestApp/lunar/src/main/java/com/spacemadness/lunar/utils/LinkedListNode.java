package com.spacemadness.lunar.utils;

/**
 * Created by alementuev on 6/1/15.
 */
public class LinkedListNode<T>
{
    private LinkedList<T> list;

    public T value;
    LinkedListNode next;
    LinkedListNode prev;

    LinkedListNode(LinkedList<T> list, T value)
    {
        if (list == null)
        {
            throw new NullPointerException("List is null");
        }
        this.list = list;
        this.value = value;
    }

    public LinkedListNode<T> Next()
    {
        return next;
    }

    public LinkedListNode<T> Prev()
    {
        return prev;
    }
}
