package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.debug.Assert;
import com.spacemadness.lunar.debug.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils
{
    private static final Class<?>[] EMPTY_PARAMS = new Class<?>[0];

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
                Constructor<? extends T> defaultConstructor = cls.getDeclaredConstructor(EMPTY_PARAMS);
                return as(defaultConstructor.newInstance(), cls);
            }
        }
        catch (InvocationTargetException e)
        {
            Log.logCrit("Unable to instantiate class %s: %s", cls, e.getCause().getMessage());
        }
        catch (Exception e)
        {
            Log.logCrit("Unable to instantiate class %s: %s", cls, e.getMessage());
        }
        return null;
    }

    public static Method[] ListInstanceMethods(Class<?> cls, MethodFilter filter)
    {
        List<Method> result = new ArrayList<Method>();
        ListInstanceMethods(result, cls, filter);
        return ArrayUtils.toArray(result, Method.class);
    }

    public static List<Method> ListInstanceMethods(List<Method> outList, Class<?> cls, MethodFilter filter)
    {
        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        for (Method method : cls.getDeclaredMethods())
        {
            if (filter.accept(method))
            {
                outList.add(method);
            }
        }

        return outList;
    }

    public static List<Field> listFields(Class<?> cls, boolean recursive)
    {
        return listFields(cls, defaultFieldFilter, recursive);
    }

    public static List<Field> listFields(Class<?> cls, FieldFilter filter, boolean recursive)
    {
        return listFields(new ArrayList<Field>(), cls, filter, recursive);
    }

    public static List<Field> listFields(List<Field> outList, Class<?> cls, FieldFilter filter, boolean recursive)
    {
        if (outList == null)
        {
            throw new NullPointerException("Out list is null");
        }

        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        if (recursive)
        {
            Class<?> c = cls;
            while (c != null && c != Object.class) // don't list Object's fields
            {
                listFields(outList, c, filter);
                c = c.getSuperclass();
            }

            return outList;
        }

        return listFields0(outList, cls, filter);
    }

    public static List<Field> listFields(Class<?> cls)
    {
        return listFields(cls, defaultFieldFilter);
    }

    public static List<Field> listFields(Class<?> cls, FieldFilter filter)
    {
        return listFields(new ArrayList<Field>(), cls, filter);
    }

    public static List<Field> listFields(List<Field> outList, Class<?> cls, FieldFilter filter)
    {
        if (outList == null)
        {
            throw new NullPointerException("Out list is null");
        }

        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        return listFields0(outList, cls, filter);
    }

    private static List<Field> listFields0(List<Field> outList, Class<?> cls, FieldFilter filter)
    {
        final Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i)
        {
            final Field field = fields[i];
            if (filter.accept(field))
            {
                outList.add(field);
            }
        }

        return outList;
    }

    public static String TypeShortName(Class<?> cls)
    {
        throw new NotImplementedException();
    }

    public interface MethodFilter
    {
        boolean accept(Method method);
    }

    public interface FieldFilter
    {
        boolean accept(Field field);
    }

    public static final MethodFilter defaultMethodFilter = new MethodFilter()
    {
        @Override
        public boolean accept(Method method)
        {
            return true;
        }
    };

    public static final FieldFilter defaultFieldFilter = new FieldFilter()
    {
        @Override
        public boolean accept(Field field)
        {
            return true;
        }
    };
}