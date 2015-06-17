package com.spacemadness.lunar.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.spacemadness.lunar.console.CommandHistory;

/**
 * Created by alementuev on 6/16/15.
 */
public class CommandEditText extends EditText
{
    private CommandHistory history;
    private OnCommandRunListener commandListener;
    private String lastUserInput;

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
                    String commandLine = v.getText().toString().trim();
                    if (commandLine.length() > 0)
                    {
                        pushHistory(commandLine);
                        notifyListener(commandLine);
                        v.setText("");
                        resetHistory();
                    }

                    return true;
                }

                return false;
            }
        });
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

    public boolean historyPrev()
    {
        if (lastUserInput == null)
        {
            lastUserInput = getCommandLine();
        }

        String commandLine = getHistoryPrev();
        if (commandLine != null)
        {
            setText(commandLine);
            return true;
        }

        return false;
    }

    public boolean historyNext()
    {
        String commandLine = getHistoryNext();
        if (commandLine != null)
        {
            setText(commandLine);
            return true;
        }

        if (lastUserInput != null)
        {
            setCommandLine(lastUserInput);
            resetHistory();

            return true;
        }

        return false;
    }

    private void pushHistory(String commandLine)
    {
        history.Push(commandLine);
    }

    private String getHistoryPrev()
    {
        return history.Prev();
    }

    private String getHistoryNext()
    {
        return history.Next();
    }

    private void resetHistory()
    {
        history.Reset();
        lastUserInput = null;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Operations

    public void clear()
    {
        setCommandLine("");
        resetHistory();
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
        setText(commandLine);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Listeners

    public interface OnCommandRunListener
    {
        void onCommandRun(CommandEditText v, String commandLine);
    }
}
