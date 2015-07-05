package com.spacemadness.lunar.console;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.core.IDestroyable;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Terminal implements TerminalAdapter.DataSource, ICCommandDelegate, IDestroyable
{
    private final CommandProcessor commandProcessor;
    private final TerminalEntries entries;
    private EntriesListener listener;

    public Terminal(int capacity)
    {
        commandProcessor = new CommandProcessor();
        commandProcessor.CommandDelegate(this);
        entries = new TerminalEntries(capacity);
    }

    @Override
    public void Destroy()
    {
        // TODO: release resources
    }

    public void Add(String line)
    {
        TerminalEntry entry = entries.add(line);
        notifyEntryAdded(entry);
    }

    public void Add(String[] lines)
    {
        TerminalEntry entry = entries.add(lines);
        notifyEntryAdded(entry);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Auto complete

    public String DoAutoComplete(String text, boolean isDoubleTab)
    {
        // TODO: add unit tests

        List<String> tokensList = new ArrayList<String>();
        CommandTokenizer.Tokenize(text, tokensList);

        if (tokensList.size() == 1)
        {
            String newText = DoAutoComplete(text, tokensList.get(0), isDoubleTab);
            if (newText != null && newText != text)
            {
                return newText;
            }
        }

        if (tokensList.size() > 0)
        {
            return DoAutoComplete(text, tokensList, isDoubleTab);
        }

        return DoAutoCompleteNoToken(text, isDoubleTab);
    }

    private String DoAutoComplete(String text, String token, boolean isDoubleTab)
    {
        List<CCommand> suggestedCommands = CRegistery.ListCommands(token);
        if (suggestedCommands.size() == 1)
        {
            return suggestedCommands.get(0).Name + " ";
        }

        if (suggestedCommands.size() > 1)
        {
            String[] names = new String[suggestedCommands.size()];
            for (int i = 0; i < suggestedCommands.size(); ++i)
            {
                names[i] = suggestedCommands.get(i).Name;
            }

            String result = StringUtils.GetSuggestedText(token, names);

            if (isDoubleTab)
            {
                for (int i = 0; i < suggestedCommands.size(); ++i)
                {
                    CCommand cmd = suggestedCommands.get(i);
                    ColorCode color = cmd instanceof CVarCommand ? ColorCode.TableVar : cmd.ColorCode();
                    names[i] = StringUtils.C(cmd.Name, color);
                }

                Arrays.sort(names);

                Add(CCommand.Prompt(token));
                Add(names);
            }

            return result;
        }

        return text;
    }

    private String DoAutoComplete(String text, List<String> tokensList, boolean isDoubleTab)
    {
        String name = tokensList.get(0);
        CCommand command = CRegistery.FindCommand(name);
        if (command != null)
        {
            command.Delegate(this); // FIXME: split ICCommandDelegate interface
            String newText = command.AutoComplete(text, tokensList, isDoubleTab);
            command.Delegate(null);
            if (newText != null)
            {
                return newText;
            }
        }

        return text;
    }

    private String DoAutoCompleteNoToken(String text, boolean isDoubleTab)
    {
        if (isDoubleTab)
        {
            List<CCommand> commands = CRegistery.ListCommands();
            String[] names = new String[commands.size()];
            for (int i = 0; i < commands.size(); ++i)
            {
                CCommand cmd = commands.get(i);
                ColorCode color = cmd instanceof CVarCommand ? ColorCode.TableVar : cmd.ColorCode();
                names[i] = StringUtils.C(cmd.Name, color);
            }

            Add(names);
        }

        return text;
    }

    //////////////////////////////////////////////////////////////////////////////
    // TerminalAdapter.DataSource

    @Override
    public TerminalEntry getEntry(int position)
    {
        return entries.get(position);
    }

    @Override
    public int getEntryCount()
    {
        return entries.size();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Command listener

    @Override
    public void LogTerminal(String message)
    {
        Add(message);
    }

    @Override
    public void LogTerminal(String[] table)
    {
        Add(table);
    }

    @Override
    public void LogTerminal(Throwable e, String message)
    {
        throw new NotImplementedException();
    }

    @Override
    public boolean ExecuteCommandLine(String commandLine, boolean manual)
    {
        return commandProcessor.TryExecute(commandLine, manual);
    }

    @Override
    public boolean IsPromptEnabled()
    {
        return true;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public void setListener(EntriesListener listener)
    {
        this.listener = listener;
    }

    public EntriesListener getListener()
    {
        return listener;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    private void notifyEntryAdded(TerminalEntry entry)
    {
        if (listener != null)
        {
            listener.onEntryAdded(this, entry);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Entries listener

    public interface EntriesListener
    {
        void onEntryAdded(Terminal terminal, TerminalEntry entry);
    }
}
