package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 5/29/15.
 */
public class LunarRuntimeResolverException extends RuntimeException // FIXME: use Exception
{
    public LunarRuntimeResolverException(String message, Exception innerException)
    {
        super(message, innerException);
    }
}
