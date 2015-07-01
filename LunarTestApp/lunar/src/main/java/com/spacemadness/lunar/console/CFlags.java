package com.spacemadness.lunar.console;

/**
 * Created by weee on 5/28/15.
 */
public class CFlags
{
    /// <summary>
    /// No flags (default value)
    /// </summary>
    public static final int None      = CCommandFlags.None;

    /// <summary>
    /// Only accessible in debug mode
    /// </summary>
    public static final int Debug     = CCommandFlags.Debug;

    /// <summary>
    /// Won't be listed in cvarlist
    /// </summary>
    public static final int Hidden    = CCommandFlags.Hidden;

    /// <summary>
    /// System variable (hidden in cvarlist unless "--all (-a)" option is used)
    /// </summary>
    public static final int System    = CCommandFlags.System;

    /// <summary>
    /// The value cannot be modified from the command line
    /// </summary>
    public static final int Readonly  = 1 << 255;

    /// <summary>
    /// Don't save into config file
    /// </summary>
    public static final int NoArchive = 1 << 256;
}