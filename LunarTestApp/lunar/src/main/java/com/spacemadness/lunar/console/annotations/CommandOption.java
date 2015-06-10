package com.spacemadness.lunar.console.annotations;

/**
 * Created by weee on 6/10/15.
 */
public @interface CommandOption
{
    String Name() default "";
    String ShortName() default "";
    String Description() default "";
}
