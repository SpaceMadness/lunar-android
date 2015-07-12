package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 5/28/15.
 */
public class CCommandParseException extends RuntimeException // FIXME: change to Exception
{
    public CCommandParseException(String format, Object... args)
    {
        super(String.format(format, args));
    }
}
