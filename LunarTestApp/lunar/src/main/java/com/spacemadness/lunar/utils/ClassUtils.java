package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.debug.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils
{
    /**
     * Tries to cast the object to the specified class. Fires an assertion and
     * return null if class cast is impossible.
     */
    public static <T> T cast(Object obj, Class<T> clazz)
    {
        if (obj != null)
        {
            T casted = as(obj, clazz);
            Assert.IsTrue(casted != null, "Can't cast from '%s' to '%s'", obj.getClass(), clazz);

            return casted;
        }
        return null;
    }

    /**
     * Tries to cast the object to the specified class. Returns null if class
     * cast is impossible.
     */
    @SuppressWarnings("unchecked")
    public static <T> T as(Object obj, Class<T> clazz)
    {
        if (obj != null && clazz.isInstance(obj))
        {
            return (T) obj;
        }

        return null;
    }

    /**
     * Tries to create an instance of the class by calling default constructor.
     * Returns null is instantiation fails or passed class object is null.
     */
    public static <T> T tryNewInstance(Class<? extends T> cls)
    {
        try
        {
            if (cls != null)
            {
                Constructor<? extends T> defaultConstructor = cls.getConstructor();
                return as(defaultConstructor.newInstance(), cls);
            }
        }
        catch (Exception e)
        {
            Log.logCrit("Unable to instantiate class %s: %s", cls, e.getMessage());
        }
        return null;
    }

    public static Method[] ListInstanceMethods(Class<?> cls, MethodFilter filter)
    {
        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        List<Method> result = new ArrayList<Method>();
        for (Method method : cls.getDeclaredMethods())
        {
            if (filter.accept(method))
            {
                result.add(method);
            }
        }

        return ArrayUtils.toArray(result, Method.class);
    }

    public interface MethodFilter
    {
        boolean accept(Method method);
    }
}