package com.spacemadness.lunar.console;

import android.util.Log;

import com.spacemadness.lunar.console.annotations.Command;
import static com.spacemadness.lunar.utils.ClassUtils.*;
import com.spacemadness.lunar.utils.NotImplementedException;

import static com.spacemadness.lunar.utils.StringUtils.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * Created by alementuev on 5/28/15.
 */
class RuntimeResolver // TODO: remove this class
{
    private static final String TAG = PathClassLoader.class.getSimpleName();
    private static final Field dexField;

    static
    {
        dexField = resolveDexField();
    }

    private static Field resolveDexField()
    {
        try
        {
            Field dexField = PathClassLoader.class.getDeclaredField("mDexs");
            dexField.setAccessible(true);
            return dexField;
        }
        catch (Exception e)
        {
            Log.e(TAG, "Can't init runtime resolver: " + e.getMessage());
        }

        return null;
    }

    public static List<CCommand> ResolveCommands()
    {
        try
        {
            return ResolveCommandsGuarded();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CCommand> ResolveCommandsGuarded() throws Exception
    {
        List<CCommand> list = new ArrayList<CCommand>();

        PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();
        DexFile[] dexFiles = (DexFile[]) dexField.get(classLoader);
        for (DexFile dex : dexFiles)
        {
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements())
            {
                String className = entries.nextElement();

                Class<?> aClass = Class.forName(className, false, classLoader);
                if (aClass != null)
                {
                    process(aClass);
                }
            }
        }

        /*
        for (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies())
        {
            foreach (Type type in assembly.GetTypes())
            {
                Object[] attrs = type.GetCustomAttributes(typeof(CCommandAttribute), true);
                if (attrs != null && attrs.Length == 1)
                {
                    CCommandAttribute cmdAttr = (CCommandAttribute)attrs[0];
                    String commandName = cmdAttr.Name;
                    if (!IsCorrectPlatform(cmdAttr.Flags))
                    {
                        Debug.LogWarning("Skipping command: " + commandName);
                        continue;
                    }

                    CCommand command = CreateInstance<CCommand>(type);
                    if (command != null)
                    {
                        command.Name = commandName;
                        command.Description = cmdAttr.Description;
                        if (cmdAttr.Values != null)
                        {
                            command.Values = cmdAttr.Values.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                        }
                        command.Flags |= cmdAttr.Flags;
                        ResolveOptions(command);
                        list.Add(command);
                    }
                    else
                    {
                        Log.e("Unable to register command: name=%s type=%s", commandName, type);
                    }
                }
            }
        }
        */

        return list;
    }

    private static CCommand process(Class<?> aClass)
    {
        Command annotation = aClass.getAnnotation(Command.class);
        if (annotation == null)
        {
            return null;
        }

        Object instance = tryNewInstance(aClass);
        CCommand command = as(instance, CCommand.class);
        if (command == null)
        {
            return null;
        }

        command.Name = annotation.Name();
        command.Description = nullOrNonEmpty(annotation.Description());

        if (!IsNullOrEmpty(annotation.Values()))
        {
            command.Values(annotation.Values().split("\\s*,\\s*")); // FIXME: add error checking
        }
        command.Flags |= annotation.Flags();

        ResolveOptions(command);

        return command;
    }

    public static void ResolveOptions(CCommand command)
    {
        ResolveOptions(command, command.getClass());
    }

    public static void ResolveOptions(CCommand command, Class<? extends CCommand> commandType)
    {
        /*
        FieldInfo[] fields = commandType.GetFields(BindingFlags.Public | BindingFlags.NonPublic | BindingFlags.Instance);
        for (int i = 0; i < fields.Length; ++i)
        {
            FieldInfo info = fields[i];
            Object[] attributes = info.GetCustomAttributes(typeof(CCommandOptionAttribute), true);
            if (attributes.Length == 1)
            {
                CCommandOptionAttribute attr = (CCommandOptionAttribute)attributes[0];

                String name = attr.Name != null ? attr.Name : info.Name;

                Option option = new Option(info, name, attr.Description);
                if (attr.Values != null)
                {
                    option.Values = ParseValues(attr.Values, info.FieldType);
                }

                option.ShortName = attr.ShortName;
                option.IsRequired = attr.Required;
                option.DefaultValue = GetDefaultValue(command, info);

                command.AddOption(option);
            }
        }
        */

        throw new NotImplementedException();
    }

    private static String[] ParseValues(String str, Class<?> type)
    {
        /*
        String[] tokens = str.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
        for (int i = 0; i < tokens.Length; ++i)
        {
            String token = tokens[i].Trim();
            if (Option.IsValidValue(type, token))
            {
                tokens[i] = token;
            }
            else
            {
                throw new CCommandParseException("Invalid value '%s' for type '%s'", token, type);
            }
        }

        Array.Sort(tokens);

        return tokens;
        */

        throw new NotImplementedException();
    }

    private static Object GetDefaultValue(CCommand command, /*FieldInfo*/ Object info)
    {
        /*
        Object value = info.GetValue(command);
        if (info.FieldType.IsValueType)
        {
            return value;
        }

        return value is ICloneable ? ((ICloneable)value).Clone() : value;
        */
        throw new NotImplementedException();
    }
}