package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.ObjectUtils;

/**
 * Created by weee on 5/28/15.
 */
class CValue implements Cloneable
{
    public String stringValue;
    public int intValue;
    public float floatValue;

    public CValue copyFrom(CValue other)
    {
        if (other == null)
        {
            throw new NullPointerException("Other value is null");
        }

        stringValue = other.stringValue;
        intValue = other.intValue;
        floatValue = other.floatValue;

        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof CValue))
        {
            return false;
        }

        CValue other = (CValue) o;
        return intValue == other.intValue &&
                floatValue == other.floatValue &&
                ObjectUtils.areEqual(stringValue, other.stringValue);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        CValue temp = new CValue();
        temp.stringValue = stringValue;
        temp.intValue = intValue;
        temp.floatValue = floatValue;
        return temp;
    }
}