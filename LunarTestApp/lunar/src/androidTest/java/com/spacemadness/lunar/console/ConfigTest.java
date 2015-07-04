package com.spacemadness.lunar.console;

public class ConfigTest extends CCommandTestCase
{
    public void testWriteDefaultConfig()
    {
        CVar boolVar = new CVar("bool", true);
        CVar intVar = new CVar("int", 10);
        CVar floatVar = new CVar("float", 3.14f);
        CVar stringVar = new CVar("string", "Some string");


    }
}
