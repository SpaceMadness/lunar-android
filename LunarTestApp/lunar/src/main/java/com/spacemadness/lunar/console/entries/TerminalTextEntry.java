package com.spacemadness.lunar.console.entries;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacemadness.lunar.console.TerminalAdapter;
import com.spacemadness.lunar.console.TerminalEntry;
import com.spacemadness.lunar.console.ViewHolderBuilder;

import spacemadness.com.lunar.R;

public class TerminalTextEntry extends TerminalEntry
{
    static
    {
        register(TerminalTextEntry.class, new ViewHolderBuilder<TerminalTextEntry>()
        {
            @Override
            public TerminalAdapter.ViewHolder<TerminalTextEntry> createViewHolder(ViewGroup parent)
            {
                View itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.view_terminal_entry, parent, false);

                return new ViewHolder(itemView);
            }
        });
    }

    private final String text;

    public TerminalTextEntry(String text)
    {
        if (text == null)
        {
            throw new NullPointerException("Text is null");
        }

        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    private static class ViewHolder extends TerminalAdapter.ViewHolder<TerminalTextEntry>
    {
        private final TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            textView = (TextView) itemView;
        }

        @Override
        public void onBindViewHolder(TerminalTextEntry entry)
        {
            textView.setText(entry.getText());
        }
    }
}
