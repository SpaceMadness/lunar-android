package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/29/15.
 */
final class NullUpdatableList extends UpdatableList
{
    public NullUpdatableList()
    {
        super(null, null);
    }

    @Override
    public void Update(float delta)
    {
    }

    @Override
    public boolean Add(IUpdatable updatable)
    {
        throw new InvalidOperationException("Can't add element to unmodifiable updatable list");
    }

    @Override
    public boolean Remove(IUpdatable updatable)
    {
        throw new InvalidOperationException("Can't remove element from unmodifiable updatable list");
    }

    @Override
    public void RemoveAt(int index)
    {
        throw new InvalidOperationException("Can't remove element from unmodifiable updatable list");
    }

    @Override
    public void Clear()
    {
        throw new InvalidOperationException("Can't clear unmodifiable updatable list");
    }

    @Override
    public boolean Contains(IUpdatable updatable)
    {
        return false;
    }

    @Override
    public boolean IsNull()
    {
        return true;
    }

    @Override
    public int Count()
    {
        return 0;
    }
}