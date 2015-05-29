package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;

/**
 * Created by weee on 5/28/15.
 */
public class ObjectsPool<T extends ObjectsPoolEntry> extends FastList<ObjectsPoolEntry>
        implements IObjectsPool, IDestroyable
{
    public ObjectsPool()
    {
    }

    public T NextAutoRecycleObject()
    {
        return (T)NextObject().AutoRecycle();
    }

    public T NextObject()
    {
        ObjectsPoolEntry first = RemoveFirstItem();
        if (first == null)
        {
            first = CreateObject();
        }

        first.pool = this;
        first.recycled = false;

        return (T)first;
    }

    public void Recycle(ObjectsPoolEntry e)
    {
        Assert.IsInstanceOfType<T>(e);
        Assert.AreSame(this, e.pool);

        AddLastItem(e);
    }

    protected T CreateObject()
    {
        return new T();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Destroyable

    public void Destroy()
    {
        Clear();
    }
}