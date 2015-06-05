package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.ReusableLists;

import java.util.List;

/**
 * Created by alementuev on 5/29/15.
 */
class CommandSplitter
{
    private static final char Space        = ' ';
    private static final char DoubleQuote  = '"';
    private static final char SingleQuote  = '\'';
    private static final char EscapeSymbol = '\\';

    public static List<String> Split(String str)
    {
        List<String> list = ReusableLists.NextAutoRecycleList(String.class);
        Split(str, list);
        return list;
    }

    public static void Split(String str, List<String> list)
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
                    AddCommand(commandBuffer, list);
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

        if (insideDoubleQuotes)
        {
            throw new TokenizeException("Missing closing double quote");
        }

        if (insideSingleQuotes)
        {
            throw new TokenizeException("Missing closing single quote");
        }

        if (commandBuffer.length() > 0)
        {
            AddCommand(commandBuffer, list);
        }
    }

    private static void AddCommand(StringBuilder buffer, List<String> list)
    {
        String command = buffer.toString().trim();
        if (command.length() == 0)
        {
            throw new TokenizeException("Can't add empty command");
        }

        list.add(command);
        buffer.setLength(0);
    }
}
