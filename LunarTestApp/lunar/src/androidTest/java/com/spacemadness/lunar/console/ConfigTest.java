package com.spacemadness.lunar.console;

import android.content.Context;
import android.support.annotation.NonNull;

import com.spacemadness.lunar.AppTerminal;
import com.spacemadness.lunar.MockNotificationCenter;
import com.spacemadness.lunar.MockRuntimePlatform;
import com.spacemadness.lunar.console.commands.Cmd_alias;
import com.spacemadness.lunar.console.commands.Cmd_exec;
import com.spacemadness.lunar.console.commands.Cmd_reset;
import com.spacemadness.lunar.console.commands.Cmd_resetAll;
import com.spacemadness.lunar.console.commands.Cmd_unalias;
import com.spacemadness.lunar.console.commands.Cmd_writeconfig;
import com.spacemadness.lunar.core.MockTimerManager;
import com.spacemadness.lunar.core.NotificationCenter;
import com.spacemadness.lunar.core.TimerManager;
import com.spacemadness.lunar.utils.ArrayUtils;
import com.spacemadness.lunar.utils.FileUtils;

import java.io.File;
import java.util.List;

public class ConfigTest extends CCommandTestCase
{
    public void testWriteConfigCvars() throws Exception
    {
        new CVar("bool", true);
        new CVar("int", 10);
        new CVar("float", 3.14f);
        new CVar("string", "Some string");

        execute("bool 0");
        execute("int 30");
        execute("float 6.28");
        execute("string 'Some other string'");

        assertConfig(
                "bool 0",
                "float 6.28",
                "int 30",
                "string \"Some other string\""
        );
    }

    public void testWriteConfigCvarsWithDefaultValues() throws Exception
    {
        new CVar("bool1", true);
        new CVar("bool2", true);
        new CVar("int1", 10);
        new CVar("int2", 10);
        new CVar("float1", 3.14f);
        new CVar("float2", 3.14f);
        new CVar("string1", "Some string");
        new CVar("string2", "Some string");

        execute("bool1 0");
        execute("bool2 1");
        execute("int1 30");
        execute("int2 10");
        execute("float1 6.28");
        execute("float2 3.14");
        execute("string1 'Some other string'");
        execute("string2 'Some string'");

        assertConfig(
                "bool1 0",
                "float1 6.28",
                "int1 30",
                "string1 \"Some other string\""
        );
    }

    public void testWriteConfigCvarsReset() throws Exception
    {
        new CVar("bool", true);
        new CVar("int", 10);
        new CVar("float", 3.14f);
        new CVar("string", "Some string");

        execute("bool 0");
        execute("int 30");
        execute("float 6.28");
        execute("string 'Some other string'");

        waitUntilConfigIsSaved();

        // reset boolean
        execute("reset bool");

        assertConfig(
                "float 6.28",
                "int 30",
                "string \"Some other string\""
        );

        // reset float
        execute("reset float");

        assertConfig(
                "int 30",
                "string \"Some other string\""
        );

        // reset int
        execute("reset int");

        assertConfig(
                "string \"Some other string\""
        );

        // reset string
        execute("reset string");

        assertConfig();
    }

    public void testWriteConfigCvarsResetAll() throws Exception
    {
        new CVar("bool", true);
        new CVar("int", 10);
        new CVar("float", 3.14f);
        new CVar("string", "Some string");

        execute("bool 0");
        execute("int 30");
        execute("float 6.28");
        execute("string 'Some other string'");

        waitUntilConfigIsSaved();

        execute("resetAll"); // reset all variables

        waitUntilConfigIsSaved();

        assertConfig();
    }

    public void testReadConfigCvars() throws Exception
    {
        CVar boolVar = new CVar("bool", true);
        CVar intVar = new CVar("int", 10);
        CVar floatVar = new CVar("float", 3.14f);
        CVar stringVar = new CVar("string", "Some string");

        execConfig(
                "bool 0",
                "int 30",
                "float 6.28",
                "string 'Some other string'"
        );

        assertEquals(false, boolVar.BoolValue());
        assertEquals(30, intVar.IntValue());
        assertEquals(6.28f, floatVar.FloatValue());
        assertEquals("Some other string", stringVar.Value());
    }

    public void testWriteConfigAliases() throws Exception
    {
        execute("alias alias1 command1");
        execute("alias alias2 command2");
        execute("alias alias3 command3");

        assertConfig(
                "alias alias1 command1",
                "alias alias2 command2",
                "alias alias3 command3"
        );
    }

    public void testWriteConfigAliasesRemove() throws Exception
    {
        execute("alias alias1 command1");
        execute("alias alias2 command2");
        execute("alias alias3 command3");

        waitUntilConfigIsSaved();

        execute("unalias alias1");
        assertConfig(
                "alias alias2 command2",
                "alias alias3 command3"
        );

        execute("unalias alias2");
        assertConfig(
                "alias alias3 command3"
        );

        execute("unalias alias3");
        assertConfig();
    }

    @Override
    protected void runSetup()
    {
        super.runSetup();

        registerCommand(Cmd_alias.class);
        registerCommand(Cmd_exec.class);
        registerCommand(Cmd_reset.class);
        registerCommand(Cmd_resetAll.class);
        registerCommand(Cmd_unalias.class);
        registerCommand(Cmd_writeconfig.class);
    }

    @Override
    protected MockRuntimePlatform createRuntimePlatform(Context context)
    {
        return new MockRuntimePlatform(context)
        {
            @Override
            protected NotificationCenter createNotificationCenter()
            {
                return new MockNotificationCenter();
            }

            @Override
            protected TimerManager createTimerManager()
            {
                return new MockTimerManager();
            }
        };
    }

    private void assertConfig(String... expected) throws Exception
    {
        assertEquals(ArrayUtils.toList(expected), readConfig());
    }

    private void execConfig(String... lines) throws Exception
    {
        File configFile = getConfigFile();
        File configsDir = configFile.getParentFile();
        if (!configsDir.exists())
        {
            boolean succeed = configsDir.mkdirs();
            assertTrue(succeed);
        }

        FileUtils.Write(configFile, ArrayUtils.toList(lines));

        execute("exec default.cfg");
    }

    private List<String> readConfig() throws Exception
    {
        waitUntilConfigIsSaved();

        File configFile = getConfigFile();
        assertTrue(configFile.exists());

        return FileUtils.Read(configFile, new FileUtils.ReadFilter()
        {
            @Override
            public boolean accept(String line)
            {
                String trimmed = line.trim();
                if (trimmed.length() == 0)
                {
                    return false;
                }

                if (trimmed.startsWith("//"))
                {
                    return false;
                }

                return true;
            }
        });
    }

    private void waitUntilConfigIsSaved() throws InterruptedException
    {
        waitUntilNotificationsDispatched(); // wait until notifications are dispatched
        waitUntilTimersDispatched(); // wait until config written
    }

    @NonNull
    private File getConfigFile()
    {
        File configsDir = AppTerminal.getConfigsDir();
        return new File(configsDir, "default.cfg");
    }
}
