package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.StringUtils;

/**
 * Created by alementuev on 5/28/15.
 */
class CCommandException extends RuntimeException
{
    public CCommandException(String message)
    {
        super(message);
    }

    public CCommandException(String format, Object... args)
    {
        this(StringUtils.TryFormat(format, args));
    }
}