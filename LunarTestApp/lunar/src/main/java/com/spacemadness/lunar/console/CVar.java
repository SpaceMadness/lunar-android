package com.spacemadness.lunar.console;

public class CVar // FIXME: IEquatable<CVar>
{
    private final String m_name;
    private final CVarType m_type;

    private CValue m_value;
    private CValue m_defaultValue;

    private CFlags m_flags;

    private CVarChangedDelegateList m_delegateList;

    public CVar(String name, boolean defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, boolean defaultValue, CFlags flags)
    {
        this(name, CVarType.Boolean, flags);

        this.IntValue = defaultValue ? 1 : 0;
        m_defaultValue = m_value;
    }

    public CVar(String name, int defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, int defaultValue, CFlags flags)
    {
        this(name, CVarType.Integer, flags)

        this.IntValue = defaultValue;
        m_defaultValue = m_value;
    }

    public CVar(String name, float defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, float defaultValue, CFlags flags)
    {
        this(name, CVarType.Float, flags);

        this.FloatValue = defaultValue;
        m_defaultValue = m_value;
    }

    public CVar(String name, String defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, String defaultValue, CFlags flags)
    {
        this(name, CVarType.String, flags);

        this.Value = defaultValue;
        m_defaultValue = m_value;
    }

    public CVar(String name, Color defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, Color defaultValue, CFlags flags)
        : this(name, CVarType.Color, flags)
    {
        this.ColorValue = defaultValue;;
        m_defaultValue = m_value;
    }

    public CVar(String name, Rect defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, Rect defaultValue, CFlags flags)
    {
        this(name, CVarType.Rect, flags);

        this.RectValue = defaultValue;
        m_defaultValue = m_value;
    }

    public CVar(String name, Vector2 defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, Vector2 defaultValue, CFlags flags)
    {
        this(name, CVarType.Vector2, flags);

        this.Vector2Value = defaultValue;
        m_defaultValue = m_value;
    }

    public CVar(String name, Vector3 defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, Vector3 defaultValue, CFlags flags)
    {
        this(name, CVarType.Vector3, flags);

        this.Vector3Value = defaultValue;
        m_defaultValue = m_value;
    }

    public CVar(String name, Vector4 defaultValue)
    {
        this(name, defaultValue, CFlags.None);
    }

    public CVar(String name, Vector4 defaultValue, CFlags flags)
    {
        this(name, CVarType.Vector4, flags);

        this.Vector4Value = defaultValue;
        m_defaultValue = m_value;
    }

    private CVar(String name, CVarType type, CFlags flags)
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

            if (m_delegateList.Count == 0)
            {
                m_delegateList = null;
            }
        }
    }

    public void RemoveDelegates(Object target)
    {
        if (target != null && m_delegateList != null)
        {
            for (int i = m_delegateList.Count - 1; i >= 0; --i)
            {
                if (m_delegateList.Get(i).Target == target)
                {
                    m_delegateList.RemoveAt(i);
                }
            }

            if (m_delegateList.Count == 0)
            {
                m_delegateList = null;
            }
        }
    }

    private void NotifyValueChanged()
    {
        if (m_delegateList != null && m_delegateList.Count > 0)
        {
            m_delegateList.NotifyValueChanged(this);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // IEquatable

    public boolean Equals(CVar other)
    {
        return other != null &&
            other.m_name == m_name &&
            other.m_value.Equals(ref m_value) &&
            other.m_defaultValue.Equals(ref m_defaultValue) &&
            other.m_type == m_type &&
            other.m_flags == m_flags;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    public String Name
    {
        get { return m_name; }
    }

    public CVarType Type
    {
        get { return m_type; }
    }

    public String DefaultValue
    {
        get { return m_defaultValue.stringValue; }
    }

    public boolean IsString
    {
        get { return m_type == CVarType.String; }
    }

    public String Value
    {
        get { return m_value.stringValue; }
        set
        {
            boolean changed = m_value.stringValue != value;

            m_value.stringValue = value;
            m_value.intValue = 0;
            m_value.floatValue = 0.0f;
            m_value.vectorValue = new Vector4();

            if (changed)
            {
                NotifyValueChanged();
            }
        }
    }

    public boolean IsInt
    {
        get { return m_type == CVarType.Integer || m_type == CVarType.Boolean; }
    }

    public int IntValue
    {
        get { return m_value.intValue; }
        set
        {
            boolean changed = m_value.intValue != value;

            m_value.stringValue = StringUtils.ToString(value);
            m_value.intValue = value;
            m_value.floatValue = (float)value;
            m_value.vectorValue = new Vector4();

            if (changed)
            {
                NotifyValueChanged();
            }
        }
    }

    public boolean IsFloat
    {
        get { return m_type == CVarType.Float; }
    }

    public float FloatValue
    {
        get { return m_value.floatValue; }
        set
        {
            float oldValue = m_value.floatValue;

            m_value.stringValue = StringUtils.ToString(value);
            m_value.intValue = (int)value;
            m_value.floatValue = value;
            m_value.vectorValue = new Vector4();

            if (oldValue != value)
            {
                NotifyValueChanged();
            }
        }
    }

    public boolean IsBool
    {
        get { return m_type == CVarType.Boolean; }
    }

    public boolean BoolValue
    {
        get { return m_value.intValue != 0; }
        set { this.IntValue = value ? 1 : 0; }
    }

    public boolean IsColor
    {
        get { return m_type == CVarType.Color; }
    }

    public Color ColorValue
    {
        get { return new Color(
            m_value.vectorValue.x,
            m_value.vectorValue.y,
            m_value.vectorValue.z,
            m_value.vectorValue.w);
        }
        set {
            Vector4 vector = new Vector4(value.r, value.g, value.b, value.a);
            boolean changed = m_value.vectorValue != vector;

            m_value.stringValue = StringUtils.ToString(ref value);
            m_value.intValue = 0;
            m_value.floatValue = 0.0f;
            m_value.vectorValue = vector;
            m_value.intValue = (int)(ColorUtils.ToRGBA(ref value));

            if (changed)
            {
                NotifyValueChanged();
            }
        }
    }

    public boolean IsRect
    {
        get { return m_type == CVarType.Rect; }
    }

    public Rect RectValue
    {
        get { return new Rect(
            m_value.vectorValue.x,
            m_value.vectorValue.y,
            m_value.vectorValue.z,
            m_value.vectorValue.w);
        }

        set {
            Vector4 vector = new Vector4(value.x, value.y, value.width, value.height);
            boolean changed = m_value.vectorValue != vector;

            m_value.stringValue = StringUtils.ToString(ref value);
            m_value.intValue = 0;
            m_value.floatValue = 0.0f;
            m_value.vectorValue = vector;

            if (changed)
            {
                NotifyValueChanged();
            }
        }
    }

    public boolean IsVector2
    {
        get { return m_type == CVarType.Vector2; }
    }

    public Vector2 Vector2Value
    {
        get { return new Vector2(
            m_value.vectorValue.x,
            m_value.vectorValue.y);
        }
        set {
            Vector4 vector = new Vector4(value.x, value.y);
            boolean changed = m_value.vectorValue != vector;

            m_value.stringValue = StringUtils.ToString(ref value);
            m_value.intValue = 0;
            m_value.floatValue = 0.0f;
            m_value.vectorValue = vector;

            if (changed)
            {
                NotifyValueChanged();
            }
        }
    }

    public boolean IsVector3
    {
        get { return m_type == CVarType.Vector3; }
    }

    public Vector3 Vector3Value
    {
        get { return new Vector3(
            m_value.vectorValue.x,
            m_value.vectorValue.y,
            m_value.vectorValue.z);
        }
        set {
            Vector4 vector = new Vector4(value.x, value.y, value.z);
            boolean changed = m_value.vectorValue != vector;

            m_value.stringValue = StringUtils.ToString(ref value);
            m_value.intValue = 0;
            m_value.floatValue = 0.0f;
            m_value.vectorValue = vector;

            if (changed)
            {
                NotifyValueChanged();
            }
        }
    }

    public boolean IsVector4
    {
        get { return m_type == CVarType.Vector4; }
    }

    public Vector4 Vector4Value
    {
        get { return m_value.vectorValue; }
        set {
            boolean changed = m_value.vectorValue != value;

            m_value.stringValue = StringUtils.ToString(ref value);
            m_value.intValue = 0;
            m_value.floatValue = 0.0f;
            m_value.vectorValue = value;

            if (changed)
            {
                NotifyValueChanged();
            }
        }
    }

    public boolean IsDefault
    {
        get { return m_value.Equals(m_defaultValue); }
        set
        {
            boolean changed = this.IsDefault ^ value;
            m_value = m_defaultValue;

            if (changed)
            {
                NotifyValueChanged();
            }
        }
    }

    internal boolean IsHidden
    {
        get { return HasFlag(CFlags.Hidden); }
    }

    internal boolean IsSystem
    {
        get { return HasFlag(CFlags.System); }
    }

    internal boolean IsDebug
    {
        get { return HasFlag(CFlags.Debug); }
    }

    internal boolean HasFlag(CFlags flag)
    {
        return (m_flags & flag) != 0;
    }
}