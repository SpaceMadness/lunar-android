package com.spacemadness.lunar.console;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.spacemadness.lunar.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandAutocompleteAdapter extends BaseAdapter implements Filterable
{
    private final Context context;
    private final LayoutInflater inflater;

    private List<String> objects;
    private Filter filter;

    public CommandAutocompleteAdapter(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("Context is null");
        }

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objects = new ArrayList<>();
    }

    //////////////////////////////////////////////////////////////////////////////
    // BaseAdapter

    @Override
    public int getCount()
    {
        return objects.size();
    }

    @Override
    public String getItem(int position)
    {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null)
        {
            view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView text = (TextView) view;
        text.setText(getItem(position));
        return text;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Filterable

    @Override
    public Filter getFilter()
    {
        if (filter == null)
            filter = new CommandFilter();

        return filter;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Command filter

    private class CommandFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            if (constraint == null) return null;

            String[] suggestions = CommandAutocompletion.getSuggestions(constraint.toString());

            FilterResults results = new FilterResults();
            results.values = ArrayUtils.toList(suggestions);
            results.count = suggestions.length;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            if (results != null)
            {
                objects = (List<String>) results.values;
                if (results.count > 0)
                {
                    notifyDataSetChanged();
                }
                else
                {
                    notifyDataSetInvalidated();
                }
            }
        }
    }
}
