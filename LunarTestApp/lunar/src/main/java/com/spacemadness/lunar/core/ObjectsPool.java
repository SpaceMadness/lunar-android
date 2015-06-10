package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.FastList;

/**
 * Created by weee on 5/28/15.
 */
public class ObjectsPool<T extends ObjectsPoolEntry> implements IDestroyable
{
    private final Class<? extends T> cls;
    private final FastList<ObjectsPoolEntry> poolList;

    public ObjectsPool(Class<? extends T> cls)
    {
        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        this.cls = cls;
        this.poolList = new FastList<>();
    }

    public T NextAutoRecycleObject()
    {
        return (T)NextObject().AutoRecycle();
    }

    public T NextObject()
    {
        ObjectsPoolEntry first = TakeReference();
        if (first == null)
        {
            first = CreateObject();
        }

        first.pool = this;
        first.recycled = false;

        return (T)first;
    }

    void Recycle(ObjectsPoolEntry e)
    {
        Assert.IsInstanceOfType(cls, e);
        Assert.AreSame(this, e.pool);

        PutReference(e);
    }

    protected T CreateObject()
    {
        return ClassUtils.tryNewInstance(cls);
    }

    private ObjectsPoolEntry TakeReference()
    {
        return poolList.RemoveFirstItem();
    }

    private void PutReference(ObjectsPoolEntry e)
    {
        poolList.AddFirstItem(e);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Destroyable

    public void Destroy()
    {
        poolList.Clear();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    public int size()
    {
        return poolList.Count();
    }
}