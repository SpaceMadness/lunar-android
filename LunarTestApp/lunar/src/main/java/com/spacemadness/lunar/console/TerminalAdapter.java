package com.spacemadness.lunar.console;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.spacemadness.lunar.ui.ViewHolderBuilder;

public class TerminalAdapter extends RecyclerView.Adapter<TerminalAdapter.ViewHolder>
{
    private DataSource dataSource;

    public TerminalAdapter(DataSource dataSource)
    {
        if (dataSource == null)
        {
            throw new NullPointerException("Data source is null");
        }

        this.dataSource = dataSource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ViewHolderBuilder builder = TerminalEntry.findBuilder(viewType);
        if (builder != null)
        {
            return builder.createViewHolder(parent);
        }

        throw new IllegalArgumentException("View type is not registered: " + viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        TerminalEntry entry = dataSource.getEntry(position);
        holder.bindViewHolder(entry);
    }

    @Override
    public int getItemViewType(int position)
    {
        return dataSource.getEntry(position).getType();
    }

    @Override
    public int getItemCount()
    {
        return dataSource.getEntryCount();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Data Source

    public interface DataSource
    {
        TerminalEntry getEntry(int position);
        int getEntryCount();
    }

    //////////////////////////////////////////////////////////////////////////////
    // View Holder

    public static abstract class ViewHolder<T extends TerminalEntry> extends RecyclerView.ViewHolder
    {
        public ViewHolder(View itemView)
        {
            super(itemView);
        }

        void bindViewHolder(TerminalEntry entry)
        {
            onBindViewHolder((T) entry);
        }

        public abstract void onBindViewHolder(T entry);
    }
}
