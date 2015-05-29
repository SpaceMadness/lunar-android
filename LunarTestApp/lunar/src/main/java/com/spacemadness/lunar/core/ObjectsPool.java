package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.ClassUtils;

public class ObjectsPool<T extends ObjectsPoolEntry> extends FastList<ObjectsPoolEntry> implements IObjectsPool, IDestroyable
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

    @SuppressWarnings("unchecked")
    public T NextObject()
    {
        ObjectsPoolEntry first = RemoveFirstItem();
        if (first == null)
        {
            first = ClassUtils.tryNewInstance(cls);
        }

        first.pool = this;
        return (T) first;
    }

    public void Recycle(ObjectsPoolEntry e)
    {
        Assert.True(cls.isInstance(e));
        Assert.True(e.pool == this);

        AddLastItem(e);
    }

    //////////////////////////////////////////////////////////////////////////////

    public void Destroy()
    {
        Clear();
    }

}