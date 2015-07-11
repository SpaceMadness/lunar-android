package com.spacemadness.lunar.console;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.core.ArrayListIterator;
import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.ArrayUtils;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.spacemadness.lunar.AppTerminal.*;

public abstract class CCommand implements Comparable<CCommand>
{
    private static final String[] EMPTY_COMMAND_ARGS = new String[0];

    private Map<String, Option> m_optionsLookup;

    private List<Option> m_options;
    private String[] m_values;
    private Method[] executeMethods;

    private ICCommandDelegate m_delegate;

    public CCommand()
    {
        this.Delegate(null);
    }

    public CCommand(String name)
    {
        this();

        if (name == null)
        {
            throw new NullPointerException("Name is null");
        }
        this.Name = name;
    }

    boolean ExecuteTokens(List<String> tokens)
    {
        return ExecuteTokens(tokens, null);
    }

    boolean ExecuteTokens(List<String> tokens, String commandLine)
    {
        try
        {
            return ExecuteGuarded(tokens, commandLine);
        }
        catch (CCommandException e)
        {
            PrintError(e.getMessage());
        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof CCommandException)
            {
                PrintError(e.getCause().getMessage());
            }
            else
            {
                PrintError(e.getCause(), "Error while executing command");
            }
        }
        catch (NotImplementedException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            PrintError(e, "Error while executing command");
        }

        return false;
    }

    private boolean ExecuteGuarded(List<String> tokens, String commandLine) throws Exception
    {
        ResetOptions();

        Iterator<String> iter = tokens.iterator();
        iter.next(); // first token is a command name

        if (this.IsManualMode)
        {
            PrintPrompt(commandLine);
        }

        List<String> argsList = new ArrayList<String>();
        while (iter.hasNext())
        {
            String token = StringUtils.UnArg(iter.next());

            // first, try to parse options
            if (token.startsWith("--"))
            {
                String optionName = token.substring(2);
                ParseOption(iter, optionName);
            }
            else if (token.startsWith("-") && !StringUtils.IsNumeric(token))
            {
                String optionName = token.substring(1);
                ParseOption(iter, optionName);
            }
            else
            {
                // consume the rest of the args
                argsList.add(token);
                while (iter.hasNext())
                {
                    token = StringUtils.UnArg(iter.next());
                    argsList.add(token);
                }

                break;
            }
        }

        if (m_values != null)
        {
            if (argsList.size() != 1)
            {
                PrintError("Unexpected arguments count %s", argsList.size());
                PrintUsage();
                return false;
            }

            String arg = argsList.get(0);
            if (ArrayUtils.IndexOf(m_values, arg) == -1)
            {
                PrintError("Unexpected argument '%s'", arg);
                PrintUsage();
                return false;
            }
        }

        if (m_options != null)
        {
            for (int i = 0; i < m_options.size(); ++i)
            {
                Option opt = m_options.get(i);
                if (opt.IsRequired && !opt.IsHandled)
                {
                    PrintError("Missing required option --%s%s", opt.Name, opt.ShortName != null ? "(-" + opt.ShortName + ")" : "");
                    PrintUsage();
                    return false;
                }
            }
        }

        final String[] args = argsList.size() > 0 ? ArrayUtils.toArray(argsList, String.class) : EMPTY_COMMAND_ARGS;

        Method executeMethod = resolveExecuteMethod(args);
        if (executeMethod == null)
        {
            PrintError("Wrong arguments");
            PrintUsage();
            return false;
        }

        return CCommandUtils.Invoke(this, executeMethod, args);
    }

    private Method resolveExecuteMethod(String[] args)
    {
        if (executeMethods == null)
        {
            executeMethods = resolveExecuteMethods();
        }

        Method method = null;
        for (int i = 0; i < executeMethods.length; ++i)
        {
            if (CCommandUtils.CanInvokeMethodWithArgsCount(executeMethods[i], args.length))
            {
                if (method != null) // multiple methods found
                {
                    return null;
                }

                method = executeMethods[i];
            }
        }

        return method;
    }

    private Method[] resolveExecuteMethods()
    {
        List<Method> result = new ArrayList<Method>();

        ClassUtils.MethodFilter filter = new ClassUtils.MethodFilter()
        {
            @Override
            public boolean accept(Method method)
            {
                if (!method.getName().equals("execute"))
                {
                    return false;
                }

                if (Modifier.isAbstract(method.getModifiers()))
                {
                    return false;
                }

                Class<?> returnType = method.getReturnType();
                return boolean.class.equals(returnType) || void.class.equals(returnType);
            }
        };

        Class<?> type = GetCommandType();
        while (type != null)
        {
            ClassUtils.ListInstanceMethods(result, type, filter);
            type = type.getSuperclass();
        }

        return ArrayUtils.toArray(result, Method.class);
    }

    private Option ParseOption(Iterator<String> iter, String name)
    {
        Option option = FindOption(name);
        if (option != null)
        {
            ParseOption(iter, option);
            return option;
        }
        else
        {
            throw new CCommandException("Can't find option: " + name);
        }
    }

    private void ParseOption(Iterator<String> iter, Option opt)
    {
        Class<?> type = opt.Target.getType();
        opt.IsHandled = true;

        if (int.class.equals(type))
        {
            Object value = CCommandUtils.NextIntArg(iter);
            CheckValue(opt, value);
            opt.setValue(value);
        }
        else if (boolean.class.equals(type))
        {
            opt.setValue(true);
        }
        else if (float.class.equals(type))
        {
            Object value = CCommandUtils.NextFloatArg(iter);
            CheckValue(opt, value);
            opt.setValue(value);
        }
        else if (String.class.equals(type))
        {
            String value = CCommandUtils.NextArg(iter);
            CheckValue(opt, value);
            opt.setValue(value);
        }
        else if (String[].class.equals(type))
        {
            String[] arr = (String[]) opt.getValue();
            if (arr == null)
            {
                throw new CCommandException("Field should be initialized: " + opt.Target.getName());
            }

            for (int i = 0; i < arr.length; ++i)
            {
                arr[i] = CCommandUtils.NextArg(iter);
            }
        }
        else if (int[].class.equals(type))
        {
            int[] arr = (int[]) opt.getValue();
            if (arr == null)
            {
                throw new CCommandException("Field should be initialized: " + opt.Target.getName());
            }

            for (int i = 0; i < arr.length; ++i)
            {
                arr[i] = CCommandUtils.NextIntArg(iter);
            }
        }
        else if (float[].class.equals(type))
        {
            float[] arr = (float[]) opt.getValue();
            if (arr == null)
            {
                throw new CCommandException("Field should be initialized: " + opt.Target.getName());
            }

            for (int i = 0; i < arr.length; ++i)
            {
                arr[i] = CCommandUtils.NextFloatArg(iter);
            }
        }
        else if (boolean[].class.equals(type))
        {
            boolean[] arr = (boolean[]) opt.getValue();
            if (arr == null)
            {
                throw new CCommandException("Field should be initialized: " + opt.Target.getName());
            }

            for (int i = 0; i < arr.length; ++i)
            {
                arr[i] = CCommandUtils.NextBoolArg(iter);
            }
        }
        else
        {
            throw new CCommandException("Unsupported field type: " + type);
        }
    }

    private boolean SkipOption(Iterator<String> iter, String name)
    {
        Option option = FindOption(name);
        return option != null && SkipOption(iter, option);
    }

    private boolean SkipOption(Iterator<String> iter, Option opt)
    {
        Class<?> type = opt.getType();

        if (type.isArray())
        {
            Object arr = opt.getValue();
            if (arr != null)
            {
                int length = Array.getLength(arr);
                Class<?> elementType = type.getComponentType();

                int index = 0;
                if (iter.hasNext() && index++ < length)
                {
                    String value = iter.next();
                    if (!Option.IsValidValue(elementType, value))
                    {
                        if (!iter.hasNext()) // this was the last arg
                        {
                            return false;
                        }

                        throw new CCommandParseException("'%s' is an invalid value for the array option '%s'", value, opt.Name);
                    }
                }

                return index == length;
            }

            return false;
        }

        if (opt.isType(int.class) ||
                opt.isType(float.class) ||
                opt.isType(String.class))
        {
            if (iter.hasNext())
            {
                String value = iter.next();
                if (!opt.IsValidValue(value))
                {
                    if (!iter.hasNext()) // this was the last arg
                    {
                        return false;
                    }

                    throw new CCommandParseException("'%s' is an invalid value for the option '%s'", value, opt.Name);
                }

                return true;
            }

            return false;
        }

        if (boolean.class.equals(type))
        {
            return true;
        }

        return false;
    }

    private void CheckValue(Option opt, Object value)
    {
        if (opt.Values != null)
        {
            for (Object v : opt.Values)
            {
                if (v.equals(value))
                {
                    return;
                }
            }

            String optionDesc = opt.ShortName != null ?
                StringUtils.TryFormat("--{%s}(-{%s})", opt.Name, opt.ShortName) :
                StringUtils.TryFormat("--{%s}", opt.Name);

            StringBuilder buffer = new StringBuilder();
            buffer.append(StringUtils.TryFormat("Invalid value '{%s}' for option {%s}\n", value, optionDesc));
            buffer.append("Valid values:");
            for (Object v : opt.Values)
            {
                buffer.append("\n• ");
                buffer.append(v);
            }

            throw new CCommandException(buffer.toString());
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    void AddOption(Option opt)
    {
        if (m_optionsLookup == null)
        {
            m_optionsLookup = new HashMap<String, Option>();
            m_options = new ArrayList<Option>();
        }

        String name = opt.Name;
        String shortName = opt.ShortName;

        if (m_optionsLookup.containsKey(name))
        {
            Log.e("Option already registered: %s", name);
            return;
        }

        if (shortName != null && m_optionsLookup.containsKey(name))
        {
            Log.e("Short option already registered: %s", shortName);
            return;
        }

        m_optionsLookup.put(name, opt);
        m_options.add(opt);

        if (shortName != null)
        {
            m_optionsLookup.put(shortName, opt);
        }
    }

    List<Option> ListShortOptions()
    {
        return ListShortOptions((String)null);
    }

    List<Option> ListShortOptions(String prefix)
    {
        return ListShortOptions(new ArrayList<Option>(), prefix);
    }

    List<Option> ListShortOptions(List<Option> outList)
    {
        return ListShortOptions(outList, null);
    }

    List<Option> ListShortOptions(List<Option> outList, final String prefix)
    {
        if (!StringUtils.IsNullOrEmpty(prefix))
        {
            return ListOptions(outList, new ListOptionsFilter()
            {
                @Override
                public boolean accept(Option opt)
                {
                    return StringUtils.StartsWithIgnoreCase(opt.ShortName, prefix);
                }
            });
        }

        return ListOptions(outList, DefaultListShortOptionsFilter);
    }

    List<Option> ListOptions()
    {
        return ListOptions((String)null);
    }

    List<Option> ListOptions(String prefix)
    {
        return ListOptions(new ArrayList<Option>(), prefix);
    }

    List<Option> ListOptions(List<Option> outList)
    {
        return ListOptions(outList, (String)null);
    }

    List<Option> ListOptions(List<Option> outList, final String prefix)
    {
        if (!StringUtils.IsNullOrEmpty(prefix))
        {
            return ListOptions(outList, new ListOptionsFilter()
            {
                @Override
                public boolean accept(Option opt)
                {
                    return StringUtils.StartsWithIgnoreCase(opt.Name, prefix);
                }
            });
        }
        return ListOptions(outList, DefaultListOptionsFilter);
    }

    List<Option> ListOptions(List<Option> outList, ListOptionsFilter filter)
    {
        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        if (m_optionsLookup != null)
        {
            for (Option opt : m_options)
            {
                if (filter.accept(opt))
                {
                    outList.add(opt);
                }
            }
        }

        return outList;
    }

    private static final ListOptionsFilter DefaultListOptionsFilter = new ListOptionsFilter()
    {
        @Override
        public boolean accept(Option opt)
        {
            return true;
        }
    };

    private static final ListOptionsFilter DefaultListShortOptionsFilter = new ListOptionsFilter()
    {
        @Override
        public boolean accept(Option opt)
        {
            return opt.ShortName != null;
        }
    };

    Option FindOption(String name)
    {
        if (name.length() == 0)
        {
            return null;
        }

        return m_optionsLookup != null ? m_optionsLookup.get(name) : null;
    }

    Option FindNonAmbiguousOption(String name, boolean useShort)
    {
        if (name.length() == 0)
        {
            return null;
        }

        if (m_options != null)
        {
            Option targetOpt = null;
            for (Option opt : m_options)
            {
                String optName = useShort ? opt.ShortName : opt.Name;
                if (optName == null)
                {
                    continue;
                }

                if (targetOpt == null)
                {
                    if (optName.equalsIgnoreCase(name))
                    {
                        targetOpt = opt;
                    }
                }
                else if (StringUtils.StartsWithIgnoreCase(optName, name))
                {
                    return null;
                }
            }

            return targetOpt;
        }

        return null;
    }

    private void ResetOptions()
    {
        if (m_options != null)
        {
            for (int i = 0; i < m_options.size(); ++i)
            {
                Option opt = m_options.get(i);
                if (opt.IsHandled)
                {
                    ResetOption(opt);
                }
            }
        }
    }

    private void ResetOption(Option opt)
    {
        Class<?> type = opt.Target.getType();
        if (type.isArray() && opt.DefaultValue != null)
        {
            System.arraycopy(opt.DefaultValue, 0, opt.getValue(), 0, Array.getLength(opt.DefaultValue));
        }
        else
        {
            opt.setValue(opt.DefaultValue);
        }
        opt.IsHandled = false;
    }

    //////////////////////////////////////////////////////////////////////////////

    String AutoComplete(String commandLine, List<String> tokens, boolean doubleTab)
    {
        ArrayListIterator<String> iter = new ArrayListIterator<String>((ArrayList<String>) tokens); // TODO: I don't like that
        iter.next(); // first token is a command name

        while (iter.hasNext())
        {
            String token = iter.next();
            int iterPos = iter.getPosition(); // store position to revert for the case if option skip fails

            // first, try to parse options
            if (token.startsWith("--"))
            {
                String optionName = token.substring(2);
                if (iter.hasNext() && SkipOption(iter, optionName)) continue;

                iter.setPosition(iterPos);
                return AutoCompleteOption(commandLine, iter, optionName, "--", doubleTab);
            }
            else if (token.startsWith("-") && !StringUtils.IsNumeric(token))
            {
                String optionName = token.substring(1);
                if (iter.hasNext() && SkipOption(iter, optionName)) continue;

                iter.setPosition(iterPos);
                return AutoCompleteOption(commandLine, iter, optionName, "-", doubleTab);
            }

            return AutoCompleteArgs(commandLine, token, doubleTab);
        }

        return AutoCompleteArgs(commandLine, "", doubleTab);
    }

    private String AutoCompleteOption(String commandLine, ArrayListIterator<String> iter, String optNameToken, String prefix, boolean doubleTab)
    {
        List<Option> optionsList = new ArrayList<Option>();

        // list options
        boolean useShort = prefix.equals("-");
        if (useShort)
        {
            ListShortOptions(optionsList, optNameToken);
            Collections.sort(optionsList, new Comparator<Option>()
            {
                @Override
                public int compare(Option lhs, Option rhs)
                {
                    return lhs.ShortName.compareTo(rhs.ShortName);
                }
            });
        }
        else
        {
            ListOptions(optionsList, optNameToken);
            Collections.sort(optionsList, new Comparator<Option>()
            {
                @Override
                public int compare(Option lhs, Option rhs)
                {
                    return lhs.Name.compareTo(rhs.Name);
                }
            });
        }

        if (optionsList.size() > 1)
        {
            if (doubleTab)
            {
                PrintOptions(commandLine, optionsList, useShort);
            }

            String suggested = GetSuggestedText(optNameToken, optionsList, useShort);
            if (suggested != null)
            {
                String text = commandLine;
                int index = text.lastIndexOf(prefix);
                return text.substring(0, index + prefix.length()) + suggested;
            }
        }
        else if (optionsList.size() == 1)
        {
            Option opt = optionsList.get(0);
            String token = iter.current();
            String optName = useShort ? opt.ShortName : opt.Name;

            int index = commandLine.lastIndexOf(token);
            String newCommandLine = commandLine.substring(0, index + prefix.length()) + optName + " ";

            if (opt.HasValues())
            {
                if (iter.hasNext())
                {
                    String arg = iter.next();
                    String[] values = opt.ListValues(arg);
                    if (values.length > 1)
                    {
                        String suggested = StringUtils.GetSuggestedText(arg, values);
                        if (suggested != null)
                        {
                            newCommandLine += suggested;
                        }

                        if (doubleTab)
                        {
                            Print(values);
                        }
                    }
                    else if (values.length == 1)
                    {
                        newCommandLine += values[0] + " ";
                    }
                }
                else if (doubleTab)
                {
                    Print(opt.Values);
                }
            }

            return newCommandLine.equals(commandLine) ? null : newCommandLine;
        }

        return null;
    }

    private String AutoCompleteOption(String commandLine, String optToken, String argToken, boolean doubleTab, boolean useShort)
    {
        String prefix = useShort ? "-" : "--";
        String optName = optToken.length() > prefix.length() ? optToken.substring(prefix.length()) : null;

        // list options
        List<Option> optionsList;
        if (useShort)
        {
            optionsList = ListShortOptions(optName);
            Collections.sort(optionsList, new Comparator<Option>()
            {
                @Override
                public int compare(Option lhs, Option rhs)
                {
                    return lhs.ShortName.compareTo(rhs.ShortName);
                }
            });
        }
        else
        {
            optionsList = ListOptions(optName);
            Collections.sort(optionsList, new Comparator<Option>()
            {
                @Override
                public int compare(Option lhs, Option rhs)
                {
                    return lhs.Name.compareTo(rhs.Name);
                }
            });
        }

        if (optionsList.size() > 1)
        {
            if (doubleTab)
            {
                PrintOptions(commandLine, optionsList, useShort);
            }
            if (optName != null)
            {
                String text = commandLine;
                int index = text.lastIndexOf(prefix);
                return text.substring(0, index + prefix.length()) + GetSuggestedText(optName, optionsList, useShort);
            }
        }
        else if (optionsList.size() == 1)
        {
            Option opt = optionsList.get(0);
            int index = commandLine.lastIndexOf(prefix);
            String newCommandLine = commandLine.substring(0, index + prefix.length()) +
                    (useShort ? opt.ShortName : opt.Name) + " " +
                    (argToken != null ? argToken : "");

            if (newCommandLine.equals(commandLine))
            {
                return opt.HasValues() ? AutoComplete(commandLine, opt, argToken, doubleTab) : null;
            }

            return newCommandLine;
        }

        return null;
    }

    private String AutoComplete(String commandLine, Option opt, String token, boolean doubleTab)
    {
        if (token != null)
        {
            if (opt.Values != null)
            {
                if (opt.Values.length == 1)
                {
                    String value = opt.Values[0];
                    if (StringUtils.StartsWithIgnoreCase(value, token))
                    {
                        return commandLine + value + " ";
                    }

                    return null;
                }

                String[] strValues = opt.ListValues(token);
                if (doubleTab)
                {
                    Print(strValues);
                }

                String suggested = StringUtils.GetSuggestedText(token, opt.ListValues());
                if (suggested != null)
                {
                    String trimmed = commandLine.trim();
                    return trimmed.substring(0, trimmed.length() - token.length()) + suggested;
                }
            }

            return null;
        }

        if (opt.Values.length == 1)
        {
            return commandLine + opt.Values[0].toString() + " ";
        }

        if (opt.Values.length > 1 && doubleTab)
        {
            Print(opt.Values);
        }

        return null;
    }

    protected String AutoCompleteArgs(String commandLine, String token, boolean doubleTab)
    {
        if (m_values != null && m_values.length > 0)
        {
            return AutoCompleteArgs(commandLine, m_values, token, doubleTab);
        }

        String[] customValues = AutoCompleteArgs(commandLine, token);
        if (customValues != null && customValues.length > 0)
        {
            return AutoCompleteArgs(commandLine, customValues, token, doubleTab);
        }

        return null;
    }

    private String AutoCompleteArgs(String commandLine, String[] values, String token, boolean doubleTab)
    {
        // we need to keep suggested values in a sorted order
        List<String> sortedValues = new ArrayList<String>(values.length);
        for (int i = 0; i < values.length; ++i)
        {
            if (token.length() == 0 || StringUtils.StartsWithIgnoreCase(StringUtils.RemoveRichTextTags(values[i]), token))
            {
                sortedValues.add(values[i]);
            }
        }

        if (sortedValues.size() > 1)
        {
            Collections.sort(sortedValues);

            if (doubleTab)
            {
                PrintPrompt(commandLine);
                Print(sortedValues.toArray(new String[sortedValues.size()]));
            }

            StringUtils.RemoveRichTextTags(sortedValues);

            String suggested = StringUtils.GetSuggestedText(token, sortedValues);
            if (suggested != null)
            {
                String trimmed = commandLine.trim();
                return trimmed.substring(0, trimmed.length() - token.length()) + suggested;
            }
        }
        else if (sortedValues.size() == 1)
        {
            String trimmed = commandLine.trim();
            return trimmed.substring(0, trimmed.length() - token.length()) + StringUtils.RemoveRichTextTags(sortedValues.get(0)) + " ";
        }

        return null;
    }

    protected String[] AutoCompleteArgs(String commandLine, String token)
    {
        return null;
    }

    private void PrintOptions(String commandLine, List<Option> options, boolean useShort)
    {
        if (options.size() > 0)
        {
            PrintPrompt(commandLine);

            String[] names = new String[options.size()];
            for (int i = 0; i < options.size(); ++i)
            {
                if (useShort)
                {
                    names[i] = C("-" + options.get(i).ShortName, ColorCode.TableVar);
                }
                else
                {
                    names[i] = C("--" + options.get(i).Name, ColorCode.TableVar);
                }
            }

            Print(names);
        }
    }

    private String GetSuggestedText(String token, List<Option> options, boolean useShort)
    {
        if (options.size() == 1)
        {
            Option opt = options.get(0);
            return useShort ? opt.ShortName : opt.Name;
        }

        String[] names = new String[options.size()];
        for (int i = 0; i < names.length; ++i)
        {
            names[i] = useShort ? options.get(i).ShortName : options.get(i).Name;
        }

        return StringUtils.GetSuggestedText(token, names);
    }

    void Clear()
    {
        this.Delegate(null);
        this.Args = null;
        this.CommandString = null;
        this.IsManualMode = false;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Output

    static String Prompt(String commandLine)
    {
        return "> " + commandLine;
    }

    void PrintPrompt(String commandLine)
    {
        if (Delegate().IsPromptEnabled())
        {
            Print(Prompt(commandLine));
        }
    }

    /// <summary>
    /// Print the specified message.
    /// </summary>
    protected void Print(String message)
    {
        m_delegate.LogTerminal(message);
    }

    /// <summary>
    /// Print the specified formatted message.
    /// </summary>
    protected void Print(String format, Object... args)
    {
        m_delegate.LogTerminal(StringUtils.TryFormat(format, args));
    }

    /// <summary>
    /// Pretty print the table of Strings.
    ///
    /// Example:
    /// alias            cvar_restart     reset
    /// aliaslist        cvarlist         tag
    /// bind             echo             test
    /// bindlist         exec             throw_exception
    /// break            exit             timeScale
    /// cat              log              toggle
    /// clear            man              unalias
    /// clearprefs       menu             unbind
    /// cmdlist          next             unbindall
    /// colors           prefs            writeconfig
    /// </summary>
    protected void Print(String[] table)
    {
        m_delegate.LogTerminal(table);
    }

    /// <summary>
    /// Prints message with indent.
    /// </summary>
    protected void PrintIndent(String message)
    {
        m_delegate.LogTerminal("  " + message);
    }

    /// <summary>
    /// Prints formatted message with indent.
    /// </summary>
    protected void PrintIndent(String format, Object... args)
    {
        m_delegate.LogTerminal("  " + StringUtils.TryFormat(format, args));
    }

    /// <summary>
    /// Prints error message.
    /// </summary>
    protected void PrintError(String format, Object... args)
    {
        PrintIndent(C(StringUtils.TryFormat(format, args), ColorCode.Error));
    }

    /// <summary>
    /// Prints exceptions.
    /// </summary>
    protected void PrintError(Throwable e)
    {
        PrintError(e, null);
    }

    /// <summary>
    /// Prints exceptions.
    /// </summary>
    protected void PrintError(Throwable e, String format, Object... args)
    {
        PrintError(e, StringUtils.TryFormat(format, args));
    }

    /// <summary>
    /// Prints exceptions.
    /// </summary>
    protected void PrintError(Throwable e, String message)
    {
        m_delegate.LogTerminal(e, message);
    }

    /// <summary>
    /// Prints command's usage.
    /// </summary>
    public void PrintUsage()
    {
        PrintUsage(false);
    }

    /// <summary>
    /// Prints command's usage.
    /// </summary>
    public void PrintUsage(boolean showDescription)
    {
        StringBuilder buffer = new StringBuilder();

        // description
        if (showDescription && this.Description != null)
        {
            buffer.append(StringUtils.TryFormat("  %s\n", this.Description));
        }

        String optionsUsage = m_options != null && m_options.size() > 0 ?
                GetOptionsUsage(m_options) : null;
        String[] argsUsages = GetArgsUsages();

        // name
        if (argsUsages != null && argsUsages.length > 0)
        {
            String name = StringUtils.C(this.Name, ColorCode.TableCommand);

            // first usage line
            buffer.append(StringUtils.TryFormat("  usage: %s", name));
            if (!StringUtils.IsNullOrEmpty(optionsUsage))
            {
                buffer.append(optionsUsage);
            }
            if (argsUsages[0] != null)
            {
                buffer.append(argsUsages[0]);
            }

            // optional usage lines
            for (int i = 1; i < argsUsages.length; ++i)
            {
                buffer.append(StringUtils.TryFormat("\n         %s", name));
                if (!StringUtils.IsNullOrEmpty(optionsUsage))
                {
                    buffer.append(optionsUsage);
                }
                buffer.append(argsUsages[i]);
            }
        }
        else
        {
            buffer.append(StringUtils.C("'execute' method is not resolved", ColorCode.Error));
        }

        Print(buffer.toString());
    }

    private String GetOptionsUsage(List<Option> options)
    {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < options.size(); ++i)
        {
            Option opt = options.get(i);

            buffer.append(' ');

            if (!opt.IsRequired)
            {
                buffer.append('[');
            }

            if (opt.ShortName != null)
            {
                buffer.append(String.format("-%s|", StringUtils.C(opt.ShortName, ColorCode.TableVar)));
            }

            buffer.append(String.format("--%s", StringUtils.C(opt.Name, ColorCode.TableVar)));

            if (!boolean.class.equals(opt.getType()))
            {
                if (opt.HasValues())
                {
                    String[] values = opt.Values;
                    buffer.append(" <");
                    for (int valueIndex = 0; valueIndex < values.length; ++valueIndex)
                    {
                        buffer.append(StringUtils.Arg(values[valueIndex]));
                        if (valueIndex < values.length - 1)
                        {
                            buffer.append("|");
                        }
                    }
                    buffer.append('>');
                }
                else
                {
                    buffer.append(String.format(" <%s>", UsageOptionName(opt)));
                }
            }

            if (!opt.IsRequired)
            {
                buffer.append(']');
            }
        }

        return buffer.toString();
    }

    private String UsageOptionName(Option opt)
    {
        try
        {
            return GetUsageOptionName(opt);
        }
        catch (Exception e)
        {
            return "#err";
        }
    }

    private String GetUsageOptionName(Option opt)
    {
        Class<?> type = opt.getType();
        if (type != null && type.isArray())
        {
            Object arr = opt.getValue();
            if (arr != null)
            {
                int length = Array.getLength(arr);
                String elementTypeName = ClassUtils.TypeShortName(type.getComponentType());

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < length; ++i)
                {
                    result.append(elementTypeName);
                    if (i < length - 1)
                    {
                        result.append(',');
                    }
                }
                return result.toString();
            }
        }

        String typename = ClassUtils.TypeShortName(type);
        return typename != null ? typename : "opt";
    }

    private String[] GetArgsUsages()
    {
        try
        {
            return GetUsageArgs();
        }
        catch (Exception e)
        {
            return new String[] { "#err" };
        }
    }

    protected String[] GetUsageArgs()
    {
        if (m_values != null && m_values.length > 0)
        {
            return new String[] { " " + StringUtils.Join(m_values, "|") };
        }

        if (executeMethods == null)
        {
            executeMethods = resolveExecuteMethods();
        }

        if (executeMethods.length > 0)
        {
            String[] result = new String[executeMethods.length];
            for (int i = 0; i < result.length; ++i)
            {
                result[i] = CCommandUtils.GetMethodParamsUsage(executeMethods[i]);
            }

            return result;
        }

        return null;
    }

    protected boolean ExecCommand(String commandLine)
    {
        return ExecCommand(commandLine, false);
    }

    protected boolean ExecCommand(String commandLine, boolean manualMode)
    {
        return m_delegate.ExecuteCommandLine(commandLine, manualMode);
    }

    protected void PostNotification(String name, Object... data)
    {
        getNotificationCenter().Post(this, name, data);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    public boolean HasFlag(int flag)
    {
        return (Flags & flag) != 0;
    }

    public void SetFlag(int flag, boolean value)
    {
        if (value)
        {
            Flags |= flag;
        }
        else
        {
            Flags &= ~flag;
        }
    }

    boolean StartsWith(String prefix)
    {
        return StringUtils.StartsWithIgnoreCase(Name, prefix);
    }

    static String Arg(String value) { return StringUtils.Arg(value); }

    static String C(String str, ColorCode color) { return StringUtils.C(str, color); }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    public String Name; // FIXME { get; protected set; }
    public String Description; // FIXME { get; set; }

    public Class<? extends CCommand> GetCommandType()
    {
        return getClass();
    }

    public String[] Values() // FIXME
    {
        return m_values;
    }

    public void Values(String... values) // FIXME
    {
        m_values = values;
    }

    public ICCommandDelegate Delegate() // FIXME
    {
        return m_delegate;
    }

    public void Delegate(ICCommandDelegate value) // FIXME
    {
        m_delegate = value != null ? value : NullCommandDelegate.Instance;
    }

    List<String> Args; // FIXME { get; set; }

    public String CommandString; // FIXME { get; set; } // TODO: rename to CommandLine
    public int Flags; // FIXME { get; set; }

    public boolean IsHidden() // FIXME
    {
        return HasFlag(CCommandFlags.Hidden);
    }

    public void IsHidden(boolean value) // FIXME
    {
        SetFlag(CCommandFlags.Hidden, value);
    }

    public boolean IsSystem() // FIXME
    {
        return HasFlag(CCommandFlags.System);
    }

    public void IsSystem(boolean value) // FIXME
    {
        SetFlag(CCommandFlags.System, value);
    }

    public boolean IsDebug() // FIXME
    {
        return HasFlag(CCommandFlags.Debug);
    }

    public void IsDebug(boolean value) // FIXME
    {
        SetFlag(CCommandFlags.Debug, value);
    }

    protected boolean IsManualMode; // FIXME { get; set; }

    public ColorCode ColorCode()
    {
        return ColorCode.TableCommand;
    }

    public void setParentCommand(CCommand parent)
    {
        if (parent != null)
        {
            IsManualMode = parent.IsManualMode;
            Delegate(parent.Delegate());
        }
        else
        {
            Clear();
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Comparable

    @Override
    public int compareTo(CCommand another)
    {
        return Name.compareTo(another.Name);
    }

    //////////////////////////////////////////////////////////////////////////////

    public static class Option
    {
        private final CCommand command;

        public Option(CCommand command, Field field, String name, String description)
        {
            if (command == null)
            {
                throw new NullPointerException("Command is null");
            }

            if (field == null)
            {
                throw new NullPointerException("Field is null");
            }

            if (StringUtils.IsNullOrEmpty(name))
            {
                throw new IllegalArgumentException("Name is null or empty");
            }

            this.command = command;
            this.Target = field;
            this.Name = name;
            this.Description = description;
        }

        public boolean IsValidValue(String value)
        {
            if (HasValues())
            {
                return ArrayUtils.IndexOf(Values, value) != -1;
            }

            return IsValidValue(getType(), value);
        }

        static boolean IsValidValue(Class<?> type, String value)
        {
            if (String.class.equals(type))
            {
                if (value.startsWith("--")) // can't be long option
                {
                    return false;
                }
                if (value.startsWith("-")) // can't be short option
                {
                    return StringUtils.IsNumeric(value); // but can be a negative number
                }
                if (value.equals("&&") || value.equals("||"))
                {
                    return false;
                }

                return value.length() > 0; // can't be empty
            }

            if (int.class.equals(type))
            {
                return StringUtils.IsInteger(value);
            }

            if (float.class.equals(type))
            {
                return StringUtils.IsNumeric(value);
            }

            return false;
        }

        public boolean HasValues()
        {
            return Values != null && Values.length > 0;
        }

        //////////////////////////////////////////////////////////////////////////////
        // Properties

        private Field Target; // FIXME: { get; private set; }

        public String Name; // FIXME: { get; protected set; }
        public String Description; // FIXME: { get; protected set; }
        public Object DefaultValue; // FIXME: { get; set; }

        public String ShortName; // FIXME: { get; set; }
        public boolean IsRequired; // FIXME: { get; set; }
        public boolean IsHandled; // FIXME: { get; set; }

        public String[] Values; // FIXME: { get; set; }

        public String[] ListValues()
        {
            return ListValues(null);
        }

        public String[] ListValues(String token)
        {
            if (HasValues())
            {
                List<String> list = new ArrayList<String>(Values.length);
                for (int i = 0; i < Values.length; ++i)
                {
                    String str = Values[i];
                    if (token == null || StringUtils.StartsWithIgnoreCase(str, token))
                    {
                        list.add(str);
                    }
                }

                return list.toArray(new String[list.size()]);
            }

            return new String[0];
        }

        public Class<?> getType()
        {
            return Target.getType();
        }

        public boolean isType(Class<?> type)
        {
            return getType().equals(type);
        }

        @Override
        public String toString()
        {
            StringBuilder result = new StringBuilder();
            result.append(Target.getType().getName());
            result.append(" ");
            result.append(Name);
            if (ShortName != null)
            {
                result.append("(");
                result.append(ShortName);
                result.append(")");
            }

            if (DefaultValue != null)
            {
                result.append(" = ");
                result.append(DefaultValue.toString());
            }

            return result.toString();
        }

        public Object getValue()
        {
            try
            {
                return Target.get(command);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e); // FIXME: throw real exception
            }
        }

        public void setValue(Object value)
        {
            try
            {
                Target.set(command, value);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e); // FIXME: throw real exception
            }
        }
    }
}