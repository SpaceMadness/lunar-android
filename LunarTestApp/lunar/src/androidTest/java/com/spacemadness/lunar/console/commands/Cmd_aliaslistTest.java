package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommandTest;

public class Cmd_aliaslistTest extends CCommandTest
{
    public void testListAliases()
    {
        execute("aliaslist -s");
        assertResult(
                "cmd_alias1," +
                "cmd_alias12," +
                "cmd_alias2"
        );
    }

    public void testListAliasesPrefix()
    {
        execute("aliaslist -s cmd_alias1");
        assertResult(
                "cmd_alias1," +
                "cmd_alias12"
        );
    }

    public void testListAliasesPrefix2()
    {
        execute("aliaslist -s cmd_aliases");
        assertResult();
    }

    public void testListAliasesPrefixIgnoreCase()
    {
        execute("aliaslist -s CMD_ALIAS1");
        assertResult(
                "cmd_alias1," +
                "cmd_alias12"
        );
    }

    @Override
    protected void runSetup()
    {
        super.runSetup();

        OverrideDebugMode(true);

        this.IsTrackTerminalLog = true;

        RegisterCommands(
                new cmdlist(),
                new cvarlist(),
                new alias(),
                new aliaslist(),
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

        RegisterCommands("cmd_1", "cmd_12", "cmd_2");

        Execute("alias cmd_alias1 cmdlist");
        Execute("alias cmd_alias12 cmdlist");
        Execute("alias cmd_alias2 cmdlist");

    }
}