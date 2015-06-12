package com.spacemadness.lunar.console;

import com.spacemadness.lunar.TestCaseEx;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

/**
 * Created by weee on 6/10/15.
 */
public class CCommandTest extends TestCaseEx implements ICCommandDelegate
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
    public void LogTerminal(Exception e, String message)
    {
        throw new NotImplementedException();
    }

    @Override
    public void ClearTerminal()
    {
    }

    @Override
    public boolean ExecuteCommandLine(String commandLine, boolean manualMode)
    {
        return m_commandProcessor.TryExecute(commandLine, manualMode);
    }

    @Override
    public void PostNotification(CCommand cmd, String name, Object... data)
    {
    }

    @Override
    public boolean IsPromptEnabled()
    {
        return false;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

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

    }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    protected boolean IsTrackConsoleLog;
    protected boolean IsTrackTerminalLog;
}