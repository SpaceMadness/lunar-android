package com.spacemadness.lunar.console;

public class ConfigTest extends CCommandTestCase
{
    public void testWriteDefaultConfig()
    {
        new CVar("bool", true);
        new CVar("int", 10);
        new CVar("float", 3.14f);
        new CVar("string", "Some string");

        execute("bool 0");
        execute("int 30");
        execute("float 6.28");
        execute("string 'Some other string'");
    }
}
