package com.spacemadness.lunar.console.entries;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacemadness.lunar.console.TerminalAdapter;
import com.spacemadness.lunar.console.TerminalEntry;
import com.spacemadness.lunar.console.ViewHolderBuilder;
import com.spacemadness.lunar.utils.StringUtils;

import spacemadness.com.lunar.R;

public class TerminalExceptionEntry extends TerminalEntry
{
    static
    {
        register(TerminalExceptionEntry.class, new ViewHolderBuilder<TerminalExceptionEntry>()
        {
            @Override
            public TerminalAdapter.ViewHolder<TerminalExceptionEntry> createViewHolder(ViewGroup parent)
            {
                View itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.view_terminal_entry, parent, false);

                return new ViewHolder(itemView);
            }
        });
    }

    private final String message;
    private final StackTraceElement[] stackTrace;

    public TerminalExceptionEntry(Throwable e, String message)
    {
        this.message = StringUtils.NonNullOrEmpty(message);
        this.stackTrace = e.getStackTrace();
    }

    public String getMessage()
    {
        return message;
    }

    public StackTraceElement[] getStackTrace()
    {
        return stackTrace;
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    private static class ViewHolder extends TerminalAdapter.ViewHolder<TerminalExceptionEntry>
    {
        private final TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            textView = (TextView) itemView;
            textView.setTextColor(getColor(R.color.terminal_entry_exception));
        }

        @Override
        public void onBindViewHolder(TerminalExceptionEntry entry)
        {
            textView.setText(entry.getMessage());
        }
    }
}
