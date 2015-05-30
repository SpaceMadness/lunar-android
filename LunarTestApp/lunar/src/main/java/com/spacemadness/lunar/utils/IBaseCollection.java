package com.spacemadness.lunar.utils;

/**
 * Created by weee on 5/28/15.
 */
public interface IBaseCollection<T>
{
    boolean Add(T t);
    boolean Remove(T t);
    boolean Contains(T t);
    void Clear();
    int Count();
}
