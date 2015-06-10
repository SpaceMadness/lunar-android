package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.ObjectUtils;

/**
 * Created by weee on 5/28/15.
 */
class CValue
{
    public String stringValue;
    public int intValue;
    public float floatValue;

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
}