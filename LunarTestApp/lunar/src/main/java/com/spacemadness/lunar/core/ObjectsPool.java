package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.FastList;

/**
 * Created by weee on 5/28/15.
 */
public class ObjectsPool<T extends ObjectsPoolEntry> extends FastList<ObjectsPoolEntry>
        implements IObjectsPool, IDestroyable
{
    private Class<? extends T> cls;

    public ObjectsPool(Class<? extends T> cls)
    {
        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        this.cls = cls;
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
        return ClassUtils.tryNewInstance(cls);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Destroyable

    public void Destroy()
    {
        Clear();
    }
}