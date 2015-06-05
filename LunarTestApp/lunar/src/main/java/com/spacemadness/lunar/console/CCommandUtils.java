package com.spacemadness.lunar.console;

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

    public static boolean CanInvokeMethodWithArgsCount(Method method, int argsCount)
    {
        if (method == null)
        {
            throw new NullPointerException("Method is null");
        }

        Class<?> returnType = method.getReturnType();
        if (!Boolean.class.equals(returnType) && !Void.class.equals(returnType))
        {
            return false;
        }

        Class<?>[] parameters = method.getParameterTypes();

        int realParamsLength = 0;
        for (Class<?> paramType : parameters)
        {
            // if (paramType == typeof(Vector2)) realParamsLength += 2;
            //else if (paramType == typeof(Vector3)) realParamsLength += 3;
            //else if (paramType == typeof(Vector4)) realParamsLength += 4;
            realParamsLength += 1;
        }

        if (realParamsLength == argsCount)
        {
            return true;
        }

        if (realParamsLength == 1 && parameters[0].isArray())
        {
            return true; // a single array param
        }

        return false;
    }

    public static boolean Invoke(Object target, Method method, String[] invokeArgs)
    {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length == 0)
        {
            return Invoke(target, method, EMPTY_INVOKE_ARGS);
        }

        List<Object> invokeList = new ArrayList<Object>(invokeArgs.length);

        Iterator<String> iter = new Iterator<String>(invokeArgs);
        for (Class<?> param : parameters)
        {
            invokeList.add(ResolveInvokeParameter(param, iter));
        }

        return Invoke(target, method, invokeList.toArray());
    }

    private static boolean Invoke(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException
    {
        if (boolean.class.equals(method.getReturnType()))
        {
            return (Boolean) method.invoke(target, args);
        }

        method.invoke(target, args);
        return true;
    }

    public static String GetMethodParamsUsage(Method method)
    {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length > 0)
        {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < parameters.length; ++i)
            {
                Class<?> param = parameters[i];
                if (param.isArray())
                {
                    result.append(" ...");
                }
                else
                {
                    result.append(StringUtils.TryFormat(" <%s>", param.Name));
                }
            }

            return result.toString();
        }

        return null;
    }

    static List<Object> ResolveInvokeParameters(Class<?>[] parameters, String[] invokeArgs)
    {
        List<Object> list = new ArrayList<Object>(invokeArgs.length);

        Iterator<String> iter = new Iterator<String>(invokeArgs);
        for (Class<?> param : parameters)
        {
            list.add(ResolveInvokeParameter(param, iter));
        }

        return list;
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
            return values.toArray();
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
            List<Integer> values = new ArrayList<Integer>();
            while (iter.hasNext())
            {
                values.add(NextIntArg(iter));
            }
            return values.toArray();
        }

        if (float[].class.equals(type))
        {
            List<Float> values = new ArrayList<Float>();
            while (iter.hasNext())
            {
                values.add(NextFloatArg(iter));
            }
            return values.toArray();
        }

        if (boolean[].class.equals(type))
        {
            List<Boolean> values = new ArrayList<Boolean>();
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