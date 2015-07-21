package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.CCommandTestCase;
import com.spacemadness.lunar.console.CRegistery;
import com.spacemadness.lunar.console.CommandAutocompletion;

import java.util.ArrayList;
import java.util.List;

public class Cmd_aliasTest extends CCommandTestCase
{
    public void testSingleAlias()
    {
        execute("alias test \"echo 'some string'\"");
        execute("test");

        assertResult("echo 'some string'");
    }

    public void testSingleAliasSingleQuote()
    {
        execute("alias test 'echo \"some string\"'");
        execute("test");

        assertResult("echo \"some string\"");
    }

    public void testOverrideAlias()
    {
        execute("alias test 'echo \"some string\"'");
        execute("alias test 'echo \"some other string\"'");
        execute("test");

        assertResult("echo \"some other string\"");
    }

    public void testMultipleCommandAlias()
    {
        execute("alias test \"echo 'some string' && echo 'some other string'\"");
        execute("test");

        assertResult(
                "echo 'some string'",
                "echo 'some other string'"
        );
    }

    public void testUnalias()
    {
        execute("alias test \"echo 'some string'\"");
        execute("test");
        execute("unalias test");
        execute("test");

        assertResult("echo 'some string'");
    }

    public void testConfigAliases()
    {
        execute("alias a1 'echo \"alias 1\"'");
        execute("alias a2 \"echo 'alias 2'\"");
        execute("alias a3 \"echo \\\"alias 3\\\"\"");

        String[] lines = Cmd_alias.ListAliasesConfig();

        runSetup(); // reset everything

        for (String cmd : lines)
        {
            execute(cmd);
        }

        execute("a1");
        execute("a2");
        execute("a3");

        assertResult(
                "echo \"alias 1\"",
                "echo 'alias 2'",
                "echo \"alias 3\""
        );
    }

    //////////////////////////////////////////////////////////////////////////////
    // Auto complete

    public void testUnaliasAutocomplete()
    {
        execute("alias alias1 'echo 1'");
        execute("alias alias12 'echo 12'");
        execute("alias alias2 'echo 2'");

        assertSuggestions("unalias ¶",
            "alias1", //
            "alias12",//
            "alias2" //
        );

        assertSuggestions("unalias alias1¶",
                "alias1", //
                "alias12" //
        );

        assertSuggestions("unalias alias12¶",
                "alias12" //
        );

        assertSuggestions("unalias alias123¶");
    }

    //////////////////////////////////////////////////////////////////////////////
    // Setup

    @Override
    protected void runSetup()
    {
        super.runSetup();

        registerCommand(Cmd_alias.class);
        registerCommand(Cmd_unalias.class);

        CRegistery.Register(new echo());
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    private class echo extends CCommand
    {
        public echo()
        {
            super("echo");
        }

        void execute(String[] args)
        {
            AddResult(CommandString);
        }
    }
}