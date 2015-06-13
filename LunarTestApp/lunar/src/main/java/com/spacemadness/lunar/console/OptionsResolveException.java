package com.spacemadness.lunar.console;

public class OptionsResolveException extends RuntimeException
{
    public OptionsResolveException(String message)
    {
        super(message);
    }

    public OptionsResolveException(Exception cause)
    {
        super(cause);
    }
}
