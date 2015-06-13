package com.spacemadness.lunar.console;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.ArrayUtils;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        ResolveOptions();
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

    private void ResolveOptions()
    {
        RuntimeResolver.ResolveOptions(this);
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
        /*
        Type type = opt.Target.FieldType;
        opt.IsHandled = true;

        if (type == typeof(int))
        {
            Object value = CCommandUtils.NextIntArg(iter);
            CheckValue(opt, value);
            opt.Target.SetValue(this, value);
        }
        else if (type == typeof(boolean))
        {
            opt.Target.SetValue(this, true);
        }
        else if (type == typeof(float))
        {
            Object value = CCommandUtils.NextFloatArg(iter);
            CheckValue(opt, value);
            opt.Target.SetValue(this, value);
        }
        else if (type == typeof(String))
        {
            String value = CCommandUtils.NextArg(iter);
            CheckValue(opt, value);
            opt.Target.SetValue(this, value);
        }
        else if (type == typeof(String[]))
        {
            String[] arr = (String[]) opt.Target.GetValue(this);
            if (arr == null)
            {
                throw new CCommandException("Field should be initialized: " + opt.Target.Name);
            }

            for (int i = 0; i < arr.Length; ++i)
            {
                arr[i] = CCommandUtils.NextArg(iter);
            }
        }
        else if (type == typeof(int[]))
        {
            int[] arr = (int[]) opt.Target.GetValue(this);
            if (arr == null)
            {
                throw new CCommandException("Field should be initialized: " + opt.Target.Name);
            }

            for (int i = 0; i < arr.length; ++i)
            {
                arr[i] = CCommandUtils.NextIntArg(iter);
            }
        }
        else if (type == typeof(float[]))
        {
            float[] arr = (float[]) opt.Target.GetValue(this);
            if (arr == null)
            {
                throw new CCommandException("Field should be initialized: " + opt.Target.Name);
            }

            for (int i = 0; i < arr.length; ++i)
            {
                arr[i] = CCommandUtils.NextFloatArg(iter);
            }
        }
        else if (type == typeof(boolean[]))
        {
            boolean[] arr = (boolean[]) opt.Target.GetValue(this);
            if (arr == null)
            {
                throw new CCommandException("Field should be initialized: " + opt.Target.Name);
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
        */

        throw new NotImplementedException();
    }

    private boolean SkipOption(Iterator<String> iter, String name)
    {
        Option option = FindOption(name);
        return option != null && SkipOption(iter, option);
    }

    private boolean SkipOption(Iterator<String> iter, Option opt)
    {
        /*
        Type type = opt.Target.FieldType;

        if (type.IsArray)
        {
            Array arr = (Array) opt.Target.GetValue(this);
            if (arr != null)
            {
                int length = arr.Length;
                Type elementType = arr.GetType().GetElementType();

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

        if (type == typeof(int) ||
            type == typeof(float) ||
            type == typeof(String))
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
            }

            return false;
        }

        if (type == typeof(boolean))
        {
            return true;
        }

        return false;
        */
        throw new NotImplementedException();
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

    List<Option> ListShortOptions(List<Option> outList, String prefix)
    {
        /*
        if (!StringUtils.IsNullOrEmpty(prefix))
        {
            return ListOptions(outList, delegate(Option opt) {
                return StringUtils.StartsWithIgnoreCase(opt.ShortName, prefix);
            });
        }

        return ListOptions(outList, DefaultListShortOptionsFilter);
        */

        throw new NotImplementedException(); // FIXME
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

    private Option FindOption(String name)
    {
        if (name.length() == 0)
        {
            return null;
        }

        return m_optionsLookup != null ? m_optionsLookup.get(name) : null;
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
        /*
        Type type = opt.Target.FieldType;
        if (type.IsArray)
        {
            Array source = (Array)opt.DefaultValue;
            Array target = (Array)opt.Target.GetValue(this);
            Array.Copy(source, target, source.Length);
        }
        else
        {
            opt.Target.SetValue(this, opt.DefaultValue);
        }
        opt.IsHandled = false;
        */
        throw new NotImplementedException(); // FIXME
    }

    //////////////////////////////////////////////////////////////////////////////

    String AutoComplete(String commandLine, List<String> tokens, boolean doubleTab)
    {
        /*
        Iterator<String> iter = tokens.iterator();
        iter.next(); // first token is a command name

        while (iter.hasNext())
        {
            String token = iter.next();
            int iterPos = iter.Position; // store position to revert for the case if option skip fails

            // first, try to parse options
            if (token.startsWith("--"))
            {
                String optionName = token.substring(2);
                if (iter.hasNext() && SkipOption(iter, optionName)) continue;

                iter.Position = iterPos;
                return AutoCompleteOption(commandLine, iter, optionName, "--", doubleTab);
            }
            else if (token.startsWith("-") && !StringUtils.IsNumeric(token))
            {
                String optionName = token.substring(1);
                if (iter.hasNext() && SkipOption(iter, optionName)) continue;

                iter.Position = iterPos;
                return AutoCompleteOption(commandLine, iter, optionName, "-", doubleTab);
            }

            return AutoCompleteArgs(commandLine, token, doubleTab);
        }

        return AutoCompleteArgs(commandLine, "", doubleTab);
        */

        throw new NotImplementedException(); // FIXME
    }

    private String AutoCompleteOption(String commandLine, Iterator<String> iter, String optNameToken, String prefix, boolean doubleTab)
    {
        /*
        ReusableList<Option> optionsList = ReusableLists.NextAutoRecycleList<Option>();

        // list options
        boolean useShort = false;
        useShort = prefix.equals("-");
        if (useShort)
        {
            ListShortOptions(optionsList, optNameToken);
            optionsList.Sort(delegate(Option op1, Option op2) {
                return op1.ShortName.CompareTo(op2.ShortName);
            });
        }
        else
        {
            ListOptions(optionsList, optNameToken);
            optionsList.Sort(delegate(Option op1, Option op2) {
                return op1.Name.CompareTo(op2.Name);
            });
        }

        if (optionsList.Count > 1)
        {
            if (doubleTab)
            {
                PrintOptions(commandLine, optionsList, useShort);
            }

            String suggested = GetSuggestedText(optNameToken, optionsList, useShort);
            if (suggested != null)
            {
                String text = commandLine;
                int index = text.LastIndexOf(prefix);
                return text.SubString(0, index + prefix.Length) + suggested;
            }
        }
        else if (optionsList.Count == 1)
        {
            Option opt = optionsList[0];
            String token = iter.Current();
            String optName = useShort ? opt.ShortName : opt.Name;

            int index = commandLine.LastIndexOf(token);
            String newCommandLine = commandLine.SubString(0, index + prefix.Length) + optName + " ";

            if (opt.HasValues())
            {
                if (iter.hasNext())
                {
                    String arg = iter.next();
                    String[] values = opt.ListValues(arg);
                    if (values.Length > 1)
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
                    else if (values.Length == 1)
                    {
                        newCommandLine += values[0] + " ";
                    }
                }
                else if (doubleTab)
                {
                    Print(opt.Values);
                }
            }

            return newCommandLine.Equals(commandLine) ? null : newCommandLine;
        }

        return null;
        */

        throw new NotImplementedException(); // FIXME
    }

    private String AutoCompleteOption(String commandLine, String optToken, String argToken, boolean doubleTab, boolean useShort)
    {
        /*
        String prefix = useShort ? "-" : "--";
        String optName = optToken.Length > prefix.Length ? optToken.SubString(prefix.Length) : null;

        // list options
        List<Option> optionsList;
        if (useShort)
        {
            optionsList = ListShortOptions(optName);
            ListUtils.Sort(optionsList, delegate(Option op1, Option op2) {
                return op1.ShortName.CompareTo(op2.ShortName);
            });
        }
        else
        {
            optionsList = ListOptions(optName);
            ListUtils.Sort(optionsList, delegate(Option op1, Option op2) {
                return op1.Name.CompareTo(op2.Name);
            });
        }

        if (optionsList.Count > 1)
        {
            if (doubleTab)
            {
                PrintOptions(commandLine, optionsList, useShort);
            }
            if (optName != null)
            {
                String text = commandLine;
                int index = text.LastIndexOf(prefix);
                return text.SubString(0, index + prefix.Length) + GetSuggestedText(optName, optionsList, useShort);
            }
        }
        else if (optionsList.Count == 1)
        {
            Option opt = optionsList[0];
            int index = commandLine.LastIndexOf(prefix);
            String newCommandLine = commandLine.SubString(0, index + prefix.Length) +
                    (useShort ? opt.ShortName : opt.Name) + " " +
                    (argToken != null ? argToken : "");

            if (newCommandLine.Equals(commandLine))
            {
                return opt.HasValues() ? AutoComplete(commandLine, opt, argToken, doubleTab) : null;
            }

            return newCommandLine;
        }

        return null;
        */

        throw new NotImplementedException(); // FIXME
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

            Arrays.sort(names);
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

        return StringUtils.GetSuggestedTextFiltered(token, names);
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

        String optionsUsage = GetOptionsUsage(m_options);
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
            buffer.append(argsUsages[0]);

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
        /*
        if (options != null && options.size() > 0)
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
                    buffer.AppendFormat("-%s|", StringUtils.C(opt.ShortName, ColorCode.TableVar));
                }

                buffer.AppendFormat("--%s", StringUtils.C(opt.Name, ColorCode.TableVar));

                if (opt.Type != typeof(boolean))
                {
                    if (opt.HasValues())
                    {
                        String[] values = opt.Values;
                        buffer.Append(" <");
                        for (int valueIndex = 0; valueIndex < values.Length; ++valueIndex)
                        {
                            buffer.Append(StringUtils.Arg(values[valueIndex]));
                            if (valueIndex < values.Length - 1)
                            {
                                buffer.Append("|");
                            }
                        }
                        buffer.Append('>');
                    }
                    else
                    {
                        buffer.AppendFormat(" <%s>", UsageOptionName(opt));
                    }
                }

                if (!opt.IsRequired)
                {
                    buffer.Append(']');
                }
            }

            return buffer.ToString();
        }

        return null;
        */

        throw new NotImplementedException();
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
        /*
        Type type = opt.Type;
        if (type != null && type.IsArray)
        {
            Array arr = (Array) opt.Target.GetValue(this);
            if (arr != null)
            {
                int length = arr.Length;
                String elementTypeName = ClassUtils.TypeShortName(arr.GetType().GetElementType());

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < length; ++i)
                {
                    result.Append(elementTypeName);
                    if (i < length - 1)
                    {
                        result.Append(',');
                    }
                }
                return result.ToString();
            }
        }

        String typename = ClassUtils.TypeShortName(type);
        return typename != null ? typename : "opt";
        */

        throw new NotImplementedException();
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
        /*
        if (m_values != null && m_values.length > 0)
        {
            return new String[] { " " + StringUtils.Join(m_values, "|") };
        }

        Method[] executeMethods = ClassUtils.ListInstanceMethods(GetCommandType(), delegate(Method method)
        {
            if (method.Name != "execute")
            {
                return false;
            }

            if (method.IsAbstract)
            {
                return false;
            }

            Type returnType = method.ReturnType;
            if (returnType != typeof(boolean) && returnType != typeof(void))
            {
                return false;
            }

            return true;
        });

        if (executeMethods.Length > 0)
        {
            String[] result = new String[executeMethods.Length];
            for (int i = 0; i < result.Length; ++i)
            {
                result[i] = CCommandUtils.GetMethodParamsUsage(executeMethods[i]);
            }

            return result;
        }

        return null;
        */

        throw new NotImplementedException();
    }

    protected void ClearTerminal()
    {
        m_delegate.ClearTerminal();
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
        m_delegate.PostNotification(this, name, data);
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

    public void Values(String[] values) // FIXME
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
        public Option(Field field, String name, String description)
        {
            Target = field;
            Name = name;
            Description = description;
        }

        public boolean IsValidValue(String value)
        {
            /*
            if (HasValues())
            {
                return ArrayUtils.IndexOf(Values, value) != -1;
            }

            return IsValidValue(Target.FieldType, value);
            */

            throw new NotImplementedException(); // FIXME
        }

        static boolean IsValidValue(Class<?> type, String value)
        {
            /*
            if (type == typeof(String))
            {
                if (value.StartsWith("--")) // can't be long option
                {
                    return false;
                }
                if (value.StartsWith("-")) // can't be short option
                {
                    return StringUtils.IsNumeric(value); // but can be a negative number
                }
                return true;
            }

            if (type == typeof(int))
            {
                return StringUtils.IsInteger(value);
            }

            if (type == typeof(float))
            {
                return StringUtils.IsNumeric(value);
            }

            return false;
            */

            throw new NotImplementedException(); // FIXME
        }

        public boolean HasValues()
        {
            return Values != null && Values.length > 0;
        }

        //////////////////////////////////////////////////////////////////////////////
        // Properties

        public Field Target; // FIXME: { get; private set; }

        public String Name; // FIXME: { get; protected set; }
        public String Description; // FIXME: { get; protected set; }
        public Class<?> Type;  // FIXME: { get { return Target != null ? Target.FieldType : null; } }
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
    }
}