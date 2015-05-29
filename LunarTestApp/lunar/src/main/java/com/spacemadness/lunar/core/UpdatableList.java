package com.spacemadness.lunar.core;

import java.util.List;

/**
 * Created by weee on 5/29/15.
 */
public class UpdatableList extends BaseUpdatableList<IUpdatable> implements IDestroyable
{
    public static final UpdatableList Null = new NullUpdatableList();

    private static final IUpdatable nullUpdatable = new NullUpdatable();

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

    public void Destroy()
    {
        if (Count() > 0)
        {
            Clear();
        }
    }
}