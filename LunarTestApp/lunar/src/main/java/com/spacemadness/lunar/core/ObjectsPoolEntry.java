package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.FastListNode;
import com.spacemadness.lunar.utils.NotImplementedException;

/**
 * Created by weee on 5/28/15.
 */
public class ObjectsPoolEntry extends FastListNode
{
    ObjectsPool<?> pool;
    boolean recycled;

    public ObjectsPoolEntry AutoRecycle()
    {
//        TimerManager.ScheduleTimer(Recycle);
//        return this;
        throw new NotImplementedException();
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

    protected void OnRecycleObject() // FIXME: rename
    {
    }
}
