package com.spacemadness.lunar.console;

import com.spacemadness.lunar.com.spacemadness.lunar.console.CCommand;

import spacemadness.com.lunar.NotImplementedException;

/**
 * Created by alementuev on 5/28/15.
 */
class RuntimeResolver // TODO: remove this class
{
    public static List<CCommand> ResolveCommands()
    {
        List<CCommand> list = new List<CCommand>();

        try
        {
            foreach (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies())
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

                        CCommand command = ClassUtils.CreateInstance<CCommand>(type);
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
                            Log.e("Unable to register command: name={0} type={1}", commandName, type);
                        }
                    }
                }
            }
        }
        catch (ReflectionTypeLoadException e)
        {
            StringBuilder message = new StringBuilder("Unable to resolve Lunar commands:");
            
            foreach (Exception ex in e.LoaderExceptions)
            {
                message.AppendFormat("\n\t{0}", ex.Message);
            }
            
            throw new LunarRuntimeResolverException(message.ToString(), e);
        }
        catch (Exception e)
        {
            throw new LunarRuntimeResolverException("Unable to resolve Lunar commands", e);
        }

        return list;
    }

    public static void ResolveOptions(CCommand command)
    {
        ResolveOptions(command, command.GetType());
    }

    public static void ResolveOptions(CCommand command, Type commandType)
    {
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
    }

    private static String[] ParseValues(String str, Type type)
    {
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
                throw new CCommandParseException("Invalid value '{0}' for type '{1}'", token, type);
            }
        }

        Array.Sort(tokens);

        return tokens;
    }

    private static boolean IsCorrectPlatform(CCommandFlags flags)
    {
        /*
            if ((flags & CCommandFlags.IOS) != 0 && !Runtime.IsIOS)
            {
                return false;
            }
            
            if ((flags & CCommandFlags.Android) != 0 && !Runtime.IsAndroid)
            {
                return false;
            }

            if ((flags & CCommandFlags.Mobile) != 0 && !Runtime.IsMobile)
            {
                return false;
            }

            if ((flags & CCommandFlags.Standalone) != 0 && !Runtime.IsStandAlone)
            {
                return false;
            }

            if ((flags & CCommandFlags.OSX) != 0 && !Runtime.IsOSX)
            {
                Debug.Log(4);
                return false;
            }

            if ((flags & CCommandFlags.Windows) != 0 && !Runtime.IsWindows)
            {
                Debug.Log(5);
                return false;
            }

            if ((flags & CCommandFlags.Linux) != 0 && !Runtime.IsLinux)
            {
                Debug.Log(6);
                return false;
            }

            if ((flags & CCommandFlags.Editor) != 0)
            {
                if ((flags & CCommandFlags.OSXEditor) != 0 && !Runtime.IsOSXEditor)
                {
                    return false;
                }

                if ((flags & CCommandFlags.WindowsEditor) != 0 && !Runtime.IsWindowsEditor)
                {
                    return false;
                }

                return true;
            }
            */

        return true;
    }

    private static Object GetDefaultValue(CCommand command, FieldInfo info)
    {
        Object value = info.GetValue(command);
        if (info.FieldType.IsValueType)
        {
            return value;
        }

        return value is ICloneable ? ((ICloneable)value).Clone() : value;
    }
}