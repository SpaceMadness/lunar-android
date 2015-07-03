package com.spacemadness.lunar.console;

import android.view.ViewGroup;

/**
 * Created by alementuev on 6/22/15.
 */
public abstract class ViewHolderBuilder<T extends TerminalEntry>
{
    public abstract TerminalAdapter.ViewHolder<T> createViewHolder(ViewGroup parent);
}
