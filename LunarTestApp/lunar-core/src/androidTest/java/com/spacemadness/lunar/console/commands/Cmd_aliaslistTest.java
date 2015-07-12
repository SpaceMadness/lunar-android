package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommandTestCase;

public class Cmd_aliaslistTest extends CCommandTestCase
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

        registerCommand(Cmd_alias.class, true);
        registerCommand(Cmd_aliaslist.class, true);

        execute("alias cmd_alias1 cmdlist");
        execute("alias cmd_alias12 cmdlist");
        execute("alias cmd_alias2 cmdlist");
    }
}