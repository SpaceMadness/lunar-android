package com.spacemadness.lunar.ui;

import android.view.ViewGroup;

import com.spacemadness.lunar.console.TerminalAdapter;
import com.spacemadness.lunar.console.TerminalEntry;

/**
 * Created by alementuev on 6/22/15.
 */
public abstract class ViewHolderBuilder<T extends TerminalEntry>
{
    public abstract TerminalAdapter.ViewHolder<T> createViewHolder(ViewGroup parent);
}
