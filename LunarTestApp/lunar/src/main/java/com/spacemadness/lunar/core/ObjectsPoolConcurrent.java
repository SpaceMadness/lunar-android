package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
public class ObjectsPoolConcurrent<T extends ObjectsPoolEntry> extends ObjectsPool<T>
{
    public ObjectsPoolConcurrent(Class<? extends T> cls)
    {
        super(cls);
    }

    @Override
    public synchronized T NextObject()
    {
        return super.NextObject();
    }

    @Override
    public synchronized void Recycle(ObjectsPoolEntry e)
    {
        super.Recycle(e);
    }

    @Override
    public synchronized void Destroy()
    {
        super.Destroy();
    }

    @Override
    public synchronized int size()
    {
        return super.size();
    }
}