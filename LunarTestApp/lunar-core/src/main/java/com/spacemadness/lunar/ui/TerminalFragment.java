package com.spacemadness.lunar.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.spacemadness.lunar.console.Terminal;
import com.spacemadness.lunar.console.TerminalAdapter;
import com.spacemadness.lunar.console.TerminalEntry;
import com.spacemadness.lunar.utils.RuntimeUtils;

import java.util.List;

import spacemadness.com.lunar.R;

public class TerminalFragment extends Fragment implements CommandEditText.OnCommandRunListener, Terminal.EntriesListener
{
    private final Terminal terminal;
    private RecyclerView recycleView;
    private TerminalAdapter recycleViewAdapter;

    public TerminalFragment()
    {
        terminal = new Terminal(1024);
        terminal.setListener(this);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);

        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view_terminal);
        recycleView.setLayoutManager(layoutManager);
        recycleViewAdapter = new TerminalAdapter(terminal);

        recycleView.setAdapter(recycleViewAdapter);

        final CommandEditText commandEditText = (CommandEditText) view.findViewById(R.id.edit_text_command_line);
        commandEditText.setCommandListener(this);

        setClickListener(view, R.id.button_history, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commandEditText.prevHistory();
            }
        });

        if (RuntimeUtils.isAndroidVersionAvailable(Build.VERSION_CODES.HONEYCOMB))
        {
            setLongClickListener(view, R.id.button_history, new View.OnLongClickListener()
            {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onLongClick(View v)
                {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            commandEditText.setCommandLine(item.getTitle().toString());
                            return true;
                        }
                    });

                    Menu m = popupMenu.getMenu();

                    List<String> history = commandEditText.listHistory();
                    for (String cmd : history)
                    {
                        m.add(cmd);
                    }

                    popupMenu.show();

                    return true;
                }
            });
        }

        setClickListener(view, R.id.button_clear, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commandEditText.clear();
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////
    // CommandEditText.OnCommandRunListener

    @Override
    public void onCommandRun(CommandEditText v, String commandLine)
    {
        terminal.ExecuteCommandLine(commandLine, true);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Terminal.EntriesListener

    @Override
    public void onEntryAdded(Terminal terminal, TerminalEntry entry)
    {
        recycleViewAdapter.notifyItemInserted(terminal.getEntryCount() - 1);
        recycleView.smoothScrollBy(0, Integer.MAX_VALUE);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    private void setClickListener(View view, int id, View.OnClickListener listener)
    {
        view.findViewById(id).setOnClickListener(listener);
    }

    private void setLongClickListener(View view, int id, View.OnLongClickListener listener)
    {
        view.findViewById(id).setOnLongClickListener(listener);
    }
}
