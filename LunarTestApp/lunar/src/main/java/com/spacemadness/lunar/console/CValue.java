package com.spacemadness.lunar.console;

/**
 * Created by weee on 5/28/15.
 */
struct CValue
{
    public string stringValue;
    public int intValue;
    public float floatValue;
    public Vector4 vectorValue;

    public bool Equals(ref CValue other)
    {
        return other.intValue == intValue &&
        other.floatValue == floatValue &&
        other.stringValue == stringValue &&
        other.vectorValue == vectorValue;
    }
}