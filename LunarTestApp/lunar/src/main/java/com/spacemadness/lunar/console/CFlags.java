package com.spacemadness.lunar.console;

/**
 * Created by weee on 5/28/15.
 */
public class CFlags
{
    /// <summary>
    /// No flags (default value)
    /// </summary>
    public static final int None      = 0;

    /// <summary>
    /// The value cannot be modified from the command line
    /// </summary>
    public static final int Readonly  = 1 << 1;

    /// <summary>
    /// Only accessible in debug mode
    /// </summary>
    public static final int Debug     = 1 << 2;

    /// <summary>
    /// Won't be listed in cvarlist
    /// </summary>
    public static final int Hidden    = 1 << 3;

    /// <summary>
    /// System variable (hidden in cvarlist unless "--all (-a)" option is used)
    /// </summary>
    public static final int System    = 1 << 4;

    /// <summary>
    /// Don't save into config file
    /// </summary>
    public static final int NoArchive = 1 << 5;
}