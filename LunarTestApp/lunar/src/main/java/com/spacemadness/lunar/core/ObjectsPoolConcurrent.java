package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
public class ObjectsPoolConcurrent<T extends ObjectsPoolEntry> extends ObjectsPool<T>
{
    @Override
    public T NextObject()
    {
        synchronized (this)
        {
            return super.NextObject();
        }
    }

    @Override
    public void Recycle(ObjectsPoolEntry e)
    {
        synchronized (this)
        {
            super.Recycle(e);
        }
    }

    @Override
    public void Destroy()
    {
        synchronized (this)
        {
            super.Destroy();
        }
    }
}