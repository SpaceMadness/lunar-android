package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
public interface IObjectsPool
{
    void Recycle(ObjectsPoolEntry entry);
}