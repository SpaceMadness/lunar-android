package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommandTest;
import com.spacemadness.lunar.console.CFlags;
import com.spacemadness.lunar.console.CVar;

public class Cmd_cvarlistTest extends CCommandTest
{
    public void testListVars()
    {
        execute("cvarlist -s");
        assertResult(
                "cvar_debug_1," +
                "cvar_debug_12," +
                "cvar_debug_2," +
                "cvar_normal_1," +
                "cvar_normal_12," +
                "cvar_normal_2"
        );
    }

    public void testListCvarPrefix()
    {
        execute("cvarlist -s cvar_normal");
        assertResult(
                "cvar_normal_1," +
                "cvar_normal_12," +
                "cvar_normal_2"
        );
    }

    public void testListVarsPrefix2()
    {
        execute("cvarlist -s cvar_normaly");
        assertResult();
    }

    public void testListVarsPrefixIgnoreCase()
    {
        execute("cvarlist -s CVAR_NORMAL");
        assertResult(
                "cvar_normal_1," +
                "cvar_normal_12," +
                "cvar_normal_2"
        );
    }

    public void testListVarsPrefixDebugCommands()
    {
        execute("cvarlist -s cvar_debug");
        assertResult(
                "cvar_debug_1," +
                "cvar_debug_12," +
                "cvar_debug_2"
        );
    }

    public void testListAllVars()
    {
        execute("cvarlist -s -a");
        assertResult(
                "cvar_debug_1," +
                "cvar_debug_12," +
                "cvar_debug_2," +
                "cvar_normal_1," +
                "cvar_normal_12," +
                "cvar_normal_2," +
                "cvar_system_1," +
                "cvar_system_12," +
                "cvar_system_2"
        );
    }

    public void testListVarsPrefixAllCommands()
    {
        execute("cvarlist -s -a cvar_system");
        assertResult(
                "cvar_system_1," +
                "cvar_system_12," +
                "cvar_system_2"
        );
    }

    public void testListVarsRelease()
    {
        OverrideDebugMode(false);

        execute("cvarlist -s");
        assertResult(
                "cvar_normal_1," +
                "cvar_normal_12," +
                "cvar_normal_2"
        );
    }

    public void testListAllVarsRelease()
    {
        OverrideDebugMode(false);

        execute("cvarlist -s -a");
        assertResult(
                "cvar_normal_1," +
                "cvar_normal_12," +
                "cvar_normal_2," +
                "cvar_system_1," +
                "cvar_system_12," +
                "cvar_system_2"
        );
    }

    public void testListCommandsPrefixDebugVarsRelease()
    {
        OverrideDebugMode(false);

        execute("cvarlist -s cvar_debug_");
        assertResult();
    }

    @Override
    protected void runSetup()
    {
        super.runSetup();

        OverrideDebugMode(true);

        this.IsTrackTerminalLog = true;

        RegisterCommands(
            Cmd_cvarlist.class
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
    }
}