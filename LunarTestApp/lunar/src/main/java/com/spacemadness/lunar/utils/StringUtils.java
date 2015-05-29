package com.spacemadness.lunar.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by alementuev on 5/28/15.
 */
public class StringUtils
{
    private static final char[] kSpaceSplitChars = { ' ' };

    private static final Pattern kRichTagRegex = Pattern.compile("(<color=.*?>)|(<b>)|(<i>)|(</color>)|(</b>)|(</i>)");
    private static final Pattern kColorCodeTagRegex = Pattern.compile("<color=\\$(\\d+)>");

    static String TryFormat(String format, Object... args)
    {
        if (format != null && args != null && args.length > 0)
        {
            try
            {
                return String.format(format, args);
            }
            catch (Exception e)
            {
                Debug.LogError("Error while formatting String: " + e.getMessage());
            }
        }

        return format;
    }

    //////////////////////////////////////////////////////////////////////////////

    public static boolean StartsWithIgnoreCase(String str, String prefix)
    {
        // return str != null && prefix != null && str.startsWith(prefix, StringComparison.OrdinalIgnoreCase);
        throw new NotImplementedException();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Parsing

    public static int ParseInt(String str)
    {
        return ParseInt(str, 0);
    }

    public static int ParseInt(String str, int defValue)
    {   
        if (!IsNullOrEmpty(str))
        {
            int value;
            boolean succeed = int.TryParse(str, out value);
            return succeed ? value : defValue;
        }

        return defValue;
    }

    public static int ParseInt(String str, out boolean succeed)
    {
        if (!IsNullOrEmpty(str))
        {
            int value;
            succeed = int.TryParse(str, out value);
            return succeed ? value : 0;
        }

        succeed = false;
        return 0;
    }

    public static float ParseFloat(String str)
    {
        return ParseFloat(str, 0.0f);
    }

    public static float ParseFloat(String str, float defValue)
    {
        if (!IsNullOrEmpty(str))
        {
            float value;
            boolean succeed = float.TryParse(str, out value);
            return succeed ? value : defValue;
        }

        return defValue;
    }

    public static float ParseFloat(String str, out boolean succeed)
    {
        if (!IsNullOrEmpty(str))
        {
            float value;
            succeed = float.TryParse(str, out value);
            return succeed ? value : 0.0f;
        }

        succeed = false;
        return 0.0f;
    }

    public static boolean ParseBool(String str)
    {
        return ParseBool(str, false);
    }

    public static boolean ParseBool(String str, boolean defValue)
    {
        if (!IsNullOrEmpty(str))
        {
            boolean value;
            boolean succeed = boolean.TryParse(str, out value);
            return succeed ? value : defValue;
        }

        return defValue;
    }

    public static boolean ParseBool(String str, out boolean succeed)
    {
        if (!IsNullOrEmpty(str))
        {
            boolean value;
            succeed = boolean.TryParse(str, out value);
            return succeed ? value : false;
        }

        succeed = false;
        return false;
    }

    public static float[] ParseFloats(String str)
    {
        return str != null ? ParseFloats(str.Split(kSpaceSplitChars, StringSplitOptions.RemoveEmptyEntries)) : null;
    }

    public static float[] ParseFloats(String[] args)
    {
        if (args != null)
        {
            float[] floats = new float[args.length];
            for (int i = 0; i < args.length; ++i)
            {
                if (!float.TryParse(args[i], out floats[i]))
                {
                    return null;
                }
            }

            return floats;
        }

        return null;
    }

    public static boolean IsNumeric(String str)
    {
        double value;
        return double.TryParse(str, out value);
    }

    public static boolean IsInteger(String str)
    {
        int value;
        return int.TryParse(str, out value);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Words

    static int StartOfTheWordOffset(String value, int index)
    {
        return StartOfTheWord(value, index) - index;
    }

    static int StartOfTheWord(String value, int index)
    {
        int i = index - 1;

        while (i >= 0 && IsSeparator(value.charAt(i)))
        {
            --i;
        }

        while (i >= 0 && !IsSeparator(value.charAt(i)))
        {
            --i;
        }

        return i + 1;
    }

    static int EndOfTheWordOffset(String value, int index)
    {
        return EndOfTheWord(value, index) - index;
    }

    static int EndOfTheWord(String value, int index)
    {
        int i = index;

        while (i < value.length() && IsSeparator(value.charAt(i)))
        {
            ++i;
        }

        while (i < value.length() && !IsSeparator(value.charAt(i)))
        {
            ++i;
        }

        return i;
    }

    private static boolean IsSeparator(char ch)
    {
        return !(char.IsLetter(ch) || char.IsDigit(ch));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Lines

    static int MoveLineUp(String value, int index)
    {
        if (index > 0 && index <= value.length())
        {
            int startOfPrevLineIndex = StartOfPrevLineIndex(value, index);
            if (startOfPrevLineIndex == -1) // this is the first line (or single line): can't move up
            {
                return index;
            }

            int offsetInLine = OffsetInLine(value, index);
            int endOfPrevLineIndex = EndOfPrevLineIndex(value, index);

            return Math.min(startOfPrevLineIndex + offsetInLine, endOfPrevLineIndex);
        }

        return index;
    }

    static int MoveLineDown(String value, int index)
    {
        if (index >= 0 && index < value.length())
        {
            int startOfNextLineIndex = StartOfNextLineIndex(value, index);
            if (startOfNextLineIndex == -1)
            {
                return index; // the last line (of single line): can't move down
            }

            int offsetInLine = OffsetInLine(value, index);
            int endOfNextLineIndex = EndOfNextLineIndex(value, index);

            return Math.min(startOfNextLineIndex + offsetInLine, endOfNextLineIndex);
        }

        return index;
    }

    static int StartOfLineOffset(String value, int index)
    {
        return StartOfLineIndex(value, index) - index;
    }

    static int StartOfLineIndex(String value, int index)
    {
        return index > 0 ? value.lastIndexOf('\n', index - 1) + 1 : 0;
    }

    static int EndOfLineOffset(String value, int index)
    {
        return EndOfLineIndex(value, index) - index;
    }

    static int EndOfLineIndex(String value, int index)
    {
        if (index < value.length())
        {
            int nextLineBreakIndex = value.indexOf('\n', index);
            if (nextLineBreakIndex != -1)
            {
                return nextLineBreakIndex;
            }
        }

        return value.length();
    }

    static int OffsetInLine(String value, int index)
    {
        return index - StartOfLineIndex(value, index);
    }

    static int StartOfPrevLineIndex(String value, int index)
    {
        int endOfPrevLine = EndOfPrevLineIndex(value, index);
        return endOfPrevLine != -1 ? StartOfLineIndex(value, endOfPrevLine) : -1;
    }

    static int EndOfPrevLineIndex(String value, int index)
    {
        return StartOfLineIndex(value, index) - 1;
    }

    static int StartOfNextLineIndex(String value, int index)
    {
        int endOfLineIndex = EndOfLineIndex(value, index);
        return endOfLineIndex < value.length() ? endOfLineIndex + 1 : -1;
    }

    static int EndOfNextLineIndex(String value, int index)
    {
        int startOfNextLineIndex = StartOfNextLineIndex(value, index);
        return startOfNextLineIndex != -1 ? EndOfLineIndex(value, startOfNextLineIndex) : -1;
    }

    static int LinesBreaksCount(String value)
    {
        if (value != null)
        {
            int count = 0;
            for (int i = 0; i < value.length(); ++i)
            {
                if (value.charAt(i) == '\n')
                {
                    ++count;
                }
            }

            return count;
        }

        return 0;
    }

    static int Strlen(String str)
    {
        return str != null ? str.length() : 0;
    }

    //////////////////////////////////////////////////////////////////////////////

    private static List<String> s_tempList;

    static String GetSuggestedText(String token, String[] strings)
    {
        return GetSuggestedText0(token, strings);
    }

    static String GetSuggestedText(String token, List<String> Strings)
    {
        return GetSuggestedText0(token, (List)Strings);
    }

    private static String GetSuggestedText0(String token, IList Strings)
    {
        if (token == null || token.length() == 0)
        {
            return null;
        }

        if (s_tempList == null) s_tempList = new ArrayList<String>();
        else s_tempList.clear();

        for (String str : Strings)
        {
            if (str.startsWith(token, true, null))
            {
                s_tempList.add(str);
            }
        }

        return GetSuggestedTextFiltered0(token, s_tempList);
    }

    static String GetSuggestedTextFiltered(String token, List<String> Strings)
    {
        return GetSuggestedTextFiltered0(token, (IList)Strings);
    }

    static String GetSuggestedTextFiltered(String token, String[] Strings)
    {
        return GetSuggestedTextFiltered0(token, Strings);
    }

    private static String GetSuggestedTextFiltered0(String token, IList Strings)
    {
        if (token == null) return null;
        if (Strings.Count == 0) return null;
        if (Strings.Count == 1) return (String)Strings[0];

        String firstString = (String)Strings[0];
        if (token.length() == 0)
        {
            token = firstString;
        }

        StringBuilder suggestedToken = new StringBuilder();

        for (int charIndex = 0; charIndex < firstString.length(); ++charIndex)
        {
            char chr = firstString[charIndex];
            char chrLower = char.ToLower(chr);
            
            boolean searchFinished = false;
            for (int strIndex = 1; strIndex < Strings.Count; ++strIndex)
            {
                String otherString = (String)Strings[strIndex];
                if (charIndex >= otherString.length() || char.ToLower(otherString[charIndex]) != chrLower)
                {
                    searchFinished = true;
                    break;
                }
            }
            
            if (searchFinished)
            {
                return suggestedToken.length() > 0 ? suggestedToken.toString() : null;
            }
            
            suggestedToken.append(chr);
        }
        
        return suggestedToken.length() > 0 ? suggestedToken.toString() : null;
    }

    //////////////////////////////////////////////////////////////////////////////

    private static final String Quote = "\"";
    private static final String SingleQuote = "'";

    private static final String EscapedQuote = "\\\"";
    private static final String EscapedSingleQuote = "\\'";

    static String Arg(String value)
    {
        if (value != null && value.length() > 0)
        {
            value = value.replace(Quote, EscapedQuote);
            value = value.replace(SingleQuote, EscapedSingleQuote);

            if (value.indexOf(' ') != -1)
            {
                value = StringUtils.TryFormat("\"{0}\"", value);
            }

            return value;
        }

        return "\"\"";
    }

    static String UnArg(String value)
    {
        if (value != null && value.length() > 0)
        {
            if (value.startsWith(Quote) && value.endsWith(Quote) ||
                value.startsWith(SingleQuote) && value.endsWith(SingleQuote))
            {
                value = value.SubString(1, value.length() - 2);
            }

            value = value.replace(EscapedQuote, Quote);
            value = value.replace(EscapedSingleQuote, SingleQuote);
            
            return value;
        }
        
        return "";
    }

    static String C(String str, ColorCode color)
    {
        return StringUtils.TryFormat("<color=${0}>{1}</color>", ((int)color).toString(), str);
    }

    static String C(String str, Color color, int startIndex, int endIndex)
    {
        return StringUtils.TryFormat("{0}<color=#{1}>{2}</color>{3}", str.SubString(0, startIndex), 
            ToHexValue(ref color), 
            str.SubString(startIndex, endIndex - startIndex), 
            str.SubString(endIndex)
        );
    }

    public static String C(String str, Color color)
    {
        return StringUtils.TryFormat("<color=#{0}>{1}</color>", ToHexValue(ref color), str);
    }

    private static String ToHexValue(ref Color color)
    {
        int value = (((int)(255 * color.r)) << 24) |
            (((int)(255 * color.g)) << 16) |
            (((int)(255 * color.b)) << 8) |
            (color.a > 0 ? (int)(255 * color.a) : 0xff);

        return value.ToString("x8");
    }

    public static String B(String str)
    {
        return StringUtils.TryFormat("<b>{0}</b>", str);
    }

    public static String I(String str)
    {
        return StringUtils.TryFormat("<i>{0}</i>", str);
    }

    public static String RemoveRichTextTags(String line)
    {
        return kRichTagRegex.replace(line, String.Empty);
    }

    static void RemoveRichTextTags(IList<String> lines)
    {
        for (int i = 0; i < lines.Count; ++i)
        {
            lines[i] = RemoveRichTextTags(lines[i]);
        }
    }

    static String SetColors(String line, Color[] colorLookup)
    {
        Match match = kColorCodeTagRegex.Match(line);

        if (match.Success)
        {
            StringBuilder buffer = new StringBuilder(line.length()); // FIXME: use objects pool

            int lastIndex = 0;
            do
            {
                int index = match.Index;
                int length = match.length();

                buffer.append(line, lastIndex, index - lastIndex);

                int lookupIndex = UnsafeParseInt(match.Groups[1].Value);
                if (lookupIndex < colorLookup.length())
                {
                    buffer.appendFormat("<color=#{0}>", ToHexValue(ref colorLookup[lookupIndex]));
                }
                else
                {
                    buffer.append(match.Value);
                }

                lastIndex = index + length;
            }
            while ((match = match.NextMatch()).Success);

            if (lastIndex < line.length())
            {
                buffer.append(line, lastIndex, line.length() - lastIndex);
            }

            return buffer.toString();
        }

        return line;
    }

    private static int UnsafeParseInt(String str)
    {
        int value = 0;
        for (int i = 0; i < str.length(); ++i)
        {
            value = value * 10 + (str[i] - '0');
        }

        return value;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Nullability

    static boolean IsNullOrEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    static String NonNullOrEmpty(String str)
    {
        return str != null ? str : "";
    }

    //////////////////////////////////////////////////////////////////////////////
    // String representation

    static String ToString(int value)
    {
        return Integer.toString(value);
    }

    static String ToString(float value)
    {
        return Float.toString(value);
    }

    static String ToString(boolean value)
    {
        return Boolean.toString(value);
    }

    public static <T> String Join(List<T> list)
    {
        return Join(list, ",");
    }

    public static <T> String Join(List<T> list, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); ++i)
        {
            builder.append(list.get(i));
            if (i < list.size()-1) builder.append(separator);
        }
        return builder.toString();
    }
}
