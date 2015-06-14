package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommandFlags;
import com.spacemadness.lunar.console.CCommandTestCase;
import com.spacemadness.lunar.console.CFlags;
import com.spacemadness.lunar.console.CVar;

public class Cmd_cmdlistTest extends CCommandTestCase
{
    public void testListCommands()
    {
        execute("cmdlist");
        assertResult(
                "cmd_1," +
                "cmd_12," +
                "cmd_2," +
                "cmd_alias1," +
                "cmd_alias12," +
                "cmd_alias2," +
                "cmd_debug_1," +
                "cmd_debug_12," +
                "cmd_debug_2"
        );
    }

    public void testListCommandsPrefix()
    {
        execute("cmdlist cmd_1");
        assertResult(
                "cmd_1," +
                "cmd_12"
        );
    }

    public void testListCommandsPrefix2()
    {
        execute("cmdlist cmd_123");
        assertResult();
    }

    public void testListCommandsPrefixIgnoreCase()
    {
        execute("cmdlist CMD_1");
        assertResult(
                "cmd_1," +
                "cmd_12"
        );
    }

    public void testListCommandsPrefixDebugCommands()
    {
        execute("cmdlist cmd_debug_1");
        assertResult(
                "cmd_debug_1," +
                "cmd_debug_12"
        );
    }

    public void testListAllCommands()
    {
        execute("cmdlist -a");
        assertResult(
                "cmd_1," +
                "cmd_12," +
                "cmd_2," +
                "cmd_alias1," +
                "cmd_alias12," +
                "cmd_alias2," +
                "cmd_debug_1," +
                "cmd_debug_12," +
                "cmd_debug_2," +
                "cmd_system_1," +
                "cmd_system_12," +
                "cmd_system_2"
        );
    }

    public void testListCommandsPrefixAllCommands()
    {
        execute("cmdlist -a cmd_system_1");
        assertResult(
                "cmd_system_1," +
                "cmd_system_12"
        );
    }

    public void testListCommandsRelease()
    {
        OverrideDebugMode(false);

        execute("cmdlist");
        assertResult(
                "cmd_1," +
                "cmd_12," +
                "cmd_2," +
                "cmd_alias1," +
                "cmd_alias12," +
                "cmd_alias2"
        );
    }

    public void testListAllCommandsRelease()
    {
        OverrideDebugMode(false);

        execute("cmdlist -a");
        assertResult(
                "cmd_1," +
                "cmd_12," +
                "cmd_2," +
                "cmd_alias1," +
                "cmd_alias12," +
                "cmd_alias2," +
                "cmd_system_1," +
                "cmd_system_12," +
                "cmd_system_2"
        );
    }

    public void testListCommandsPrefixDebugCommandsRelease()
    {
        OverrideDebugMode(false);

        execute("cmdlist cmd_debug_1");
        assertResult();
    }

    @Override
    protected void runSetup()
    {
        super.runSetup();

        OverrideDebugMode(true);

        this.IsTrackTerminalLog = true;

        RegisterCommands(
            Cmd_cmdlist.class,
            Cmd_cvarlist.class,
            Cmd_alias.class,
            Cmd_aliaslist.class
        );

        RegisterCommands(
            new cmd_hidden("hidden_cmd"),
            new cmd_system("cmd_system_1"),
            new cmd_system("cmd_system_12"),
            new cmd_system("cmd_system_2"),
            new cmd_debug("cmd_debug_1"),
            new cmd_debug("cmd_debug_12"),
            new cmd_debug("cmd_debug_2")
        );

        new CVar("cvar_normal_1",  "value_normal_1");
        new CVar("cvar_normal_12", "value_normal_12");
        new CVar("cvar_normal_2",  "value_normal_2");
        new CVar("cvar_debug_1",  "value_debug_1", CFlags.Debug);
        new CVar("cvar_debug_12", "value_debug_12", CFlags.Debug);
        new CVar("cvar_debug_2",  "value_debug_2", CFlags.Debug);
        new CVar("cvar_system_1",  "value_debug_1", CFlags.System);
        new CVar("cvar_system_12", "value_debug_12", CFlags.System);
        new CVar("cvar_system_2",  "value_debug_2", CFlags.System);
        new CVar("cvar_hidden",  "cvar_hidden", CFlags.Hidden);

        RegisterCommands(
                new CCommandMock("cmd_1"),
                new CCommandMock("cmd_12"),
                new CCommandMock("cmd_2")
        );

        execute("alias cmd_alias1 cmdlist");
        execute("alias cmd_alias12 cmdlist");
        execute("alias cmd_alias2 cmdlist");
    }

    public class cmd_hidden extends CCommandMock
    {
        public cmd_hidden(String name)
        {
            super(name);
            this.Flags = CCommandFlags.Hidden;
        }
    }

    public class cmd_system extends CCommandMock
    {
        public cmd_system(String name)
        {
            super(name);
            this.Flags = CCommandFlags.System;
        }
    }

    public class cmd_debug extends CCommandMock
    {
        public cmd_debug(String name)
        {
            super(name);
            this.Flags = CCommandFlags.Debug;
        }
    }
}