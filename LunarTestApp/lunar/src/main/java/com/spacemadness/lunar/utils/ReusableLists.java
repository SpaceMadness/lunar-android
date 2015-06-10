package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.core.ObjectsPool;
import com.spacemadness.lunar.core.ObjectsPoolConcurrent;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by alementuev on 5/28/15.
 */
public class ReusableLists
{
    private static Map<Class<?>, Object> m_poolLookup;

    public static <T> ReusableList<T> NextAutoRecycleList(Class<? extends T> cls)
    {
        return (ReusableList<T>) NextList(cls).AutoRecycle();
    }

    public static <T> ReusableList<T> NextList(Class<? extends T> cls)
    {
//        if (m_poolLookup == null)
//        {
//            m_poolLookup = new HashMap<Class<?>, Object>();
//        }
//
//        Object poolObject = m_poolLookup.get(cls);
//        if (poolObject == null)
//        {
//            poolObject = new ObjectsPoolConcurrent<ReusableList<T>>(ReusableList.class);
//            m_poolLookup.put(cls, poolObject);
//        }
//
//        return ((ObjectsPool<ReusableList<T>>) poolObject).NextObject();

        throw new NotImplementedException();
    }

    public static void Clear()
    {
        m_poolLookup.clear();
    }
}
