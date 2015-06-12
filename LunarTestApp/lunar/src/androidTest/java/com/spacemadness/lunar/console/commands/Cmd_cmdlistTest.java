package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommandTest;
import com.spacemadness.lunar.console.commands.mocks.cmdlist;

public class Cmd_cmdlistTest extends CCommandTest
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
                "cmd_debug_2," +
                "cmd_delegate_1," +
                "cmd_delegate_12," +
                "cmd_delegate_2"
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
                "cmd_delegate_1," +
                "cmd_delegate_12," +
                "cmd_delegate_2," +
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
                "cmd_alias2," +
                "cmd_delegate_1," +
                "cmd_delegate_12," +
                "cmd_delegate_2"
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
                "cmd_delegate_1," +
                "cmd_delegate_12," +
                "cmd_delegate_2," +
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

        RegisterCommands("cmd_1", "cmd_12", "cmd_2");
    }
}