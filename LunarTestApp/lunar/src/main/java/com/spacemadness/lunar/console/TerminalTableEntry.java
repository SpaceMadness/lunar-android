package com.spacemadness.lunar.console;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import spacemadness.com.lunar.R;

/**
 * Created by alementuev on 6/22/15.
 */
public class TerminalTableEntry extends TerminalEntry
{
    static
    {
        register(TerminalTableEntry.class, new ViewHolderBuilder<TerminalTableEntry>()
        {
            @Override
            public TerminalAdapter.ViewHolder<TerminalTableEntry> createViewHolder(ViewGroup parent)
            {
                View itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.view_terminal_entry, parent, false);

                return new ViewHolder(itemView);
            }
        });
    }

    private final String[] table;

    public TerminalTableEntry(String[] table)
    {
        if (table == null)
        {
            throw new NullPointerException("Table is null");
        }

        this.table = table;
    }

    public String[] getTable()
    {
        return table;
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    private static class ViewHolder extends TerminalAdapter.ViewHolder<TerminalTableEntry>
    {
        private final TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            textView = (TextView) itemView;
        }

        @Override
        public void onBindViewHolder(TerminalTableEntry entry)
        {
            StringBuilder buffer = new StringBuilder();

            int index = 0;
            String[] table = entry.getTable();
            for (String line : table)
            {
                buffer.append("  ");
                buffer.append(line);
                if (++index < table.length)
                {
                    buffer.append('\n');
                }
            }

            textView.setText(buffer.toString());
        }
    }
}
