package com.spacemadness.lunar.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.spacemadness.lunar.console.CommandAutocompleteAdapter;
import com.spacemadness.lunar.console.CommandHistory;
import com.spacemadness.lunar.core.Dispatch;
import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.FileUtils;
import com.spacemadness.lunar.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandEditText extends AutoCompleteTextView
{
    private CommandHistory history;
    private OnCommandRunListener commandListener;
    private HistorySaveRunnable historySaveRunnable;

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
        loadHistory(history);

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
                        clear();
                    }

                    return true;
                }

                return false;
            }
        });

        this.setAdapter(new CommandAutocompleteAdapter(context));
        this.setThreshold(0);
    }

    @Override
    protected void replaceText(CharSequence text)
    {
        clearComposingText();

        String oldText = getText().toString();
        String suggested = text.toString();
        setText(StringUtils.replaceWithSuggested(oldText, suggested));

        // make sure we keep the caret at the end of the text view
        Editable spannable = getText();
        Selection.setSelection(spannable, spannable.length());
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
        synchronized (history)
        {
            history.Push(commandLine);
        }

        saveHistory();
    }

    private File getHistoryFile()
    {
        return getHistoryFile(getContext());
    }

    private static File getHistoryFile(Context context)
    {
        return new File(context.getFilesDir(), ".history");  // FIXME: resolve file name
    }

    public static boolean clearHistory(Context context)
    {
        return getHistoryFile(context).delete();
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
        if (commandLine == null)
        {
            throw new NullPointerException("Command line is null");
        }

        setText(commandLine);
        setSelection(commandLine.length());
    }

    //////////////////////////////////////////////////////////////////////////////
    // History

    public void prevHistory()
    {
        String prevCommand = getPrevHistory();
        if (prevCommand != null)
        {
            setCommandLine(prevCommand);
        }
    }

    public List<String> listHistory()
    {
        return listHistory(new ArrayList<String>());
    }

    public List<String> listHistory(List<String> outList)
    {
        synchronized (history)
        {
            return history.toList(outList);
        }
    }

    private void saveHistory()
    {
        if (historySaveRunnable == null)
        {
            historySaveRunnable = new HistorySaveRunnable(getHistoryFile());
        }
        Dispatch.dispatchOnce(historySaveRunnable);
    }

    private void loadHistory(final CommandHistory history)
    {
        final File historyFile = getHistoryFile();
        if (!historyFile.exists())
        {
            return;
        }

        Dispatch.dispatch(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    List<String> commands = FileUtils.Read(historyFile);
                    synchronized (history)
                    {
                        history.load(commands);
                    }
                }
                catch (IOException e)
                {
                    Log.logException(e, "Can't load command history");
                }
            }
        });
    }

    private String getPrevHistory()
    {
        synchronized (history)
        {
            return history.prev();
        }
    }

    private void resetHistory()
    {
        synchronized (history)
        {
            history.reset();
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Listeners

    public interface OnCommandRunListener
    {
        void onCommandRun(CommandEditText v, String commandLine);
    }

    //////////////////////////////////////////////////////////////////////////////
    // History saver

    private class HistorySaveRunnable implements Runnable
    {
        private final File historyFile;

        public HistorySaveRunnable(File historyFile)
        {
            if (historyFile == null)
            {
                throw new NullPointerException("History file is null");
            }
            this.historyFile = historyFile;
        }

        @Override
        public void run()
        {
            try
            {
                FileUtils.Write(historyFile, getCommands());
            }
            catch (IOException e)
            {
                Log.logException(e, "Can't save command history: " + historyFile);
            }
        }

        private List<String> getCommands()
        {
            synchronized (history)
            {
                return history.toList();
            }
        }
    }
}
