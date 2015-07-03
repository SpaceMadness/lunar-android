package com.spacemadness.lunar.core;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.utils.FastListNode;

/**
 * Created by weee on 5/28/15.
 */
public class ObjectsPoolEntry<T extends ObjectsPoolEntry> extends FastListNode<T>
{
    ObjectsPool<?> pool;
    boolean recycled;

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
