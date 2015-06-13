package com.spacemadness.lunar.console;

import android.util.Log;

import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.utils.ArrayUtils;
import com.spacemadness.lunar.utils.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

import static com.spacemadness.lunar.utils.ClassUtils.FieldFilter;
import static com.spacemadness.lunar.utils.ClassUtils.as;
import static com.spacemadness.lunar.utils.ClassUtils.tryNewInstance;
import static com.spacemadness.lunar.utils.StringUtils.IsNullOrEmpty;
import static com.spacemadness.lunar.utils.StringUtils.nullOrNonEmpty;

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

    private static final FieldFilter OPTIONS_FIELD_FILTER = new FieldFilter()
    {
        @Override
        public boolean accept(Field field)
        {
            final int modifiers = field.getModifiers();

            if (Modifier.isStatic(modifiers))
            {
                return false;
            }

            if (Modifier.isFinal(modifiers))
            {
                return false;
            }

            CommandOption annotation = field.getAnnotation(CommandOption.class);
            if (annotation == null)
            {
                return false;
            }

            return false;
        }
    };

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

    private static CCommand process(Class<?> aClass) throws IllegalAccessException
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

    public static void ResolveOptions(CCommand command) throws IllegalAccessException
    {
        ResolveOptions(command, command.getClass());
    }

    public static void ResolveOptions(CCommand command, Class<? extends CCommand> commandType) throws IllegalAccessException
    {
        List<Field> optionFields = ClassUtils.listFields(commandType, OPTIONS_FIELD_FILTER, true);
        for (Field optionField : optionFields)
        {
            final CommandOption attr = optionField.getAnnotation(CommandOption.class);

            String name = IsNullOrEmpty(attr.Name()) ? optionField.getName() : attr.Name();

            CCommand.Option option = new CCommand.Option(optionField, name, nullOrNonEmpty(attr.Description()));
            if (!IsNullOrEmpty(attr.Values()))
            {
                option.Values = ParseValues(attr.Values(), optionField.getType());
            }

            option.ShortName = nullOrNonEmpty(attr.ShortName());
            option.IsRequired = attr.Required();
            option.DefaultValue = GetDefaultValue(command, optionField);

            command.AddOption(option);
        }
    }

    private static String[] ParseValues(String str, Class<?> type)
    {
        String[] tokens = str.split("\\s*,\\s*");
        for (int i = 0; i < tokens.length; ++i)
        {
            String token = tokens[i].trim();
            if (CCommand.Option.IsValidValue(type, token))
            {
                tokens[i] = token;
            }
            else
            {
                throw new CCommandParseException("Invalid value '%s' for type '%s'", token, type);
            }
        }

        Arrays.sort(tokens);

        return tokens;
    }

    private static Object GetDefaultValue(CCommand command, Field field) throws IllegalAccessException
    {
        final Object value = field.get(command);
        final Class<?> fieldType = field.getType();

        if (value != null && fieldType.isArray())
        {
            // we only need to copy value is it's an array type
            return ArrayUtils.clone(value, fieldType.getComponentType());
        }

        return value;
    }
}