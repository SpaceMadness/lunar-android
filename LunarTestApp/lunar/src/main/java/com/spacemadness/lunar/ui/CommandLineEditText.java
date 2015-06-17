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

/**
 * Created by alementuev on 6/16/15.
 */
public class CommandLineEditText extends EditText
{
    private OnCommandRunListener commandListener;

    public CommandLineEditText(Context context)
    {
        super(context);
        init(context);
    }

    public CommandLineEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public CommandLineEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommandLineEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        setImeActionLabel("Run", EditorInfo.IME_ACTION_GO);
        setInputType(InputType.TYPE_CLASS_TEXT);
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

    private void pushHistory(String commandLine)
    {

    }

    private void resetHistory()
    {

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

    //////////////////////////////////////////////////////////////////////////////
    // Listeners

    public interface OnCommandRunListener
    {
        void onCommandRun(CommandLineEditText v, String commandLine);
    }
}
