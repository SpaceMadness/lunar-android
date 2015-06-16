package com.spacemadness.lunar.console;

import com.spacemadness.lunar.console.commands.Cmd_reset;

public class CVarChangedDelegateTest extends CCommandTestCase
{
    public void testBoolDelegate()
    {
        CVar cvarBool = new CVar("bool", false);
        cvarBool.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add(cvar.Name() + " " + cvar.BoolValue());
            }
        });

        execute("bool 0");
        assertResult();

        execute("bool 1");
        assertResult("bool true");
    }

    public void testIntDelegate()
    {
        CVar cvarInt = new CVar("int", 10);
        cvarInt.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add(cvar.Name() + " " + cvar.IntValue());
            }
        });

        execute("int 10");
        assertResult();

        execute("int 20");
        assertResult("int 20");
    }

    public void testFloatDelegate()
    {
        CVar cvarFloat = new CVar("float", 3.14f);
        cvarFloat.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add(cvar.Name() + " " + cvar.FloatValue());
            }
        });

        execute("float 3.14");
        assertResult();

        execute("float -3.14");
        assertResult("float -3.14");
    }

    public void testStringDelegate()
    {
        CVar cvarString = new CVar("string", "This is string");
        cvarString.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add(cvar.Name() + " \"" + cvar.Value() + "\"");
            }
        });

        execute("string \"This is string\"");
        assertResult();

        execute("string \"This another string\"");
        assertResult("string \"This another string\"");
    }

    public void testBoolDefaultDelegate()
    {
        CVar cvarBool = new CVar("bool", false);
        execute("bool 1");

        cvarBool.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add(cvar.Name() + " " + cvar.BoolValue());
            }
        });

        execute("reset bool");
        assertResult("bool false");
    }

    public void testIntDefaultDelegate()
    {
        CVar cvarInt = new CVar("int", 10);
        execute("int 20");

        cvarInt.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add(cvar.Name() + " " + cvar.IntValue());
            }
        });

        execute("reset int");
        assertResult("int 10");
    }

    public void testFloatDefaultDelegate()
    {
        CVar cvarFloat = new CVar("float", 3.14f);
        execute("float -3.14");

        cvarFloat.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add(cvar.Name() + " " + cvar.FloatValue());
            }
        });

        execute("reset float");
        assertResult("float 3.14");
    }

    public void testStringDefaultDelegate()
    {
        CVar cvarString = new CVar("string", "This is string");
        execute("string \"This another string\"");

        cvarString.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add(cvar.Name() + " \"" + cvar.Value() + "\"");
            }
        });

        execute("reset string");
        assertResult("string \"This is string\"");
    }

    public void testBoolDelegateMultipleDelegates()
    {
        CVar cvarBool = new CVar("bool", false);
        cvarBool.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add("delegate1 " + cvar.BoolValue());
            }
        });
        cvarBool.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add("delegate2 " + cvar.BoolValue());
            }
        });
        cvarBool.AddDelegate(new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add("delegate3 " + cvar.BoolValue());
            }
        });

        execute("bool 0");
        assertResult();

        execute("bool 1");
        assertResult("delegate1 true", "delegate2 true", "delegate3 true");
    }

    public void testBoolDelegateRemoveMultipleDelegates()
    {
        CVarChangedDelegate del1 = new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add("delegate1 " + cvar.BoolValue());
            }
        };
        CVarChangedDelegate del2 = new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add("delegate2 " + cvar.BoolValue());
            }
        };
        CVarChangedDelegate del3 = new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add("delegate3 " + cvar.BoolValue());
            }
        };

        CVar cvarBool = new CVar("bool", false);
        cvarBool.AddDelegate(del1);
        cvarBool.AddDelegate(del2);
        cvarBool.AddDelegate(del3);

        execute("bool 0");
        assertResult();
        getResultList().clear();

        execute("bool 1");
        assertResult("delegate1 true", "delegate2 true", "delegate3 true");
        getResultList().clear();

        cvarBool.RemoveDelegate(del3);
        execute("bool 0");
        assertResult("delegate1 false", "delegate2 false");
        getResultList().clear();

        cvarBool.RemoveDelegate(del2);
        execute("bool 1");
        assertResult("delegate1 true");
        getResultList().clear();

        cvarBool.RemoveDelegate(del1);
        execute("bool 0");
        assertResult();
    }

    public void testBoolDelegateRemoveMultipleDelegatesInLoop()
    {
        final CVarChangedDelegate del1 = new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add("delegate1 " + cvar.BoolValue());
            }
        };
        CVarChangedDelegate del2 = new CVarChangedDelegate()
        {
            @Override
            public void onChanged(CVar cvar)
            {
                getResultList().add("delegate2 " + cvar.BoolValue());
                cvar.RemoveDelegate(del1);
            }
        };

        CVar cvarBool = new CVar("bool", false);
        cvarBool.AddDelegate(del2);
        cvarBool.AddDelegate(del1);

        execute("bool 1");
        assertResult("delegate2 true");
        getResultList().clear();
    }

    @Override
    protected void runSetup()
    {
        super.runSetup();

        RegisterCommands(Cmd_reset.class);
    }
}