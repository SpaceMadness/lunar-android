package com.spacemadness.lunar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacemadness.lunar.console.Terminal;

import spacemadness.com.lunar.R;

public class TerminalFragment extends Fragment implements CommandEditText.OnCommandRunListener
{
    private final Terminal terminal;
    private RecyclerView recycleView;

    public TerminalFragment()
    {
        terminal = new Terminal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);
        setupUI(view);
        return view;
    }

    private void setupUI(View view)
    {
        final CommandEditText commandEditText = (CommandEditText) view.findViewById(R.id.edit_text_command_line);
        commandEditText.setCommandListener(this);

        setClickListener(view, R.id.button_up, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commandEditText.historyPrev();
            }
        });
        setClickListener(view, R.id.button_down, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commandEditText.historyNext();
            }
        });
        setClickListener(view, R.id.button_left, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(view, R.id.button_right, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        setClickListener(view, R.id.button_clear, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commandEditText.clear();
            }
        });
    }

    private void setClickListener(View view, int id, View.OnClickListener listener)
    {
        view.findViewById(id).setOnClickListener(listener);
    }

    @Override
    public void onCommandRun(CommandEditText v, String commandLine)
    {
        terminal.ExecuteCommandLine(commandLine, true);
    }
}
