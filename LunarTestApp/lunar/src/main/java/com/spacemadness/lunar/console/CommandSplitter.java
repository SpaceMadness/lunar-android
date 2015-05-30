package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 5/29/15.
 */
class CommandSplitter
{
    private const char Space        = ' ';
    private const char DoubleQuote  = '"';
    private const char SingleQuote  = '\'';
    private const char EscapeSymbol = '\\';

    public static IList<String> Split(String str)
    {
        IList<String> list = ReusableLists.NextAutoRecycleList<String>();
        Split(str, list);
        return list;
    }

    public static void Split(String str, IList<String> list)
    {
        StringBuilder commandBuffer = StringBuilderPool.NextAutoRecycleBuilder();

        boolean insideDoubleQuotes = false;
        boolean insideSingleQuotes = false;

        char ch = (char) 0;
        char prevCh;
        for (int i = 0; i < str.Length;)
        {
            prevCh = ch;
            ch = str[i++];

            if (ch == '&' && i < str.Length && str[i] == '&') // found command separator
            {
                if (!insideDoubleQuotes && !insideSingleQuotes)
                {
                    AddCommand(commandBuffer, list);
                    ++i; // skip second separator's char
                }
                else
                {
                    commandBuffer.Append(ch);
                }
            }
            else if (ch == DoubleQuote)
            {
                commandBuffer.Append(ch);

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
                commandBuffer.Append(ch);
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
                commandBuffer.Append(ch);
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

        if (commandBuffer.Length > 0)
        {
            AddCommand(commandBuffer, list);
        }
    }

    private static void AddCommand(StringBuilder buffer, IList<String> list)
    {
        String command = buffer.ToString().Trim();
        if (command.Length == 0)
        {
            throw new TokenizeException("Can't add empty command");
        }

        list.Add(command);
        buffer.Length = 0;
    }
}
