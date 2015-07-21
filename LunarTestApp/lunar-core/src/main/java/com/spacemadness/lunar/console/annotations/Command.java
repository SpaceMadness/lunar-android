package com.spacemadness.lunar.console.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
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
