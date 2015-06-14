package com.spacemadness.lunar.console;

import com.spacemadness.lunar.console.commands.Cmd_reset;

/**
 * Created by weee on 6/10/15.
 */
public class CVarCommandTest extends CCommandTestCase
{
	public void testCvarBool()
    {
        CVar cvar = new CVar("var", false);

        assertTrue(cvar.IsBool());
        assertTrue(cvar.IsInt());
        assertTrue(cvar.IsDefault());
        assertEquals("0", cvar.DefaultValue());

        assertEquals(false, cvar.BoolValue());
        assertEquals(0, cvar.IntValue());
        assertEquals(0.0f, cvar.FloatValue());
        assertEquals("0", cvar.Value());

        execute("var 1");
        assertFalse(cvar.IsDefault());
        assertEquals(true, cvar.BoolValue());
        assertEquals(1, cvar.IntValue());
        assertEquals(1.0f, cvar.FloatValue());
        assertEquals("1", cvar.Value());

        execute("var 0");
        assertTrue(cvar.IsDefault());
        assertEquals(false, cvar.BoolValue());
        assertEquals(0, cvar.IntValue());
        assertEquals(0.0f, cvar.FloatValue());
        assertEquals("0", cvar.Value());

        execute("reset var");
        assertTrue(cvar.IsDefault());
        assertEquals(false, cvar.BoolValue());
        assertEquals(0, cvar.IntValue());
        assertEquals(0.0f, cvar.FloatValue());
        assertEquals("0", cvar.Value());
    }

    public void testCvarBool2()
    {
        CVar cvar = new CVar("var", true);

        assertTrue(cvar.IsBool());
        assertTrue(cvar.IsInt());
        assertTrue(cvar.IsDefault());
        assertEquals("1", cvar.DefaultValue());

        assertEquals(true, cvar.BoolValue());
        assertEquals(1, cvar.IntValue());
        assertEquals(1.0f, cvar.FloatValue());
        assertEquals("1", cvar.Value());

        execute("var 0");
        assertFalse(cvar.IsDefault());
        assertEquals(false, cvar.BoolValue());
        assertEquals(0, cvar.IntValue());
        assertEquals(0.0f, cvar.FloatValue());
        assertEquals("0", cvar.Value());

        execute("var 1");
        assertTrue(cvar.IsDefault());
        assertEquals(true, cvar.BoolValue());
        assertEquals(1, cvar.IntValue());
        assertEquals(1.0f, cvar.FloatValue());
        assertEquals("1", cvar.Value());

        execute("reset var");
        assertTrue(cvar.IsDefault());
        assertEquals(true, cvar.BoolValue());
        assertEquals(1, cvar.IntValue());
        assertEquals(1.0f, cvar.FloatValue());
        assertEquals("1", cvar.Value());
    }

    public void testCvarInt()
    {
        CVar cvar = new CVar("var", 128);
        assertTrue(cvar.IsInt());
        assertTrue(cvar.IsDefault());
        assertEquals("128", cvar.DefaultValue());

        assertEquals(128, cvar.IntValue());
        assertEquals(128.0f, cvar.FloatValue());
        assertEquals("128", cvar.Value());

        execute("var -128");
        assertFalse(cvar.IsDefault());
        assertEquals(-128, cvar.IntValue());
        assertEquals(-128.0f, cvar.FloatValue());
        assertEquals("-128", cvar.Value());

        execute("var 128");
        assertTrue(cvar.IsDefault());
        assertEquals(128, cvar.IntValue());
        assertEquals(128.0f, cvar.FloatValue());
        assertEquals("128", cvar.Value());

        execute("var 0");
        assertFalse(cvar.IsDefault());
        assertEquals(0, cvar.IntValue());
        assertEquals(0.0f, cvar.FloatValue());
        assertEquals("0", cvar.Value());

        execute("reset var");
        assertTrue(cvar.IsDefault());
        assertEquals(128, cvar.IntValue());
        assertEquals(128.0f, cvar.FloatValue());
        assertEquals("128", cvar.Value());
    }

    public void testCvarFloat()
    {
        CVar cvar = new CVar("var", 3.14f);
        assertTrue(cvar.IsFloat());
        assertTrue(cvar.IsDefault());
        assertEquals("3.14", cvar.DefaultValue());

        assertEquals(3, cvar.IntValue());
        assertEquals(3.14f, cvar.FloatValue());
        assertEquals("3.14", cvar.Value());

        execute("var -3.14");
        assertFalse(cvar.IsDefault());
        assertEquals(-3, cvar.IntValue());
        assertEquals(-3.14f, cvar.FloatValue());
        assertEquals("-3.14", cvar.Value());

        execute("var 3.14");
        assertTrue(cvar.IsDefault());
        assertEquals(3, cvar.IntValue());
        assertEquals(3.14f, cvar.FloatValue());
        assertEquals("3.14", cvar.Value());

        execute("var 0");
        assertFalse(cvar.IsDefault());
        assertEquals(0, cvar.IntValue());
        assertEquals(0.0f, cvar.FloatValue());
        assertEquals("0.0", cvar.Value());

        execute("reset var");
        assertTrue(cvar.IsDefault());
        assertEquals(3, cvar.IntValue());
        assertEquals(3.14f, cvar.FloatValue());
        assertEquals("3.14", cvar.Value());
    }

    public void testCvarString()
    {
        CVar cvar = new CVar("var", "Default string");
        assertTrue(cvar.IsString());
        assertTrue(cvar.IsDefault());
        assertEquals("Default string", cvar.DefaultValue());
        assertEquals("Default string", cvar.Value());

        execute("var 'Some string'");
        assertFalse(cvar.IsDefault());
        assertEquals("Some string", cvar.Value());

        execute("var 'Default string'");
        assertTrue(cvar.IsDefault());
        assertEquals("Default string", cvar.Value());

        execute("var 'Some other string'");
        assertFalse(cvar.IsDefault());
        assertEquals("Some other string", cvar.Value());

        execute("reset var");
        assertTrue(cvar.IsDefault());
        assertEquals("Default string", cvar.Value());
    }

    public void testCvarString2()
    {
        CVar cvar = new CVar("var", null);
        assertTrue(cvar.IsString());
        assertTrue(cvar.IsDefault());
        assertNull(cvar.DefaultValue());
        assertNull(cvar.Value());

        execute("var 'Some string'");
        assertFalse(cvar.IsDefault());
        assertEquals("Some string", cvar.Value());

        execute("reset var");
        assertTrue(cvar.IsDefault());
        assertNull(cvar.Value());
    }

    @Override
    protected void runSetup()
    {
        super.runSetup();

        RegisterCommands(Cmd_reset.class);
    }
}