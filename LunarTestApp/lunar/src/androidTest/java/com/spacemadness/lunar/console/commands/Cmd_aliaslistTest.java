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
            Cmd_alias.class,
            Cmd_aliaslist.class
        );

        execute("alias cmd_alias1 cmdlist");
        execute("alias cmd_alias12 cmdlist");
        execute("alias cmd_alias2 cmdlist");
    }
}