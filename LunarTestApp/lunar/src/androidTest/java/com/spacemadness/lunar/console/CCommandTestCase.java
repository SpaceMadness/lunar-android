package com.spacemadness.lunar.console;

import android.support.annotation.NonNull;

import com.spacemadness.lunar.Config;
import com.spacemadness.lunar.TestCaseEx;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.ClassUtilsEx;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

public class CCommandTestCase extends TestCaseEx implements ICCommandDelegate
{
    private CommandProcessor m_commandProcessor;

    //////////////////////////////////////////////////////////////////////////////
    // Setup

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        runSetup();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        runTearDown();
    }

    protected void runSetup()
    {
        this.IsTrackConsoleLog = false;
        this.IsTrackTerminalLog = false;

        CRegistery.Clear();

        m_commandProcessor = new CommandProcessor();
        m_commandProcessor.CommandDelegate(this);
    }

    private void runTearDown()
    {
        CRegistery.Clear();

        m_commandProcessor = null;
    }

    //////////////////////////////////////////////////////////////////////////////
    // ICCommandDelegate

    @Override
    public void LogTerminal(String message)
    {
        if (IsTrackTerminalLog)
        {
            AddResult(message);
        }
    }

    @Override
    public void LogTerminal(String[] table)
    {
        if (IsTrackTerminalLog)
        {
            AddResult(StringUtils.Join(table));
        }
    }

    @Override
    public void LogTerminal(Throwable e, String message)
    {
        throw new Error(message, e);
    }

    @Override
    public void LogTerminal(TerminalEntry entry)
    {
        throw new NotImplementedException();
    }

    @Override
    public boolean ExecuteCommandLine(String commandLine, boolean manualMode)
    {
        return m_commandProcessor.TryExecute(commandLine, manualMode);
    }

    @Override
    public boolean IsPromptEnabled()
    {
        return false;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    protected void registerCommand(Class<? extends CCommand> cls)
    {
        registerCommand(cls, true);
    }

    protected void registerCommand(Class<? extends CCommand> cls, boolean hidden)
    {
        CCommand command = ClassUtils.tryNewInstance(cls);
        if (command == null)
        {
            throw new IllegalArgumentException("Can't create class instance: " + cls.getName());
        }

        String commandName = cls.getSimpleName();
        if (commandName.startsWith("Cmd_"))
        {
            commandName = commandName.substring("Cmd_".length());
        }

        command.Name = commandName;
        command.IsHidden(hidden);
        RuntimeResolver.ResolveOptions(command);

        CRegistery.Register(command);
    }

    protected void RegisterCommands(CCommand... commands)
    {
        for (CCommand cmd : commands)
        {
            CRegistery.Register(cmd);
        }
    }

    protected boolean execute(String format, Object... args)
    {
        return execute(String.format(format, args));
    }

    protected boolean execute(String commandLine)
    {
        return m_commandProcessor.TryExecute(commandLine, true);
    }

    protected void AddResult(String format, Object... args)
    {
        getResultList().add(StringUtils.RemoveRichTextTags(String.format(format, args)));
    }

    protected void OverrideDebugMode(boolean flag)
    {
        try
        {
            ClassUtilsEx.setField(Config.class, null, "isDebugBuild", flag);
        }
        catch (Exception e)
        {
            throw new AssertionError(e);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    protected boolean IsTrackConsoleLog;
    protected boolean IsTrackTerminalLog;
}