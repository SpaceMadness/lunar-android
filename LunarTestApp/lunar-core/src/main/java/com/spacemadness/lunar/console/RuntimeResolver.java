package com.spacemadness.lunar.console;

import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;
import com.spacemadness.lunar.utils.ArrayUtils;
import com.spacemadness.lunar.utils.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            return field.getAnnotation(CommandOption.class) != null;
        }
    };

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
        final List<CCommand> list = new ArrayList<CCommand>();

        ClassUtils.listClassesName(new ClassUtils.Map<String>()
        {
            private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            @Override
            public void each(String className)
            {
                if (className.startsWith("android.")) // skip system Android classes
                {
                    return;
                }

                try
                {
                    Class<?> aClass = Class.forName(className, false, classLoader);
                    CCommand command = process(aClass);
                    if (command != null)
                    {
                        list.add(command);
                    }
                }
                catch (Throwable e)
                {
                    e.printStackTrace(); // TODO: better error handling
                }
            }
        });

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

    static void ResolveOptions(CCommand command)
    {
        try
        {
            List<Field> optionFields = ClassUtils.listFields(command.getClass(), OPTIONS_FIELD_FILTER, true);
            for (Field optionField : optionFields)
            {
                if (!Modifier.isPublic(optionField.getModifiers()))
                {
                    optionField.setAccessible(true);
                }

                final CommandOption attr = optionField.getAnnotation(CommandOption.class);

                String name = IsNullOrEmpty(attr.Name()) ? optionField.getName() : attr.Name();

                CCommand.Option option = new CCommand.Option(command, optionField, name, nullOrNonEmpty(attr.Description()));
                if (!IsNullOrEmpty(attr.Values())) {
                    option.Values = ParseValues(attr.Values(), optionField.getType());
                }

                option.ShortName = nullOrNonEmpty(attr.ShortName());
                option.IsRequired = attr.Required();
                option.DefaultValue = GetDefaultValue(command, optionField);

                command.AddOption(option);
            }
        }
        catch (Exception e)
        {
            throw new OptionsResolveException(e);
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