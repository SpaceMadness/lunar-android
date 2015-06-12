package com.spacemadness.lunar.utils;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.core.ArrayIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alementuev on 5/28/15.
 */
public class StringUtils
{
    private static final char[] kSpaceSplitChars = { ' ' }; // FIXME: rename

    private static final Pattern kRichTagRegex = Pattern.compile("(<color=.*?>)|(<b>)|(<i>)|(</color>)|(</b>)|(</i>)");  // FIXME: rename
    private static final Pattern kColorCodeTagRegex = Pattern.compile("<color=\\$(\\d+)>"); // FIXME: rename

    public static String TryFormat(String format, Object... args) // FIXME: rename
    {
        if (format != null && args != null && args.length > 0)
        {
            try
            {
                return String.format(format, args);
            }
            catch (Exception e)
            {
                android.util.Log.e("Lunar", "Error while formatting String: " + e.getMessage()); // FIXME: better system loggingb
            }
        }

        return format;
    }

    //////////////////////////////////////////////////////////////////////////////

    public static boolean StartsWithIgnoreCase(String str, String prefix) // FIXME: rename
    {
        if (str == null || prefix == null)
        {
            return (str == null && prefix == null);
        }

        if (prefix.length() > str.length())
        {
            return false;
        }

        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    //////////////////////////////////////////////////////////////////////////////
    // Parsing

    public static int ParseInt(String str) // FIXME: rename
    {
        return ParseInt(str, 0);
    }

    public static int ParseInt(String str, int defValue)
    {   
        if (!IsNullOrEmpty(str))
        {
            try
            {
                return Integer.parseInt(str);
            }
            catch (NumberFormatException e)
            {
                return defValue;
            }
        }

        return defValue;
    }

    public static float ParseFloat(String str) // FIXME: rename
    {
        return ParseFloat(str, 0.0f);
    }

    public static float ParseFloat(String str, float defValue)
    {
        if (!IsNullOrEmpty(str))
        {
            try
            {
                return Float.parseFloat(str);
            }
            catch (NumberFormatException e)
            {
                return defValue;
            }
        }

        return defValue;
    }

    public static boolean ParseBool(String str) // FIXME: rename
    {
        return ParseBool(str, false);
    }

    public static boolean ParseBool(String str, boolean defValue) // FIXME: rename
    {
        if (!IsNullOrEmpty(str))
        {
            try
            {
                return Boolean.parseBoolean(str);
            }
            catch (NumberFormatException e)
            {
                return defValue;
            }
        }

        return defValue;
    }

    public static float[] ParseFloats(String str)
    {
        return str != null ? ParseFloats(str.split("\\s+")) : null;
    }

    public static float[] ParseFloats(String[] args)
    {
        if (args != null)
        {
            try
            {
                float[] floats = new float[args.length];

                for (int i = 0; i < args.length; ++i)
                {
                    floats[i] = Float.parseFloat(args[i]);
                }

                return floats;
            }
            catch (NumberFormatException e)
            {
            }
        }

        return null;
    }

    public static boolean IsNumeric(String str)
    {
        try
        {
            Double.parseDouble(str); // TODO: a better approach
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static boolean IsInteger(String str)
    {
        try
        {
            Integer.parseInt(str); // TODO: a better approach
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
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
        return !(Character.isLetter(ch) || Character.isDigit(ch));
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

    public static String GetSuggestedText(String token, String[] strings)
    {
        return GetSuggestedText0(token, new ArrayIterator<String>(strings));
    }

    public static String GetSuggestedText(String token, List<String> strings)
    {
        return GetSuggestedText0(token, strings.iterator());
    }

    private static String GetSuggestedText0(String token, Iterator<String> iter)
    {
        if (IsNullOrEmpty(token))
        {
            return null;
        }

        if (s_tempList == null) s_tempList = new ArrayList<String>();
        else s_tempList.clear();

        while (iter.hasNext())
        {
            String str = iter.next();
            if (StringUtils.StartsWithIgnoreCase(str, token))
            {
                s_tempList.add(str);
            }
        }

        return GetSuggestedTextFiltered0(token, s_tempList);
    }

    public static String GetSuggestedTextFiltered(String token, List<String> strings)
    {
        return GetSuggestedTextFiltered0(token, strings);
    }

    public static String GetSuggestedTextFiltered(String token, String[] strings)
    {
        // return GetSuggestedTextFiltered0(token, strings);
        throw new NotImplementedException();
    }

    private static String GetSuggestedTextFiltered0(String token, List<String> list)
    {
        if (token == null) return null;
        if (list.size() == 0) return null;
        if (list.size() == 1) return list.get(0);

        String firstString = list.get(0);
        if (token.length() == 0)
        {
            token = firstString;
        }

        StringBuilder suggestedToken = new StringBuilder();

        for (int charIndex = 0; charIndex < firstString.length(); ++charIndex)
        {
            char chr = firstString.charAt(charIndex);
            char chrLower = Character.toLowerCase(chr);
            
            boolean searchFinished = false;
            for (int strIndex = 1; strIndex < list.size(); ++strIndex)
            {
                String otherString = list.get(strIndex);
                if (charIndex >= otherString.length() || Character.toLowerCase(otherString.charAt(charIndex)) != chrLower)
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

    public static String Arg(String value)
    {
        if (value != null && value.length() > 0)
        {
            value = value.replace(Quote, EscapedQuote);
            value = value.replace(SingleQuote, EscapedSingleQuote);

            if (value.indexOf(' ') != -1)
            {
                value = StringUtils.TryFormat("\"%s\"", value);
            }

            return value;
        }

        return "\"\"";
    }

    public static String UnArg(String value)
    {
        if (value != null && value.length() > 0)
        {
            if (value.startsWith(Quote) && value.endsWith(Quote) ||
                value.startsWith(SingleQuote) && value.endsWith(SingleQuote))
            {
                value = value.substring(1, value.length() - 1);
            }

            value = value.replace(EscapedQuote, Quote);
            value = value.replace(EscapedSingleQuote, SingleQuote);
            
            return value;
        }
        
        return "";
    }

    public static String C(String str, ColorCode color)
    {
        return StringUtils.TryFormat("<color=$%s>%s</color>", ToString(color.ordinal()), str);
    }

    static String C(String str, Color color, int startIndex, int endIndex)
    {
//        return StringUtils.TryFormat("%s<color=#%s>%s</color>%s", str.SubString(0, startIndex),
//            ToHexValue(ref color),
//            str.SubString(startIndex, endIndex - startIndex),
//            str.SubString(endIndex)
//        );
        throw new NotImplementedException();
    }

    public static String C(String str, Color color)
    {
        // return StringUtils.TryFormat("<color=#%s>%s</color>", ToHexValue(ref color), str);
        throw new NotImplementedException();
    }

    private static String ToHexValue(Color color)
    {
//        int value = (((int)(255 * color.r)) << 24) |
//            (((int)(255 * color.g)) << 16) |
//            (((int)(255 * color.b)) << 8) |
//            (color.a > 0 ? (int)(255 * color.a) : 0xff);
//
//        return value.ToString("x8");
        throw new NotImplementedException();
    }

    public static String B(String str)
    {
        return StringUtils.TryFormat("<b>%s</b>", str);
    }

    public static String I(String str)
    {
        return StringUtils.TryFormat("<i>%s</i>", str);
    }

    public static String RemoveRichTextTags(String line)
    {
        Matcher matcher = kRichTagRegex.matcher(line);
        return matcher.replaceAll("");
    }

    public static void RemoveRichTextTags(List<String> lines)
    {
        for (int i = 0; i < lines.size(); ++i)
        {
            lines.set(i, RemoveRichTextTags(lines.get(i)));
        }
    }

    public static void RemoveRichTextTags(String[] lines)
    {
        for (int i = 0; i < lines.length; ++i)
        {
            lines[i] = RemoveRichTextTags(lines[i]);
        }
    }

    static String SetColors(String line, Color[] colorLookup)
    {
        /*
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
                    buffer.appendFormat("<color=#%s>", ToHexValue(ref colorLookup[lookupIndex]));
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
        */

        throw new NotImplementedException(); // FIXME
    }

    private static int UnsafeParseInt(String str)
    {
        /*
        int value = 0;
        for (int i = 0; i < str.length(); ++i)
        {
            value = value * 10 + (str[i] - '0');
        }

        return value;
        */
        throw new NotImplementedException(); // FIXME: dafuq I need that??
    }

    //////////////////////////////////////////////////////////////////////////////
    // Nullability

    public static boolean IsNullOrEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    public static String nullOrNonEmpty(String str)
    {
        return IsNullOrEmpty(str) ? null : str;
    }

    public static String NonNullOrEmpty(String str)
    {
        return str != null ? str : "";
    }

    //////////////////////////////////////////////////////////////////////////////
    // String representation

    public static String ToString(boolean value)
    {
        return Boolean.toString(value);
    }

    public static String ToString(byte value)
    {
        return Byte.toString(value);
    }

    public static String ToString(char value)
    {
        return Character.toString(value);
    }

    public static String ToString(short value)
    {
        return Short.toString(value);
    }

    public static String ToString(int value)
    {
        return Integer.toString(value);
    }

    public static String ToString(long value)
    {
        return Long.toString(value);
    }

    public static String ToString(float value)
    {
        return Float.toString(value);
    }

    public static String ToString(double value)
    {
        return Double.toString(value);
    }

    public static String ToString(Object value)
    {
        return value != null ? value.toString() : "null";
    }

    public static <T> String Join(List<T> list)
    {
        return Join(list, ",");
    }

    public static <T> String Join(List<T> list, String separator)
    {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (T e : list)
        {
            builder.append(e);
            if (++i < list.size()) builder.append(separator);
        }
        return builder.toString();
    }

    public static <T> String Join(T[] array)
    {
        return Join(array, ",");
    }

    public static <T> String Join(T[] array, String separator)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i)
        {
            builder.append(array[i]);
            if (i < array.length-1) builder.append(separator);
        }
        return builder.toString();
    }
}
