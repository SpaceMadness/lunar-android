package com.spacemadness.lunar.console;

import com.spacemadness.lunar.utils.StringUtils;

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
                StringUtils.equals(stringValue, other.stringValue);
    }
}