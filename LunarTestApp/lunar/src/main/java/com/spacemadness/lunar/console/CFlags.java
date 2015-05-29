package com.spacemadness.lunar.console;

/**
 * Created by weee on 5/28/15.
 */
public enum CFlags
{
    /// <summary>
    /// No flags (default value)
    /// </summary>
    None      = 0,

    /// <summary>
    /// The value cannot be modified from the command line
    /// </summary>
    Readonly  = 1 << 1,

    /// <summary>
    /// Only accessible in debug mode
    /// </summary>
    Debug     = 1 << 2,

    /// <summary>
    /// Won't be listed in cvarlist
    /// </summary>
    Hidden    = 1 << 3,

    /// <summary>
    /// System variable (hidden in cvarlist unless "--all (-a)" option is used)
    /// </summary>
    System    = 1 << 4,

    /// <summary>
    /// Don't save into config file
    /// </summary>
    NoArchive = 1 << 5,
}