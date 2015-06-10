package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.ObjectUtils;
import com.spacemadness.lunar.utils.StringUtils;

public class CVar
{
    private final String m_name;
    private final CVarType m_type;

    private CValue m_value;
    private CValue m_defaultValue;

    private int m_flags;

    private CVarChangedDelegateList m_delegateList;

    public CVar(String name, boolean defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, boolean defaultValue, int flags)
    {
        this(name, CVarType.Boolean, flags);

        this.IntValue(defaultValue ? 1 : 0);
        m_defaultValue = m_value;
    }

    public CVar(String name, int defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, int defaultValue, int flags)
    {
        this(name, CVarType.Integer, flags);

        this.IntValue(defaultValue);
        m_defaultValue = m_value;
    }

    public CVar(String name, float defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, float defaultValue, int flags)
    {
        this(name, CVarType.Float, flags);

        this.FloatValue(defaultValue);
        m_defaultValue = m_value;
    }

    public CVar(String name, String defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, String defaultValue, int flags)
    {
        this(name, CVarType.String, flags);

        this.Value(defaultValue);
        m_defaultValue = m_value;
    }

    private CVar(String name, CVarType type, int flags)
    {
        if (name == null)
        {
            throw new NullPointerException("Name is null");
        }

        m_name = name;
        m_type = type;
        m_flags = flags;

        Register(this);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Registry

    private static void Register(CVar cvar)
    {
        CRegistery.Register(cvar);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Delegates

    public void AddDelegate(CVarChangedDelegate del)
    {
        if (del == null)
        {
            throw new NullPointerException("Delegate is null");
        }

        if (m_delegateList == null)
        {
            m_delegateList = new CVarChangedDelegateList(1);
            m_delegateList.Add(del);
        }
        else if (!m_delegateList.Contains(del))
        {
            m_delegateList.Add(del);
        }
    }

    public void RemoveDelegate(CVarChangedDelegate del)
    {
        if (del != null && m_delegateList != null)
        {
            m_delegateList.Remove(del);

            if (m_delegateList.Count() == 0)
            {
                m_delegateList = null;
            }
        }
    }

    private void NotifyValueChanged()
    {
        if (m_delegateList != null && m_delegateList.Count() > 0)
        {
            m_delegateList.NotifyValueChanged(this);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Equality

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof CVar))
        {
            return false;
        }

        CVar other = (CVar) o;
        return m_name.equals(other.m_name) &&
                m_value.equals(other.m_value) &&
                m_defaultValue.equals(other.m_defaultValue) &&
                m_type == other.m_type &&
                m_flags == other.m_flags;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    public String Name() // FIXME: rename
    {
        return m_name;
    }

    public CVarType Type() // FIXME: rename
    {
        return m_type;
    }

    public String DefaultValue()
    {
        return m_defaultValue.stringValue;
    }

    public boolean IsString()
    {
        return m_type == CVarType.String;
    }

    public String Value()
    {
        return m_value.stringValue;
    }

    public void Value(String value)
    {
        boolean changed = !ObjectUtils.areEqual(m_value.stringValue, value);

        m_value.stringValue = value;
        m_value.intValue = 0;
        m_value.floatValue = 0.0f;

        if (changed)
        {
            NotifyValueChanged();
        }
    }

    public boolean IsInt()
    {
        return m_type == CVarType.Integer || m_type == CVarType.Boolean;
    }

    public int IntValue()
    {
        return m_value.intValue;
    }

    public void IntValue(int value)
    {
        boolean changed = m_value.intValue != value;

        m_value.stringValue = StringUtils.ToString(value);
        m_value.intValue = value;
        m_value.floatValue = (float)value;

        if (changed)
        {
            NotifyValueChanged();
        }
    }

    public boolean IsFloat()
    {
        return m_type == CVarType.Float;
    }

    public float FloatValue()
    {
        return m_value.floatValue;
    }

    public void FloatValue(float value)
    {
        float oldValue = m_value.floatValue;

        m_value.stringValue = StringUtils.ToString(value);
        m_value.intValue = (int)value;
        m_value.floatValue = value;

        if (oldValue != value)
        {
            NotifyValueChanged();
        }
    }

    public boolean IsBool()
    {
        return m_type == CVarType.Boolean;
    }

    public boolean BoolValue()
    {
        return m_value.intValue != 0;
    }

    public void BoolValue(boolean value)
    {
        this.IntValue(value ? 1 : 0);
    }

    public boolean IsDefault()
    {
        return m_value.equals(m_defaultValue);
    }

    public void IsDefault(boolean value)
    {
        boolean changed = this.IsDefault() ^ value;
        m_value = m_defaultValue;

        if (changed)
        {
            NotifyValueChanged();
        }
    }

    boolean IsHidden()
    {
        return HasFlag(CFlags.Hidden);
    }

    boolean IsSystem()
    {
        return HasFlag(CFlags.System);
    }

    boolean IsDebug()
    {
        return HasFlag(CFlags.Debug);
    }

    boolean HasFlag(int flag)
    {
        return (m_flags & flag) != 0;
    }
}