package com.spacemadness.lunar.console;

import android.util.SparseArray;

import com.spacemadness.lunar.ui.ViewHolderBuilder;

public abstract class TerminalEntry
{
    private static SparseArray<ViewHolderBuilder> lookup = new SparseArray<>();

    private final int type;

    protected TerminalEntry()
    {
        type = getType(getClass());
    }

    //////////////////////////////////////////////////////////////////////////////
    // Registry

    protected static <T extends TerminalEntry> void register(Class<T> cls, ViewHolderBuilder<T> builder)
    {
        if (cls == null)
        {
            throw new NullPointerException("Class is null");
        }

        if (builder == null)
        {
            throw new NullPointerException("Builder is null");
        }

        int type = getType(cls);

        ViewHolderBuilder existingBuilder = findBuilder(type);
        if (existingBuilder != null)
        {
            throw new IllegalArgumentException("Builder with type " + type +
                    " already registered: " + existingBuilder.getClass().getName());
        }

        lookup.put(type, builder);
    }

    static ViewHolderBuilder findBuilder(int type)
    {
        return lookup.get(type);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    private static int getType(Class<? extends TerminalEntry> cls)
    {
        return cls.hashCode(); // TODO: find a better approach
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    int getType()
    {
        return type;
    }
}
