package com.spacemadness.lunar.console;

import java.util.ArrayList;
import java.util.List;

class CommandSplitter
{
    public static final int OPTION_IGNORE_MISSING_QUOTES = 1;

    private static final char Space        = ' ';
    private static final char DoubleQuote  = '"';
    private static final char SingleQuote  = '\'';
    private static final char EscapeSymbol = '\\';

    public static List<String> Split(String str)
    {
        return Split(str, 0);
    }

    public static List<String> Split(String str, int options)
    {
        List<String> list = new ArrayList<>();
        Split(str, list, options);
        return list;
    }

    public static void Split(String str, List<String> list)
    {
        Split(str, list, 0);
    }

    public static void Split(String str, List<String> list, int options)
    {
        StringBuilder commandBuffer = new StringBuilder();

        boolean insideDoubleQuotes = false;
        boolean insideSingleQuotes = false;

        char ch = (char) 0;
        char prevCh;
        for (int i = 0; i < str.length();)
        {
            prevCh = ch;
            ch = str.charAt(i++);

            if (ch == '&' && i < str.length() && str.charAt(i) == '&') // found command separator
            {
                if (!insideDoubleQuotes && !insideSingleQuotes)
                {
                    addCommand(commandBuffer, list);
                    ++i; // skip second separator's char
                }
                else
                {
                    commandBuffer.append(ch);
                }
            }
            else if (ch == DoubleQuote)
            {
                commandBuffer.append(ch);

                if (insideDoubleQuotes)
                {
                    insideDoubleQuotes = prevCh == EscapeSymbol;
                }
                else
                {
                    insideDoubleQuotes = prevCh != EscapeSymbol;
                }
            }
            else if (ch == SingleQuote)
            {
                commandBuffer.append(ch);
                if (insideSingleQuotes)
                {
                    insideSingleQuotes = prevCh == EscapeSymbol;
                }
                else
                {
                    insideSingleQuotes = prevCh != EscapeSymbol;
                }
            }
            else
            {
                commandBuffer.append(ch);
            }
        }

        if (insideDoubleQuotes && !hasOption(options, OPTION_IGNORE_MISSING_QUOTES))
        {
            throw new TokenizeException("Missing closing double quote");
        }

        if (insideSingleQuotes && !hasOption(options, OPTION_IGNORE_MISSING_QUOTES))
        {
            throw new TokenizeException("Missing closing single quote");
        }

        if (commandBuffer.length() > 0)
        {
            addCommand(commandBuffer, list);
        }
    }

    private static void addCommand(StringBuilder buffer, List<String> list)
    {
        String command = buffer.toString().trim();
        if (command.length() == 0)
        {
            throw new TokenizeException("Can't add empty command");
        }

        list.add(command);
        buffer.setLength(0);
    }

    private static boolean hasOption(int options, int option)
    {
        return (options & option) != 0;
    }
}
