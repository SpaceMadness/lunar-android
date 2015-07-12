package com.spacemadness.lunar.console.entries;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.TerminalAdapter;
import com.spacemadness.lunar.console.TerminalEntry;
import com.spacemadness.lunar.console.ViewHolderBuilder;
import com.spacemadness.lunar.utils.StringUtils;

import spacemadness.com.lunar.R;

public class TerminalCVarEntry extends TerminalEntry
{
    static
    {
        register(TerminalCVarEntry.class, new ViewHolderBuilder<TerminalCVarEntry>()
        {
            @Override
            public TerminalAdapter.ViewHolder<TerminalCVarEntry> createViewHolder(ViewGroup parent)
            {
                View itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.terminal_entry_cvar_view, parent, false);

                return new ViewHolder(itemView);
            }
        });
    }

    private final CVar cvar;

    public TerminalCVarEntry(CVar cvar)
    {
        if (cvar == null)
        {
            throw new NullPointerException("CVar is null");
        }
        this.cvar = cvar;
    }

    @Override
    protected void onEntryDestroy()
    {

    }

    public CVar getCvar()
    {
        return cvar;
    }

    //////////////////////////////////////////////////////////////////////////////
    // View holder

    private static class ViewHolder extends TerminalAdapter.ViewHolder<TerminalCVarEntry>
    {
        private final ImageButton resetButton;
        private final TextView nameTextView;
        private final EditText valueEditText;

        public ViewHolder(View itemView)
        {
            super(itemView);

            resetButton = (ImageButton) itemView.findViewById(R.id.terminal_cvar_view_button_reset);
            nameTextView = (TextView) itemView.findViewById(R.id.terminal_cvar_view_text_view_name);
            valueEditText = (EditText) itemView.findViewById(R.id.terminal_cvar_view_edit_text_value);
        }

        @Override
        public void onBindViewHolder(TerminalCVarEntry entry)
        {
            final CVar cvar = entry.getCvar();
            final boolean modified = !cvar.IsDefault();

            // reset-to-default button
            if (modified)
            {
                resetButton.setClickable(true);
                resetButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        cvar.IsDefault(true);
                    }
                });
            }

            // name
            nameTextView.setText(StringUtils.indent(cvar.Name()));

            // value
            valueEditText.setText(cvar.Value());

            // set bold if modified
            if (modified)
            {
                valueEditText.setTypeface(valueEditText.getTypeface(), Typeface.BOLD);
                nameTextView.setTypeface(nameTextView.getTypeface(), Typeface.BOLD);
            }
        }
    }
}
