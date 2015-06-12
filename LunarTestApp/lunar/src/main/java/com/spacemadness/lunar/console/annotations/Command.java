package com.spacemadness.lunar.console.annotations;

/**
 * Created by weee on 6/10/15.
 */
public @interface Command
{
    /** Command name in terminal */
    String Name();

    /** Command description for manual page */
    String Description() default "";

    /** Comma-separated values for arguments */
    String Values() default "";

    /** Command flags: Debug, Hidden, System, etc. */
    int Flags() default 0;
}
