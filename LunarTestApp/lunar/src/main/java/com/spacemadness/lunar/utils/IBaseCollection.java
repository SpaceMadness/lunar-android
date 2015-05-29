package com.spacemadness.lunar.utils;

/**
 * Created by weee on 5/28/15.
 */
public interface IBaseCollection<T>
{
    bool Add(T t);
    bool Remove(T t);
    bool Contains(T t);
    void Clear();
    int Count();
}
