package com.spacemadness.lunar.console;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.spacemadness.lunar.utils.NotImplementedException;

public class CommandEditText extends AutoCompleteTextView
{
    private CommandHistory history;
    private OnCommandRunListener commandListener;

    public CommandEditText(Context context)
    {
        super(context);
        init(context);
    }

    public CommandEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public CommandEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommandEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        history = new CommandHistory(128);

        setImeActionLabel("Run", EditorInfo.IME_ACTION_GO);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setLines(1);
        setSingleLine(true);

        setOnEditorActionListener(new OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_GO)
                {
                    String commandLine = getCommandLine().trim();
                    if (commandLine.length() > 0)
                    {
                        pushHistory(commandLine);
                        notifyListener(commandLine);
                        setCommandLine("");
                    }

                    return true;
                }

                return false;
            }
        });

        String[] commands = {
            "alias",
            "aliaslist",
            "cat",
            "clear",
            "cmdlist",
            "cvarlist",
            "echo",
            "exec",
            "man",
            "reset",
            "resetAll",
            "toggle",
            "unalias",
            "writeconfig"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, commands);
        this.setAdapter(adapter);
        this.setThreshold(0);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Listener

    private void notifyListener(String commandLine)
    {
        if (commandListener != null)
        {
            commandListener.onCommandRun(this, commandLine);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // History

    private void pushHistory(String commandLine)
    {
        history.Push(commandLine);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Operations

    public void autocomplete()
    {
        throw new NotImplementedException();
    }

    public void clear()
    {
        setCommandLine("");
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public void setCommandListener(OnCommandRunListener commandListener)
    {
        this.commandListener = commandListener;
    }

    public OnCommandRunListener getCommandListener()
    {
        return commandListener;
    }

    public String getCommandLine()
    {
        return getText().toString();
    }

    public void setCommandLine(String commandLine)
    {
        if (commandLine == null)
        {
            throw new NullPointerException("Command line is null");
        }

        setText(commandLine);
        setSelection(commandLine.length());
    }

    //////////////////////////////////////////////////////////////////////////////
    // Listeners

    public interface OnCommandRunListener
    {
        void onCommandRun(CommandEditText v, String commandLine);
    }
}
