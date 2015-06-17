package com.spacemadness.lunar.console;

import com.spacemadness.lunar.Config;
import com.spacemadness.lunar.TestCaseEx;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.NotImplementedException;
import com.spacemadness.lunar.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by weee on 6/10/15.
 */
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

    protected void RegisterCommands(Class<? extends CCommand>... classes)
    {
        for (Class<? extends CCommand> cls : classes)
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
            command.IsHidden(true);
            RuntimeResolver.ResolveOptions(command);

            CRegistery.Register(command);
        }
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
            Field field = Config.class.getDeclaredField("isDebugBuild");
            int modifiers = field.getModifiers();

            if (Modifier.isFinal(modifiers))
            {
                Field artFieldField = Field.class.getDeclaredField("artField");
                artFieldField.setAccessible(true);
                Object artField = artFieldField.get(field);

                Class<?> artFieldClass = artField.getClass();
                Field accessFlagsField = artFieldClass.getDeclaredField("accessFlags");
                accessFlagsField.setAccessible(true);
                int accessFlags = accessFlagsField.getInt(artField) & 0xffff;
                accessFlags &= ~Modifier.FINAL;
                accessFlagsField.setInt(artField, accessFlags);
            }

            if (Modifier.isPrivate(modifiers))
            {
                field.setAccessible(true);
            }

            field.setBoolean(null, flag);
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