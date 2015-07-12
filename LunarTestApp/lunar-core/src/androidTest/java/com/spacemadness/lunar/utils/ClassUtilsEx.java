package com.spacemadness.lunar.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClassUtilsEx
{
    public static void setField(Class<?> cls, Object object, String name, boolean value) throws NoSuchFieldException, IllegalAccessException
    {
        Field field = resolveField(cls, name);
        field.setBoolean(object, value);
    }

    public static void setField(Class<?> cls, Object object, String name, int value) throws NoSuchFieldException, IllegalAccessException
    {
        Field field = resolveField(cls, name);
        field.setInt(object, value);
    }

    public static void setField(Class<?> cls, Object object, String name, float value) throws NoSuchFieldException, IllegalAccessException
    {
        Field field = resolveField(cls, name);
        field.setFloat(object, value);
    }

    public static void setField(Class<?> cls, Object object, String name, Object value) throws NoSuchFieldException, IllegalAccessException
    {
        Field field = resolveField(cls, name);
        field.set(object, value);
    }

    private static Field resolveField(Class<?> cls, String name) throws NoSuchFieldException, IllegalAccessException
    {
        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        if (name == null)
        {
            throw new NullPointerException("Name is null");
        }

        Field field = cls.getDeclaredField(name);
        setWritable(field);
        return field;
    }

    // FIXME: add support to Dalvik runtime
    private static void setWritable(Field field) throws NoSuchFieldException, IllegalAccessException
    {
        int modifiers = field.getModifiers();

        if (Modifier.isFinal(modifiers))
        {
            Field artFieldField = Field.class.getDeclaredField("artField");
            artFieldField.setAccessible(true);
            Object artField = artFieldField.get(field);

            Class<?> artFieldClass = artField.getClass();
            Field accessFlagsField = artFieldClass.getDeclaredField("accessFlags");
            accessFlagsField.setAccessible(true);
            int accessFlags = accessFlagsField.getInt(artField) & 0xffff;
            accessFlags &= ~Modifier.FINAL;
            accessFlagsField.setInt(artField, accessFlags);
        }

        if (!Modifier.isPublic(modifiers))
        {
            field.setAccessible(true);
        }
    }
}
