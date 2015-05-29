package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;

/**
 * Created by weee on 5/28/15.
 */
public class ObjectsPoolEntry extends FastListNode
{
    IObjectsPool pool;
    bool recycled;

    public ObjectsPoolEntry AutoRecycle()
    {
        TimerManager.ScheduleTimer(Recycle);
        return this;
    }

    public void Recycle()
    {
        if (pool != null)
        {
            Assert.IsFalse(recycled);
            recycled = true;

            pool.Recycle(this);
        }

        OnRecycleObject();
    }

    protected void OnRecycleObject()
    {
    }
}
