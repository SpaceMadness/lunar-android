package com.spacemadness.lunar.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.spacemadness.lunar.console.Terminal;

/**
 * Created by alementuev on 6/17/15.
 */
public class TerminalAdapter extends RecyclerView.Adapter<TerminalAdapter.ViewHolder>
{
    private final Terminal mTerminal;

    public TerminalAdapter(Terminal terminal)
    {
        if (terminal == null)
        {
            throw new NullPointerException("Terminal is null");
        }
        mTerminal = terminal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
