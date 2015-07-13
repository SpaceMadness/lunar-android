package com.spacemadness.lunar.console;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.core.ArrayListIterator;
import com.spacemadness.lunar.utils.ArrayUtils;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.spacemadness.lunar.console.CCommand.Option;

public class CommandAutocompletion
{
    private static final String[] EMPTY_SUGGESTIONS = new String[0];
    private static final String[] SINGLE_SUGGESTION = new String[1];

    public static String[] getSuggestions(String line)
    {
        return getSuggestions(line, line.length());
    }

    public static String[] getSuggestions(String line, int index)
    {
        if (line == null)
        {
            throw new NullPointerException("Line is null");
        }

        if (index < 0 || index > line.length())
        {
            throw new IllegalArgumentException("Index is out of range: " + index);
        }

        if (index != line.length())
        {
            return EMPTY_SUGGESTIONS; // TODO: add middle line suggestions
        }

        List<String> tokens = getTokens(line);
        if (tokens.size() == 0)
        {
            return getDefaultSuggestions();
        }

        if (tokens.size() == 1)
        {
            return getSuggestions(line, tokens.get(0));
        }

        return getSuggestions(line, tokens);
    }

    private static String[] getSuggestions(String line, String token)
    {
        List<CCommand> commands = CRegistery.ListCommands(token);

        if (commands.isEmpty()) // no commands found
        {
            return EMPTY_SUGGESTIONS;
        }

        if (commands.size() == 1) // single command
        {
            CCommand cmd = commands.get(0);

            // check if line already contains suggested value (e.g. "cmdlist ")
            if (line.length() == cmd.Name.length() + 1 && line.endsWith(" ") && line.startsWith(cmd.Name))
            {
                throw new NotImplementedException(); // FIXME
            }

            return singleSuggestion(toDisplayName(cmd));
        }

        return getSuggestions(commands); // multiple commands
    }

    private static String[] getSuggestions(String commandLine, List<String> tokens)
    {
        ArrayListIterator<String> iter = new ArrayListIterator<>((ArrayList<String>) tokens); // TODO: fix the cast

        CCommand cmd = CRegistery.FindCommand(iter.next());
        if (cmd == null)
        {
            return EMPTY_SUGGESTIONS; // no command found
        }

        while (iter.hasNext())
        {
            String token = iter.next();
            int iterPos = iter.getPosition(); // store position to revert for the case if option skip fails

            // first try to parse options
            if (token.startsWith("--")) // long option
            {
                String optionName = token.substring(2);
                if (skipOption(iter, cmd, optionName)) continue;

                iter.setPosition(iterPos);
                return getSuggestedOptions(iter, cmd, optionName, "--");
            }
            else if (token.startsWith("-") && !StringUtils.IsNumeric(token)) // short option
            {
                String optionName = token.substring(1);
                if (skipOption(iter, cmd, optionName)) continue;

                iter.setPosition(iterPos);
                return getSuggestedOptions(iter, cmd, optionName, "-");
            }

            return getSuggestedArgs(commandLine, cmd, token);
        }

        return getSuggestedArgs(commandLine, cmd, "");
    }

    private static String[] getDefaultSuggestions()
    {
        return getSuggestions(CRegistery.ListCommands());
    }

    private static String[] getSuggestions(List<CCommand> commands)
    {
        String[] names = new String[commands.size()];
        int index = 0;

        for (CCommand cmd : commands)
        {
            names[index++] = toDisplayName(cmd);
        }

        return names;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Options

    private static boolean skipOption(ArrayListIterator<String> iter, CCommand cmd, String name)
    {
        Option opt = cmd.FindOption(name);
        return opt != null && skipOption(iter, opt) && iter.hasNext();
    }

    private static boolean skipOption(ArrayListIterator<String> iter, Option opt)
    {
        Class<?> type = opt.getType();

        if (type.isArray())
        {
            Object arr = opt.getValue();
            if (arr != null)
            {
                int index;
                int length = Array.getLength(arr);
                for (index = 0; index < length && iter.hasNext(); ++index)
                {
                    String value = iter.next();
                    if (!isValidOptionString(value))
                    {
                        return false;
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
                return isValidOptionString(value);
            }

            return false;
        }

        if (boolean.class.equals(type))
        {
            return true;
        }

        return false;
    }

    private static boolean isValidOptionString(String value)
    {
        return Option.IsValidValue(String.class, value); // don't actually check types: just format
    }

    private static String[] getSuggestedOptions(ArrayListIterator<String> iter, CCommand cmd, String optNameToken, String prefix)
    {
        List<Option> optionsList = new ArrayList<>(); // TODO: reuse list

        // list options
        boolean useShort = prefix.equals("-");
        if (useShort)
        {
            cmd.ListShortOptions(optionsList, optNameToken);
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
            cmd.ListOptions(optionsList, optNameToken);
            Collections.sort(optionsList, new Comparator<Option>()
            {
                @Override
                public int compare(Option lhs, Option rhs)
                {
                    return lhs.Name.compareTo(rhs.Name);
                }
            });
        }

        if (optionsList.size() > 1) // multiple options available
        {
            return getSuggestedOptions(optionsList, useShort);
        }

        if (optionsList.size() == 1) // single option available
        {
            Option opt = optionsList.get(0);

            if (isOptionNameMatch(opt, optNameToken, useShort)) // option name already matched - try values
            {
                if (opt.HasValues()) // option has predefined values?
                {
                    if (iter.hasNext()) // has value token?
                    {
                        return opt.ListValues(iter.next());
                    }

                    return opt.Values;
                }

                if (iter.hasNext())
                {
                    return EMPTY_SUGGESTIONS; // don't suggest option value
                }
            }

            return singleSuggestion(getSuggestedOption(opt, useShort)); // suggest option`s name
        }

        return EMPTY_SUGGESTIONS;
    }

    private static String[] getSuggestedOptions(List<Option> options, boolean useShort)
    {
        if (options.size() > 0)
        {
            String[] names = new String[options.size()];
            for (int i = 0; i < options.size(); ++i)
            {
                names[i] = getSuggestedOption(options.get(i), useShort);
            }

            return names;
        }

        return EMPTY_SUGGESTIONS;
    }

    private static String getSuggestedOption(Option opt, boolean useShort)
    {
        if (useShort)
        {
            return StringUtils.C("-" + opt.ShortName, ColorCode.TableVar);
        }

        return StringUtils.C("--" + opt.Name, ColorCode.TableVar);
    }

    private static boolean isOptionNameMatch(Option opt, String token, boolean useShort)
    {
        if (useShort)
        {
            return opt.ShortName != null && opt.ShortName.equalsIgnoreCase(token);
        }

        return opt.Name.equalsIgnoreCase(token);
    }


    //////////////////////////////////////////////////////////////////////////////
    // Arguments

    private static String[] getSuggestedArgs(String commandLine, CCommand cmd, String token)
    {
        String[] values = cmd.Values();
        if (values != null && values.length > 0)
        {
            return getSuggestedArgs(values, token);
        }

        String[] customValues = cmd.AutoCompleteArgs(commandLine, token);
        if (customValues != null && customValues.length > 0)
        {
            return getSuggestedArgs(customValues, token);
        }

        return EMPTY_SUGGESTIONS;
    }

    private static String[] getSuggestedArgs(String[] values, String token)
    {
        // we need to keep suggested values in a sorted order
        List<String> sortedValues = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; ++i)
        {
            if (token.length() == 0 || StringUtils.StartsWithIgnoreCase(StringUtils.RemoveRichTextTags(values[i]), token))
            {
                sortedValues.add(StringUtils.RemoveRichTextTags(values[i]));
            }
        }

        if (sortedValues.isEmpty())
        {
            return EMPTY_SUGGESTIONS;
        }

        if (sortedValues.size() > 1)
        {
            Collections.sort(sortedValues);
            return ArrayUtils.toArray(sortedValues, String.class);
        }

        return singleSuggestion(sortedValues.get(0));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    private static String[] singleSuggestion(String suggestion)
    {
        if (suggestion == null)
        {
            throw new NullPointerException("Suggestion is null");
        }

        SINGLE_SUGGESTION[0] = suggestion;
        return SINGLE_SUGGESTION;
    }

    private static String toDisplayName(CCommand cmd)
    {
        ColorCode color = cmd instanceof CVarCommand ? ColorCode.TableVar : cmd.ColorCode();
        return StringUtils.C(cmd.Name, color);
    }

    private static List<String> getTokens(String line)
    {
        List<String> tokens = CommandTokenizer.Tokenize(line, CommandTokenizer.OPTION_IGNORE_MISSING_QUOTES);
        if (tokens.size() > 0 && line.endsWith(" "))
        {
            tokens.add(""); // treat last space as "empty" token
        }
        return tokens;
    }
}
