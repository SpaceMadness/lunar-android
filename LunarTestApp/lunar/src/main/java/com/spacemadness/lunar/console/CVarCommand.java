package com.spacemadness.lunar.console;

import com.spacemadness.lunar.ColorCode;
import com.spacemadness.lunar.utils.StringUtils;

public class CVarCommand extends CCommand
{
    public final CVar cvar;

    public CVarCommand(CVar cvar)
    {   
        this.cvar = cvar;
        this.Name = cvar.Name();

        this.IsDebug(cvar.IsDebug());
        this.IsHidden(cvar.IsHidden());
        this.IsSystem(cvar.IsSystem());
    }

    boolean execute(String[] args)
    {
        if (args.length == 0)
        {
            PrintIndent("%s is:\"%s\" default:\"%s\"", StringUtils.C(cvar.Name(), ColorCode.TableVar), cvar.Value(), cvar.DefaultValue());
            return false;
        }

        switch (cvar.Type())
        {
            case Boolean:
            {
                if (args.length != 1)
                {
                    PrintError("Unexpected args count");
                    return false;
                }

                try
                {
                    int value = Integer.parseInt(args[0]);
                    if (value == 0 || value == 1)
                    {
                        SetValue(value != 0);
                        return true;
                    }
                }
                catch (NumberFormatException e)
                {
                }

                PrintError("Invalid value: only '0' and '1' are permitted", args[0]);
                return false;
            }

            case Integer:
            {
                if (args.length != 1)
                {
                    PrintError("Unexpected args count");
                    return false;
                }

                try
                {
                    int value = Integer.parseInt(args[0]);
                    SetValue(value);
                    return true;
                }
                catch (NumberFormatException e)
                {
                }

                PrintError("Invalid value");
                return false;
            }

            case Float:
            {
                if (args.length != 1)
                {
                    PrintError("Unexpected args count");
                    return false;
                }

                try
                {
                    float value = Float.parseFloat(args[0]);
                    SetValue(value);
                    return true;
                }
                catch (NumberFormatException e)
                {
                }

                PrintError("Invalid value");
                return false;
            }

            case String:
            {
                if (args.length != 1)
                {
                    PrintError("Unexpected args count");
                    return false;
                }

                SetValue(args[0]);
                return true;
            }
        }

        if (args.length != 1)
        {
            Print("usage ");
            return false;
        }

        if (cvar.IsFloat())
        {
            try
            {
                float value = Float.parseFloat(args[0]);
                SetValue(value);
                return true;
            }
            catch (NumberFormatException e)
            {
            }

            PrintError("Invalid float \"%s\"", args[0]);
            return false;
        }

        if (cvar.IsInt())
        {
            try
            {
                int value = Integer.parseInt(args[0]);
                if (cvar.IsBool() && value != 0 && value != 1)
                {
                    PrintError("Invalid value \"%s\" only \"0\" and \"1\" are permitted", args[0]);
                    return false;
                }

                SetValue(value);
                return true;
            }
            catch (NumberFormatException e)
            {
            }

            PrintError("Invalid int \"%s\"", args[0]);
            return false;
        }

        String str = args[0];
        SetValue(str);
        return true;
    }

    public void SetValue(float value)
    {
        if (cvar.FloatValue() != value)
        {
            cvar.FloatValue(value);
            OnValueChanged();
        }
    }

    public void SetValue(boolean value)
    {
        if (cvar.BoolValue() != value)
        {
            cvar.BoolValue(value);
            OnValueChanged();
        }
    }

    public void SetValue(int value)
    {
        if (cvar.IntValue() != value)
        {
            cvar.IntValue(value);
            OnValueChanged();
        }
    }

    public void SetValue(String value)
    {
        if (cvar.Value() != value)
        {
            cvar.Value(value);
            OnValueChanged();
        }
    }

    public void SetDefault()
    {
        if (!cvar.IsDefault())
        {
            cvar.IsDefault(true);
            OnValueChanged();
        }
    }

    private void OnValueChanged()
    {
        PostNotification(CCommandNotifications.CVarValueChanged,
            CCommandNotifications.CVarValueChangedKeyVar, cvar,
            CCommandNotifications.KeyManualMode, this.IsManualMode
        );
    }

    public String Value()
    {
        return cvar.Value();
    }

    public String DefaultValue()
    {
        return cvar.DefaultValue();
    }

    public int IntValue()
    {
        return cvar.IntValue();
    }

    public float FloatValue()
    {
        return cvar.FloatValue();
    }

    public boolean BoolValue()
    {
        return cvar.BoolValue();
    }

    public boolean IsString()
    {
        return cvar.IsString();
    }

    public boolean IsInt()
    {
        return cvar.IsInt();
    }

    public boolean IsFloat()
    {
        return cvar.IsFloat();
    }

    public boolean IsBool()
    {
        return cvar.IsBool();
    }

    public boolean IsDefault()
    {
        return cvar.IsDefault();
    }

    public boolean HasFlag(int flag)
    {
        return cvar.HasFlag(flag);
    }
}
