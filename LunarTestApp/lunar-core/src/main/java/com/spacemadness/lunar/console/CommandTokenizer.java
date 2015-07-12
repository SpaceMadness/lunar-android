package com.spacemadness.lunar.console;

import java.util.ArrayList;
import java.util.List;

class CommandTokenizer
{
    public static final int OPTION_IGNORE_MISSING_QUOTES = 1;

    private static final char DoubleQuote  = '"';
    private static final char SingleQuote  = '\'';
    private static final char EscapeSymbol = '\\';

    public static List<String> Tokenize(String str)
    {
        return Tokenize(str, 0);
    }

    public static List<String> Tokenize(String str, int options)
    {
        List<String> list = new ArrayList<>();
        Tokenize(str, list, options);
        return list;
    }

    public static void Tokenize(String str, List<String> tokens)
    {
        Tokenize(str, tokens, 0);
    }

    public static void Tokenize(String str, List<String> tokens, int options)
    {
        StringBuilder tokenBuffer = new StringBuilder();

        boolean insideSingleQuotes = false;
        boolean insideDoubleQuotes = false;
        boolean shouldAddToken = true;

        char ch = (char) 0;
        char prevCh;

        for (int i = 0; i < str.length();)
        {
            prevCh = ch;
            ch = str.charAt(i++);

            if (Character.isWhitespace(ch))
            {
                if (!insideDoubleQuotes && !insideSingleQuotes && shouldAddToken)
                {
                    AddToken(tokenBuffer, tokens);
                }
                else
                {
                    tokenBuffer.append(ch);
                }
            }
            else if (ch == DoubleQuote)
            {
                if (insideSingleQuotes)
                {
                    tokenBuffer.append(ch);
                }
                else
                {
                    if (insideDoubleQuotes)
                    {
                        if (prevCh == EscapeSymbol)
                        {
                            tokenBuffer.append(ch);
                        }
                        else
                        {
                            if (shouldAddToken)
                            {
                                AddToken(tokenBuffer, tokens, true);
                            }
                            else
                            {
                                tokenBuffer.append(ch);
                            }

                            shouldAddToken = true;
                            insideDoubleQuotes = false;
                        }
                    }
                    else
                    {
                        shouldAddToken = Character.isWhitespace(prevCh) || prevCh == (char)0;
                        if (!shouldAddToken)
                        {
                            tokenBuffer.append(ch);
                        }
                        insideDoubleQuotes = true;
                    }
                }
            }
            else if (ch == SingleQuote)
            {
                if (insideDoubleQuotes)
                {
                    tokenBuffer.append(ch);
                }
                else 
                {
                    if (insideSingleQuotes)
                    {
                        if (prevCh == EscapeSymbol)
                        {
                            tokenBuffer.append(ch);
                        }
                        else
                        {
                            if (shouldAddToken)
                            {
                                AddToken(tokenBuffer, tokens, true);
                            }
                            else
                            {
                                tokenBuffer.append(ch);
                            }

                            shouldAddToken = true;
                            insideSingleQuotes = false;
                        }
                    }
                    else
                    {
                        shouldAddToken = Character.isWhitespace(prevCh) || prevCh == (char)0;
                        if (!shouldAddToken)
                        {
                            tokenBuffer.append(ch);
                        }
                        insideSingleQuotes = true;
                    }
                }
            }
            else
            {
                tokenBuffer.append(ch);
            }
        }

        if (insideDoubleQuotes && !hasOption(options, OPTION_IGNORE_MISSING_QUOTES))
        {
            throw new TokenizeException("Missing closing double quote: " + str);
        }

        if (insideSingleQuotes && !hasOption(options, OPTION_IGNORE_MISSING_QUOTES))
        {
            throw new TokenizeException("Missing closing single quote: " + str);
        }

        AddToken(tokenBuffer, tokens);
    }

    private static void AddToken(StringBuilder buffer, List<String> list)
    {
        AddToken(buffer, list, false);
    }

    private static void AddToken(StringBuilder buffer, List<String> list, boolean addEmpty)
    {
        if (buffer.length() > 0 || addEmpty)
        {
            list.add(buffer.toString());
            buffer.setLength(0);
        }
    }

    private static boolean hasOption(int options, int option)
    {
        return (options & option) != 0;
    }
}
