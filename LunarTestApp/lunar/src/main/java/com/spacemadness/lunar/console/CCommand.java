package com.spacemadness.lunar.console;

import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.List;

import com.spacemadness.lunar.utils.ReusableList;
import com.spacemadness.lunar.utils.ReusableLists;
import com.spacemadness.lunar.utils.StringUtils;

/**
 * Created by alementuev on 5/28/15.
 */
public abstract class CCommand // FIXME: IComparable<CCommand>
{
    private static final String[] EMPTY_COMMAND_ARGS = new String[0];

    private Map<String, Option> m_optionsLookup;

    private List<Option> m_options;
    private String[] m_values;

    private ICCommandDelegate m_delegate;

    public CCommand()
    {
        this.Delegate = null;
    }

    public CCommand(String name)
    {
        this();

        if (name == null)
        {
            throw new ArgumentNullException("Name is null");
        }
        this.Name = name;
    }

    static void ResolveOptions(CCommand command)
    {
        RuntimeResolver.ResolveOptions(command);
    }

    static void ResolveOptions(CCommand command, Type commandType)
    {
        RuntimeResolver.ResolveOptions(command, commandType);
    }

    boolean ExecuteTokens(List<String> tokens, String commandLine = null)
    {
        try
        {
            return ExecuteGuarded(tokens, commandLine);
        }
        catch (CCommandException e)
        {
            PrintError(e.getMessage());
        }
        catch (TargetInvocationException e)
        {
            if (e.InnerException is CCommandException)
            {
                PrintError(e.InnerException.Message);
            }
            else
            {
                PrintError(e.InnerException, "Error while executing command");
            }
        }
        catch (Exception e)
        {
            PrintError(e, "Error while executing command");
        }

        return false;
    }

    private boolean ExecuteGuarded(List<String> tokens, String commandLine = null)
    {
        ResetOptions();

        Iterator<String> iter = tokens.iterator();
        iter.next(); // first token is a command name

        if (this.IsManualMode)
        {
            PrintPrompt(commandLine);
        }

        // FIXME: remove play-only functionality
        // if (this.IsPlayModeOnly && !Runtime.IsPlaying)
        // {
        //    PrintError("Command is available in the play mode only");
        //    return false;
        // }

        ReusableList<String> argsList = ReusableLists.NextAutoRecycleList<String>();
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
                argsList.Add(token);
                while (iter.hasNext())
                {
                    token = StringUtils.UnArg(iter.next());
                    argsList.Add(token);
                }

                break;
            }
        }

        if (m_values != null)
        {
            if (argsList.Count() != 1)
            {
                PrintError("Unexpected arguments count {0}", argsList.Count());
                PrintUsage();
                return false;
            }

            String arg = argsList.get(0);
            if (Array.IndexOf(m_values, arg) == -1)
            {
                PrintError("Unexpected argument '{0}'", arg);
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
                    PrintError("Missing required option --{0}{1}", opt.Name, opt.ShortName != null ? "(-" + opt.ShortName + ")" : "");
                    PrintUsage();
                    return false;
                }
            }
        }

        String[] args = argsList.Count() > 0 ? argsList.ToArray() : EMPTY_COMMAND_ARGS;
        MethodInfo[] methods = ClassUtils.ListInstanceMethods(GetCommandType(), delegate(MethodInfo method)
        {
            if (method.Name != "Execute")
            {
                return false;
            }

            if (method.IsAbstract)
            {
                return false;
            }

            return CCommandUtils.CanInvokeMethodWithArgsCount(method, args.Length);
        });

        if (methods.length != 1)
        {
            PrintError("Wrong arguments");
            PrintUsage();
            return false;
        }

        return CCommandUtils.Invoke(this, methods[0], args);
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
    }

    private boolean SkipOption(Iterator<String> iter, String name)
    {
        Option option = FindOption(name);
        return option != null && SkipOption(iter, option);
    }

    private boolean SkipOption(Iterator<String> iter, Option opt)
    {
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

                        throw new CCommandParseException("'{0}' is an invalid value for the array option '{1}'", value, opt.Name);
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

                    throw new CCommandParseException("'{0}' is an invalid value for the option '{1}'", value, opt.Name);
                }
            }

            return false;
        }

        if (type == typeof(boolean))
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
                StringUtils.TryFormat("--{0}(-{1})", opt.Name, opt.ShortName) :
                StringUtils.TryFormat("--{0}", opt.Name);

            StringBuilder buffer = new StringBuilder();
            buffer.AppendFormat("Invalid value '{0}' for option {1}\n", value, optionDesc);
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
            m_optionsLookup = new Map<String, Option>();
            m_options = new List<Option>();
        }

        String name = opt.Name;
        String shortName = opt.ShortName;

        if (m_optionsLookup.containsKey(name))
        {
            Log.e("Option already registered: {0}", name);
            return;
        }

        if (shortName != null && m_optionsLookup.containsKey(name))
        {
            Log.e("Short option already registered: {0}", shortName);
            return;
        }

        m_optionsLookup.put(name, opt);
        m_options.add(opt);

        if (shortName != null)
        {
            m_optionsLookup.put(shortName, opt);
        }
    }

    List<Option> ListShortOptions(String prefix = null)
    {
        return ListShortOptions(ReusableLists.NextAutoRecycleList<Option>(), prefix);
    }

    List<Option> ListShortOptions(List<Option> outList, String prefix = null)
    {
        if (!StringUtils.IsNullOrEmpty(prefix))
        {
            return ListOptions(outList, delegate(Option opt) {
                return StringUtils.StartsWithIgnoreCase(opt.ShortName, prefix);
            });
        }

        return ListOptions(outList, DefaultListShortOptionsFilter);
    }

    List<Option> ListOptions(String prefix = null)
    {
        return ListOptions(ReusableLists.NextAutoRecycleList<Option>(), prefix);
    }

    List<Option> ListOptions(List<Option> outList, String prefix = null)
    {
        if (!StringUtils.IsNullOrEmpty(prefix))
        {
            return ListOptions(outList, delegate(Option opt) {
                return StringUtils.StartsWithIgnoreCase(opt.Name, prefix);
            });
        }
        return ListOptions(outList, DefaultListOptionsFilter);
    }

    List<Option> ListOptions(List<Option> outList, ListOptionsFilter filter)
    {
        if (filter == null)
        {
            throw new NullReferenceException("Filter is null");
        }

        if (m_optionsLookup != null)
        {
            for (Option opt : m_options)
            {
                if (filter(opt))
                {
                    outList.add(opt);
                }
            }
        }

        return outList;
    }

    private static boolean DefaultListOptionsFilter(Option opt)
    {
        return true;
    }

    private static boolean DefaultListShortOptionsFilter(Option opt)
    {
        return opt.ShortName != null;
    }

    private Option FindOption(String name)
    {
        if (name.length == 0)
        {
            return null;
        }

        Option option;
        if (m_optionsLookup != null)
        {
            if (m_optionsLookup.TryGetValue(name, out option))
            {
                return option;
            }
        }

        return null;
    }

    private void ResetOptions()
    {
        if (m_options != null)
        {
            for (int i = 0; i < m_options.Count; ++i)
            {
                Option opt = m_options[i];
                if (opt.IsHandled)
                {
                    ResetOption(opt);
                }
            }
        }
    }

    private void ResetOption(Option opt)
    {
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
    }

    //////////////////////////////////////////////////////////////////////////////

    String AutoComplete(String commandLine, List<String> tokens, boolean doubleTab)
    {
        Iterator<String> iter = new Iterator<String>(tokens);
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

        return AutoCompleteArgs(commandLine, "", doubleTab);;
    }

    private String AutoCompleteOption(String commandLine, Iterator<String> iter, String optNameToken, String prefix, boolean doubleTab)
    {
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
    }

    private String AutoCompleteOption(String commandLine, String optToken, String argToken, boolean doubleTab, boolean useShort)
    {
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
    }

    private String AutoComplete(String commandLine, Option opt, String token, boolean doubleTab)
    {
        if (token != null)
        {
            if (opt.Values != null)
            {
                if (opt.Values.Length == 1)
                {
                    String value = opt.Values[0];
                    if (value.StartsWith(token))
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
                    String trimmed = commandLine.TrimEnd();
                    return trimmed.SubString(0, trimmed.Length - token.Length) + suggested;
                }
            }

            return null;
        }

        if (opt.Values.Length == 1)
        {
            return commandLine + opt.Values[0].ToString() + " ";
        }

        if (opt.Values.Length > 1 && doubleTab)
        {
            Print(opt.Values);
        }

        return null;
    }

    protected String AutoCompleteArgs(String commandLine, String token, boolean doubleTab)
    {
        if (m_values != null && m_values.Length > 0)
        {
            return AutoCompleteArgs(commandLine, m_values, token, doubleTab);
        }

        String[] customValues = AutoCompleteArgs(commandLine, token);
        if (customValues != null && customValues.Length > 0)
        {
            return AutoCompleteArgs(commandLine, customValues, token, doubleTab);
        }

        return null;
    }

    private String AutoCompleteArgs(String commandLine, String[] values, String token, boolean doubleTab)
    {
        // we need to keep suggested values in a sorted order
        List<String> sortedValues = new List<String>(values.Length);
        for (int i = 0; i < values.Length; ++i)
        {
            if (token.Length == 0 || StringUtils.StartsWithIgnoreCase(StringUtils.RemoveRichTextTags(values[i]), token))
            {
                sortedValues.Add(values[i]);
            }
        }

        if (sortedValues.Count > 1)
        {
            sortedValues.Sort();

            if (doubleTab)
            {
                PrintPrompt(commandLine);
                Print(sortedValues.ToArray());
            }

            StringUtils.RemoveRichTextTags(sortedValues);

            String suggested = StringUtils.GetSuggestedText(token, sortedValues);
            if (suggested != null)
            {
                String trimmed = commandLine.TrimEnd();
                return trimmed.SubString(0, trimmed.Length - token.Length) + suggested;
            }
        }
        else if (sortedValues.Count == 1)
        {
            String trimmed = commandLine.TrimEnd();
            return trimmed.SubString(0, trimmed.Length - token.Length) + StringUtils.RemoveRichTextTags(sortedValues[0]) + " ";
        }

        return null;
    }

    protected String[] AutoCompleteArgs(String commandLine, String token)
    {
        return null;
    }

    private void PrintOptions(String commandLine, List<Option> options, boolean useShort)
    {
        if (options.Count > 0)
        {
            PrintPrompt(commandLine);

            String[] names = new String[options.Count];
            for (int i = 0; i < options.Count; ++i)
            {
                if (useShort)
                {
                    names[i] = C("-" + options[i].ShortName, ColorCode.TableVar);
                }
                else
                {
                    names[i] = C("--" + options[i].Name, ColorCode.TableVar);
                }
            }

            System.Array.Sort(names);
            Print(names);
        }
    }

    private String GetSuggestedText(String token, List<Option> options, boolean useShort)
    {
        if (options.Count == 1)
        {
            Option opt = options[0];
            return useShort ? opt.ShortName : opt.Name;
        }

        String[] names = new String[options.Count];
        for (int i = 0; i < names.Length; ++i)
        {
            names[i] = useShort ? options[i].ShortName : options[i].Name;
        }

        return StringUtils.GetSuggestedTextFiltered(token, names);
    }

    void Clear()
    {
        this.Delegate = null;
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
        if (Delegate.IsPromptEnabled)
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
    protected void Print(String format, params Object[] args)
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
    protected void PrintIndent(String format, params Object[] args)
    {
        m_delegate.LogTerminal("  " + StringUtils.TryFormat(format, args));
    }

    /// <summary>
    /// Prints error message.
    /// </summary>
    protected void PrintError(String format, params Object[] args)
    {
        PrintIndent(C(StringUtils.TryFormat(format, args), ColorCode.Error));
    }

    /// <summary>
    /// Prints exceptions.
    /// </summary>
    protected void PrintError(Exception e)
    {
        PrintError(e, null);
    }

    /// <summary>
    /// Prints exceptions.
    /// </summary>
    protected void PrintError(Exception e, String format, params Object[] args)
    {
        PrintError(e, StringUtils.TryFormat(format, args));
    }

    /// <summary>
    /// Prints exceptions.
    /// </summary>
    protected void PrintError(Exception e, String message)
    {
        m_delegate.LogTerminal(e, message);
    }

    /// <summary>
    /// Prints command's usage.
    /// </summary>
    public void PrintUsage(boolean showDescription = false)
    {
        StringBuilder buffer = new StringBuilder();

        // description
        if (showDescription && this.Description != null)
        {
            buffer.AppendFormat("  {0}\n", this.Description);
        }

        String optionsUsage = GetOptionsUsage(m_options);
        String[] argsUsages = GetArgsUsages();

        // name
        if (argsUsages != null && argsUsages.Length > 0)
        {
            String name = StringUtils.C(this.Name, ColorCode.TableCommand);

            // first usage line
            buffer.AppendFormat("  usage: {0}", name);
            if (!StringUtils.IsNullOrEmpty(optionsUsage))
            {
                buffer.Append(optionsUsage);
            }
            buffer.Append(argsUsages[0]);

            // optional usage lines
            for (int i = 1; i < argsUsages.Length; ++i)
            {
                buffer.AppendFormat("\n         {0}", name);
                if (!StringUtils.IsNullOrEmpty(optionsUsage))
                {
                    buffer.Append(optionsUsage);
                }
                buffer.Append(argsUsages[i]);
            }
        }
        else
        {
            buffer.Append(StringUtils.C("'Execute' method is not resolved", ColorCode.Error));
        }

        Print(buffer.ToString());
    }

    private String GetOptionsUsage(List<Option> options)
    {
        if (options != null && options.Count > 0)
        {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < options.Count; ++i)
            {
                Option opt = options[i];

                buffer.Append(' ');

                if (!opt.IsRequired)
                {
                    buffer.Append('[');
                }

                if (opt.ShortName != null)
                {
                    buffer.AppendFormat("-{0}|", StringUtils.C(opt.ShortName, ColorCode.TableVar));
                }

                buffer.AppendFormat("--{0}", StringUtils.C(opt.Name, ColorCode.TableVar));

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
                        buffer.AppendFormat(" <{0}>", UsageOptionName(opt));
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
    }

    private String UsageOptionName(Option opt)
    {
        try
        {
            return GetUsageOptionName(opt);
        }
        catch
        {
            return "#err";
        }
    }

    private String GetUsageOptionName(Option opt)
    {
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
    }

    private String[] GetArgsUsages()
    {
        try
        {
            return GetUsageArgs();
        }
        catch
        {
            return new String[] { "#err" };
        }
    }

    protected String[] GetUsageArgs()
    {
        if (m_values != null && m_values.Length > 0)
        {
            return new String[] { " " + StringUtils.Join(m_values, "|") };
        }

        MethodInfo[] executeMethods = ClassUtils.ListInstanceMethods(GetCommandType(), delegate(MethodInfo method)
        {
            if (method.Name != "Execute")
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
    }

    void ClearTerminal()
    {
        m_delegate.ClearTerminal();
    }

    protected boolean ExecCommand(String commandLine, boolean manualMode = false)
    {
        return m_delegate.ExecuteCommandLine(commandLine, manualMode);
    }

    void PostNotification(String name, params Object[] data)
    {
        m_delegate.PostNotification(this, name, data);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    public boolean HasFlag(CCommandFlags flag)
    {
        return (Flags & flag) != 0;
    }

    public void SetFlag(CCommandFlags flag, boolean value)
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
        if (prefix.Length <= Name.Length)
        {
            for (int i = 0; i < prefix.Length; ++i)
            {
                char pc = char.ToLower(prefix[i]);
                char nc = char.ToLower(Name[i]);

                if (pc != nc) return false;
            }

            return true;
        }

        return false;
    }

    static String Arg(String value) { return StringUtils.Arg(value); }

    static String C(String str, ColorCode color) { return StringUtils.C(str, color); }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    public String Name { get; protected set; }
    public String Description { get; set; }

    public Type GetCommandType()
    {
        return GetType();
    }

    public String[] Values
    {
        get { return m_values; }
        set { m_values = value; }
    }

    ICCommandDelegate Delegate
    {
        get { return m_delegate; }
        set { m_delegate = value != null ? value : NullCommandDelegate.Instance; }
    }

    List<String> Args { get; set; }

    public String CommandString { get; set; } // TODO: rename to CommandLine
    public CCommandFlags Flags { get; set; }

    public boolean IsHidden
    {
        get { return HasFlag(CCommandFlags.Hidden); }
        set { SetFlag(CCommandFlags.Hidden, value); }
    }

    public boolean IsSystem
    {
        get { return HasFlag(CCommandFlags.System); }
        set { SetFlag(CCommandFlags.System, value); }
    }

    public boolean IsDebug
    {
        get { return HasFlag(CCommandFlags.Debug); }
        set { SetFlag(CCommandFlags.Debug, value); }
    }

    public boolean IsPlayModeOnly
    {
        get { return HasFlag(CCommandFlags.PlayModeOnly); }
        set { SetFlag(CCommandFlags.PlayModeOnly, value); }
    }

    boolean IsManualMode { get; set; }

    ColorCode ColorCode
    {
        get { return IsPlayModeOnly && !Runtime.IsPlaying ? ColorCode.TableCommandDisabled : ColorCode.TableCommand; }
    }

    //////////////////////////////////////////////////////////////////////////////
    // IComparable

    public int CompareTo(CCommand other)
    {
        return Name.CompareTo(other.Name);
    }

    //////////////////////////////////////////////////////////////////////////////

    static class Option
    {
        public Option(FieldInfo target, String name, String description)
        {
            Target = target;
            Name = name;
            Description = description;
        }

        public boolean IsValidValue(String value)
        {
            if (HasValues())
            {
                return Array.IndexOf(Values, value) != -1;
            }

            return IsValidValue(Target.FieldType, value);
        }

        static boolean IsValidValue(Type type, String value)
        {
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
        }

        public boolean HasValues()
        {
            return Values != null && Values.Length > 0;
        }

        //////////////////////////////////////////////////////////////////////////////
        // Properties

        public FieldInfo Target { get; private set; }

        public String Name { get; protected set; }
        public String Description { get; protected set; }
        public Type Type { get { return Target != null ? Target.FieldType : null; } }
        public Object DefaultValue { get; set; }

        public String ShortName { get; set; }
        public boolean IsRequired { get; set; }
        public boolean IsHandled { get; set; }

        public String[] Values { get; set; }

        public String[] ListValues(String token = null)
        {
            if (HasValues())
            {
                List<String> list = new List<String>(Values.Length);
                for (int i = 0; i < Values.Length; ++i)
                {
                    String str = Values[i];
                    if (token == null || str.StartsWith(token, false, null))
                    {
                        list.Add(str);
                    }
                }

                return list.ToArray();
            }

            return new String[0];
        }
    }
}