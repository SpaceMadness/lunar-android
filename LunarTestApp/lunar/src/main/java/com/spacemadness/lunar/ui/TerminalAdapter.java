package com.spacemadness.lunar.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacemadness.lunar.console.Terminal;

import spacemadness.com.lunar.R;

/**
 * Created by alementuev on 6/17/15.
 */
public class TerminalAdapter extends RecyclerView.Adapter<TerminalAdapter.ViewHolder>
{
    private final Terminal terminal;

    public TerminalAdapter(Terminal terminal)
    {
        if (terminal == null)
        {
            throw new NullPointerException("Terminal is null");
        }
        this.terminal = terminal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.view_terminal_entry, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        TextView textView = viewHolder.getTextView();

    }

    @Override
    public int getItemCount()
    {
        return terminal.getEntriesCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.terminal_entry_text);
        }

        public TextView getTextView()
        {
            return textView;
        }
    }
}
