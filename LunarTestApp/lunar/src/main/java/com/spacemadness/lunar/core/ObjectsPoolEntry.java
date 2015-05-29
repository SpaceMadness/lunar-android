package com.spacemadness.lunar.core;

import com.spacemadness.lunar.utils.NotImplementedException;

public class ObjectsPoolEntry extends FastListNode
{
    protected IObjectsPool pool;

    public void recycle()
    {
        if (pool != null)
        {
            pool.Recycle(this);
        }

        onRecycleObject();
    }

    public void AutoRecycle()
    {
        throw new NotImplementedException();
    }

    protected void onRecycleObject()
    {
    }
}