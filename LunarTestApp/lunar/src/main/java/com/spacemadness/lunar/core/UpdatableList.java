package com.spacemadness.lunar.core;

import com.spacemadness.lunar.utils.BaseList;

import java.util.List;

/**
 * Created by weee on 5/29/15.
 */
public class UpdatableList extends BaseList<IUpdatable> implements IUpdatable, IDestroyable
{
    public static final UpdatableList Null = new NullUpdatableList();

    private static final IUpdatable nullUpdatable = new IUpdatable()
    {
        @Override
        public void Update(float dt)
        {
        }
    };

    public UpdatableList()
    {
        super(nullUpdatable);
    }

    public UpdatableList(int capacity)
    {
        super(nullUpdatable, capacity);
    }

    protected UpdatableList(List<IUpdatable> list, IUpdatable nullUpdatable)
    {
        super(list, nullUpdatable);
    }

    @Override
    public synchronized void Update(float delta)
    {
        try
        {
            lock();

            int elementsCount = list.size();
            for (int i = 0; i < elementsCount; ++i) // do not update added items on that tick
            {
                list.get(i).Update(delta);
            }
        }
        finally
        {
            unlock();
        }
    }

    public void Destroy()
    {
        if (Count() > 0)
        {
            Clear();
        }
    }
}