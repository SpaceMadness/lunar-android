package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.StringUtils;

/**
 * Created by alementuev on 5/29/15.
 */
class CommandProcessor
{
    private ICCommandDelegate m_delegate;

    public CommandProcessor()
    {
        CommandDelegate(null);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Command buffer

    public boolean TryExecute(String commandLine, boolean manualMode = false)
    {
        try
        {
            IList<String> commandList = CommandSplitter.Split(commandLine);
            for (int commandIndex = 0; commandIndex < commandList.Count; ++commandIndex)
            {
                if (!TryExecuteSingleCommand(commandList[commandIndex], manualMode))
                {
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            m_delegate.LogTerminal(StringUtils.C(e.Message, ColorCode.ErrorUnknownCommand));
            m_delegate.LogTerminal(StringUtils.C(e.StackTrace, ColorCode.ErrorUnknownCommand));
            return false;
        }

        return true;
    }

    private boolean TryExecuteSingleCommand(String commandLine, boolean manualMode = false)
    {
        IList<String> tokensList = CommandTokenizer.Tokenize(commandLine);
        if (tokensList.Count > 0)
        {
            String commandName = tokensList[0];

            CCommand command = CRegistery.FindCommand(commandName);
            if (command != null)
            {
                command.Delegate = m_delegate;
                command.IsManualMode = manualMode;
                command.CommandString = commandLine;
                boolean succeed = command.ExecuteTokens(tokensList, commandLine);
                command.Clear();

                return succeed;
            }

            if (manualMode)
            {
                m_delegate.LogTerminal(StringUtils.C(commandName + ": command not found", ColorCode.ErrorUnknownCommand));
            }
        }

        return false;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Cvar lookup

    public CVarCommand FindCvarCommand(String name)
    {
        return CRegistery.FindCvarCommand(name);
    }

    public CVar FindCvar(String name)
    {
        return CRegistery.FindCvar(name);
    }

    public CCommand FindCommand(String name)
    {
        return CRegistery.FindCommand(name);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Delegate notification

    private void NotifyCommandExecuted(CCommand cmd)
    {
        if (Delegate != null)
        {
            Delegate.OnCommandExecuted(this, cmd);
        }
    }

    private void NotifyCommandUnknown(String cmdName)
    {
        if (Delegate != null)
        {
            Delegate.OnCommandUnknown(this, cmdName);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    public ICommandProcessorDelegate Delegate; //FIXME: public ICommandProcessorDelegate Delegate { get; set; }

    public ICCommandDelegate CommandDelegate() // FIXME: rename
    {
        return m_delegate;
    }

    public void CommandDelegate(ICCommandDelegate value) // FIXME: rename
    {
        m_delegate = value != null ? value : NullCommandDelegate.Instance;
    }
}