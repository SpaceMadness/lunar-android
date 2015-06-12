package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommandTest;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.commands.mocks.cvarlist;

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

        CRegistery.Register(new cvarlist());
    }
}