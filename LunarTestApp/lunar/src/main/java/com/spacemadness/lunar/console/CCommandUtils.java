package com.spacemadness.lunar.console;

import java.util.Iterator;

import spacemadness.com.lunar.NotImplementedException;

/**
 * Created by alementuev on 5/28/15.
 */
static class CCommandUtils
{
    private static final Object[] EMPTY_INVOKE_ARGS = new Object[0];

    public static boolean CanInvokeMethodWithArgsCount(MethodInfo method, int argsCount)
    {
        if (method == null)
        {
            throw new NullPointerException("Method is null");
        }

        Type returnType = method.ReturnType;
        if (returnType != typeof(boolean) && returnType != typeof(void))
        {
            return false;
        }

        ParameterInfo[] parameters = method.GetParameters();

        int realParamsLength = 0;
        int optionalParamsCount = 0;
        for (int i = 0; i < parameters.Length; ++i)
        {
            ParameterInfo param = parameters[i];
            if (param.IsIn || param.IsOut)
            {
                return false; // 'ref' and 'out' are not permitted
            }

            Type paramType = param.ParameterType;
            if (paramType == typeof(Vector2)) realParamsLength += 2;
            else if (paramType == typeof(Vector3)) realParamsLength += 3;
            else if (paramType == typeof(Vector4)) realParamsLength += 4;
            else realParamsLength += 1;

            if (param.IsOptional)
            {
                ++optionalParamsCount;
            }
        }

        if (realParamsLength == argsCount)
        {
            return true;
        }

        if (realParamsLength == 1 && parameters[0].ParameterType.IsArray)
        {
            return true; // a single array param
        }

        if (optionalParamsCount > 0 && 
            argsCount >= realParamsLength - optionalParamsCount &&
            argsCount <= realParamsLength)
        {
            return true;
        }

        return false;
    }

    public static boolean Invoke(Delegate del, String[] invokeArgs)
    {
        if (del == null)
        {
            throw new NullPointerException("Delegate is null");
        }

        return Invoke(del.Target, del.Method, invokeArgs);
    }

    public static boolean Invoke(Object target, MethodInfo method, String[] invokeArgs)
    {
        ParameterInfo[] parameters = method.GetParameters();
        if (parameters.Length == 0)
        {
            return Invoke(target, method, EMPTY_INVOKE_ARGS);
        }

        List<Object> invokeList = new List<Object>(invokeArgs.Length);

        Iterator<String> iter = new Iterator<String>(invokeArgs);
        foreach (ParameterInfo param in parameters)
        {
            invokeList.Add(ResolveInvokeParameter(param, iter));
        }

        return Invoke(target, method, invokeList.ToArray());
    }

    public static boolean Invoke(Delegate del, Object[] invokeArgs)
    {
        if (del == null)
        {
            throw new NullPointerException("Delegate is null");
        }

        return Invoke(del.Target, del.Method, invokeArgs);
    }

    private static boolean Invoke(Object target, MethodInfo method, Object[] args)
    {
        if (method.ReturnType == typeof(boolean))
        {
            return (boolean)method.Invoke(target, args);
        }

        method.Invoke(target, args);
        return true;
    }

    public static String GetMethodParamsUsage(MethodInfo method)
    {
        ParameterInfo[] parameters = method.GetParameters();
        if (parameters.Length > 0)
        {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < parameters.Length; ++i)
            {
                ParameterInfo param = parameters[i];
                if (param.ParameterType.IsArray)
                {
                    result.Append(" ...");
                }
                else if (param.IsOptional)
                {
                    result.AppendFormat(" [<{0}>]", param.Name);
                }
                else
                {
                    result.AppendFormat(" <{0}>", param.Name);
                }
            }

            return result.ToString();
        }

        return null;
    }

    internal static List<Object> ResolveInvokeParameters(ParameterInfo[] parameters, String[] invokeArgs)
    {
        List<Object> list = new List<Object>(invokeArgs.Length);

        Iterator<String> iter = new Iterator<String>(invokeArgs);
        foreach (ParameterInfo param in parameters)
        {
            list.Add(ResolveInvokeParameter(param, iter));
        }

        return list;
    }

    private static Object ResolveInvokeParameter(ParameterInfo param, Iterator<String> iter)
    {
        if (param.IsOptional && !iter.HasNext())
        {
            return param.DefaultValue;
        }

        Type type = param.ParameterType;

        if (type == typeof(String[]))
        {
            List<String> values = new List<String>();
            while (iter.HasNext())
            {
                values.Add(NextArg(iter));
            }
            return values.ToArray();
        }

        if (type == typeof(String))
        {
            return NextArg(iter);
        }

        if (type == typeof(float))
        {
            return NextFloatArg(iter);
        }

        if (type == typeof(int))
        {
            return NextIntArg(iter);
        }

        if (type == typeof(boolean))
        {
            return NextBoolArg(iter);
        }

        if (type == typeof(Vector2))
        {
            float x = NextFloatArg(iter);
            float y = NextFloatArg(iter);

            return new Vector2(x, y);
        }

        if (type == typeof(Vector3))
        {
            float x = NextFloatArg(iter);
            float y = NextFloatArg(iter);
            float z = NextFloatArg(iter);

            return new Vector3(x, y, z);
        }

        if (type == typeof(Vector4))
        {
            float x = NextFloatArg(iter);
            float y = NextFloatArg(iter);
            float z = NextFloatArg(iter);
            float w = NextFloatArg(iter);

            return new Vector4(x, y, z, w);
        }

        if (type == typeof(int[]))
        {
            List<int> values = new List<int>();
            while (iter.HasNext())
            {
                values.Add(NextIntArg(iter));
            }
            return values.ToArray();
        }

        if (type == typeof(float[]))
        {
            List<float> values = new List<float>();
            while (iter.HasNext())
            {
                values.Add(NextFloatArg(iter));
            }
            return values.ToArray();
        }

        if (type == typeof(boolean[]))
        {
            List<boolean> values = new List<boolean>();
            while (iter.HasNext())
            {
                values.Add(NextBoolArg(iter));
            }
            return values.ToArray();
        }

        throw new CCommandException("Unsupported value type: " + type);
    }

    public static int NextIntArg(Iterator<String> iter)
    {
        String arg = NextArg(iter);
        int value;

        if (int.TryParse(arg, out value))
        {
            return value;
        }

        throw new CCommandException("Can't parse int arg: '" + arg + "'"); 
    }

    public static float NextFloatArg(Iterator<String> iter)
    {
        String arg = NextArg(iter);
        float value;

        if (float.TryParse(arg, out value))
        {
            return value;
        }

        throw new CCommandException("Can't parse float arg: '" + arg + "'"); 
    }

    public static boolean NextBoolArg(Iterator<String> iter)
    {
        String arg = NextArg(iter).ToLower();
        if (arg == "1" || arg == "yes" || arg == "true") return true; // FIXME: String comparision
        if (arg == "0" || arg == "no"  || arg == "false") return false; // FIXME: String comparision

        throw new CCommandException("Can't parse boolean arg: '" + arg + "'"); 
    }

    public static String NextArg(Iterator<String> iter)
    {
        if (iter.HasNext())
        {
            String arg = StringUtils.UnArg(iter.Next());
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
        return !arg.StartsWith("-") || StringUtils.IsNumeric(arg);
    }
}