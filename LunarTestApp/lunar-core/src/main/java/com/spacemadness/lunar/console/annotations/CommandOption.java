package com.spacemadness.lunar.console.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandOption
{
    String Name() default "";
    String ShortName() default "";
    String Description() default "";
    String Values() default "";

    boolean Required() default false;
}
