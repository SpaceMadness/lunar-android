package com.spacemadness.lunar.core;

import com.spacemadness.lunar.utils.BaseList;

import java.util.List;

/**
 * Created by weee on 5/28/15.
 */
abstract class BaseUpdatableList<T extends IUpdatable> extends BaseList<T> implements IUpdatable
{   
    protected BaseUpdatableList(T nullElement)
    {
        super(nullElement, 0);
    }

    protected BaseUpdatableList(T nullElement, int capacity)
    {
        super(nullElement, capacity);
    }

    protected BaseUpdatableList(List<T> list, T nullElement)
    {
        super(list, nullElement);
    }

    public void Update(float delta)
    {
        int elementsCount = list.size();
        for (int i = 0; i < elementsCount; ++i) // do not update added items on that tick
        {
            list.get(i).Update(delta);
        }
        
        ClearRemoved();
    }
}