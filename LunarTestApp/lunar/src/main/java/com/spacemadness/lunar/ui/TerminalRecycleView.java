package com.spacemadness.lunar.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by alementuev on 6/17/15.
 */
public class TerminalRecycleView extends RecyclerView
{
    private Adapter mAdapter;
    private LayoutManager mLayoutManager;

    public TerminalRecycleView(Context context)
    {
        super(context);
        init(context);
    }

    public TerminalRecycleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public TerminalRecycleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context)
    {

    }
}
