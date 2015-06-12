package com.spacemadness.lunar.console;

import com.spacemadness.lunar.core.ArrayIterator;
import com.spacemadness.lunar.core.BooleanList;
import com.spacemadness.lunar.core.FloatList;
import com.spacemadness.lunar.core.IntList;
import com.spacemadness.lunar.utils.ArrayUtils;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alementuev on 5/28/15.
 */
class CCommandUtils
{
    private static final Object[] EMPTY_INVOKE_ARGS = new Object[0];

    static boolean CanInvokeMethodWithArgsCount(Method method, int argsCount)
    {
        if (method == null)
        {
            throw new NullPointerException("Method is null");
        }

        Class<?>[] parameters = method.getParameterTypes();

        if (parameters.length == argsCount)
        {
            return true;
        }

        if (parameters.length > 0)
        {
            if (parameters[parameters.length - 1].isArray()) // last param is array
            {
                if (argsCount >= parameters.length - 1)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean Invoke(Object target, Method method, String[] invokeArgs) throws InvocationTargetException, IllegalAccessException
    {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length == 0)
        {
            return Invoke(target, method, EMPTY_INVOKE_ARGS);
        }

        List<Object> invokeList = new ArrayList<Object>(invokeArgs.length);

        Iterator<String> iter = new ArrayIterator<String>(invokeArgs);
        for (Class<?> param : parameters)
        {
            invokeList.add(ResolveInvokeParameter(param, iter));
        }

        return Invoke(target, method, invokeList.toArray());
    }

    private static boolean Invoke(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException
    {
        method.setAccessible(true);

        if (boolean.class.equals(method.getReturnType()))
        {
            return (boolean) method.invoke(target, args);
        }

        method.invoke(target, args);
        return true;
    }

    public static String GetMethodParamsUsage(Method method)
    {
//        Class<?>[] parameters = method.getParameterTypes();
//        if (parameters.length > 0)
//        {
//            StringBuilder result = new StringBuilder();
//            for (int i = 0; i < parameters.length; ++i)
//            {
//                Class<?> param = parameters[i];
//                if (param.isArray())
//                {
//                    result.append(" ...");
//                }
//                else
//                {
//                    result.append(StringUtils.TryFormat(" <%s>", param.Name));
//                }
//            }
//
//            return result.toString();
//        }
//
//        return null;
        throw new NotImplementedException();
    }

    static List<Object> ResolveInvokeParameters(Class<?>[] parameters, String[] invokeArgs)
    {
//        List<Object> list = new ArrayList<Object>(invokeArgs.length);
//
//        Iterator<String> iter = new ArrayIterator<String>(invokeArgs);
//        for (Class<?> param : parameters)
//        {
//            list.add(ResolveInvokeParameter(param, iter));
//        }
//
//        return list;
        throw new NotImplementedException();
    }

    private static Object ResolveInvokeParameter(Class<?> type, Iterator<String> iter)
    {
        if (String[].class.equals(type))
        {
            List<String> values = new ArrayList<String>();
            while (iter.hasNext())
            {
                values.add(NextArg(iter));
            }
            return ArrayUtils.toArray(values, String.class);
        }

        if (String.class.equals(type))
        {
            return NextArg(iter);
        }

        if (float.class.equals(type))
        {
            return NextFloatArg(iter);
        }

        if (int.class.equals(type))
        {
            return NextIntArg(iter);
        }

        if (boolean.class.equals(type) || Boolean.class.equals(type))
        {
            return NextBoolArg(iter);
        }

        if (int[].class.equals(type))
        {
            IntList values = new IntList();
            while (iter.hasNext())
            {
                values.add(NextIntArg(iter));
            }
            return values.toArray();
        }

        if (float[].class.equals(type))
        {
            FloatList values = new FloatList();
            while (iter.hasNext())
            {
                values.add(NextFloatArg(iter));
            }
            return values.toArray();
        }

        if (boolean[].class.equals(type))
        {
            BooleanList values = new BooleanList();
            while (iter.hasNext())
            {
                values.add(NextBoolArg(iter));
            }
            return values.toArray();
        }

        throw new CCommandException("Unsupported value type: " + type);
    }

    public static int NextIntArg(Iterator<String> iter)
    {
        String arg = NextArg(iter);
        try
        {
            return Integer.parseInt(arg);
        }
        catch (NumberFormatException e)
        {
            throw new CCommandException("Can't parse int arg: '" + arg + "'");
        }
    }

    public static float NextFloatArg(Iterator<String> iter)
    {
        String arg = NextArg(iter);
        try
        {
            return Float.parseFloat(arg);
        }
        catch (NumberFormatException e)
        {
            throw new CCommandException("Can't parse float arg: '" + arg + "'");
        }
    }

    public static boolean NextBoolArg(Iterator<String> iter)
    {
        String arg = NextArg(iter).toLowerCase();
        if (arg.equals("1") || arg.equals("yes") || arg.equals("true")) return true;
        if (arg.equals("0") || arg.equals("no")  || arg.equals("false")) return false;

        throw new CCommandException("Can't parse boolean arg: '" + arg + "'"); 
    }

    public static String NextArg(Iterator<String> iter)
    {
        if (iter.hasNext())
        {
            String arg = StringUtils.UnArg(iter.next());
            if (!IsValidArg(arg)) 
            {
                throw new CCommandException("Invalid arg: " + arg);
            }

            return arg;
        }

        throw new CCommandException("Unexpected end of args");
    }

    public static boolean IsValidArg(String arg)
    {
        return !arg.startsWith("-") || StringUtils.IsNumeric(arg);
    }
}