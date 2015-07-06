package com.spacemadness.lunar.console;

import android.content.Context;

import com.spacemadness.lunar.AppTerminal;
import com.spacemadness.lunar.MockNotificationCenter;
import com.spacemadness.lunar.MockRuntimePlatform;
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
    public void testWriteConfig() throws Exception
    {
        new CVar("bool", true);
        new CVar("int", 10);
        new CVar("float", 3.14f);
        new CVar("string", "Some string");

        execute("bool 0");
        execute("int 30");
        execute("float 6.28");
        execute("string 'Some other string'");

        Thread.sleep(100); // give a chance for dispatcher to kick in

        waitUntilNotificationsDispatched(); // wait until notifications are dispatched
        waitUntilTimersDispatched(); // wait until config written

        File configsDir = AppTerminal.getConfigsDir();
        File config = new File(configsDir, "default.cfg");

        assertTrue(config.exists());

        List<String> expected = ArrayUtils.toList(
            "bool 0",
            "float 6.28",
            "int 30",
            "string \"Some other string\""
        );
        List<String> actual = FileUtils.Read(config, new FileUtils.ReadFilter()
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

        assertEquals(expected, actual);
    }

    public void testWriteConfigWithDefaultValues() throws Exception
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

        Thread.sleep(100); // give a chance for dispatcher to kick in

        waitUntilNotificationsDispatched(); // wait until notifications are dispatched
        waitUntilTimersDispatched(); // wait until config written

        File configsDir = AppTerminal.getConfigsDir();
        File config = new File(configsDir, "default.cfg");

        assertTrue(config.exists());

        List<String> expected = ArrayUtils.toList(
                "bool1 0",
                "float1 6.28",
                "int1 30",
                "string1 \"Some other string\""
        );
        List<String> actual = FileUtils.Read(config, new FileUtils.ReadFilter()
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

        assertEquals(expected, actual);
    }

    @Override
    protected void runSetup()
    {
        super.runSetup();

        RegisterCommands(Cmd_writeconfig.class);
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
}
