package com.spacemadness.lunar.utils;

import java.lang.reflect.Array;

public class CycleArray<E>
{
    private final Class<? extends E> componentType;
    private E[] internalArray;
    private int headIndex;
    private int length;

    public CycleArray(Class<? extends E> componentType, int capacity)
    {
        if (componentType == null)
        {
            throw new NullPointerException("Component type is null");
        }
        this.componentType = componentType;
        this.internalArray = (E[]) Array.newInstance(componentType, capacity);
    }

    public void Add(E e)
    {
        int arrayIndex = ToArrayIndex(length);
        internalArray[arrayIndex] = e;
        ++length;

        if (length - headIndex > internalArray.length)
        {
            ++headIndex;
        }
    }

    public void Clear()
    {
        for (int i = 0; i < internalArray.length; ++i)
        {
            internalArray[i] = null;
        }
        length = 0;
        headIndex = 0;
    }

    public void TrimLength(int trimSize)
    {
        TrimToLength(length - trimSize);
    }

    public void TrimToLength(int trimmedLength)
    {
        if (trimmedLength < headIndex || trimmedLength > length)
        {
            throw new IllegalArgumentException("Trimmed length " + trimmedLength +
                    " should be between head index " + headIndex +
                    " and length " + length);
        }

        length = trimmedLength;
    }

    public void TrimHeadIndex(int trimSize)
    {
        TrimToHeadIndex(headIndex + trimSize);
    }

    public void TrimToHeadIndex(int trimmedHeadIndex)
    {
        if (trimmedHeadIndex < headIndex || trimmedHeadIndex > length)
        {
            throw new IllegalArgumentException("Trimmed head index " + trimmedHeadIndex +
                    " should be between head index " + headIndex +
                    " and length " + length);
        }

        headIndex = trimmedHeadIndex;
    }

    public E get(int index)
    {
        int arrayIndex = ToArrayIndex(index);
        return internalArray[arrayIndex];
    }

    public void set(int index, E value)
    {
        int arrayIndex = ToArrayIndex(index);
        internalArray[arrayIndex] = value;
    }

    public int ToArrayIndex(int i)
    {
        return i % internalArray.length;
    }

    public boolean IsValidIndex(int index)
    {
        return index >= headIndex && index < length;
    }

    private int ToArrayIndex(E[] array, int i)
    {
        return i % array.length;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Properties

    public int Capacity()
    {
        return internalArray.length;
    }

    public void Capacity(int value)
    {
        if (value > Capacity())
        {
            E[] data = (E[]) Array.newInstance(componentType, value);

            int totalCopyLength = RealLength();

            int fromIndex = ToArrayIndex(internalArray, headIndex);
            int toIndex = ToArrayIndex(data, headIndex);

            while (totalCopyLength > 0)
            {
                int copyLength = Math.min(totalCopyLength, Math.min(internalArray.length - fromIndex, data.length - toIndex));

                System.arraycopy(internalArray, fromIndex, data, toIndex, copyLength);
                totalCopyLength -= copyLength;
                fromIndex = ToArrayIndex(internalArray, fromIndex + copyLength);
                toIndex = ToArrayIndex(data, toIndex + copyLength);
            }

            internalArray = data;
        }
        else if (value < Capacity())
        {
            throw new NotImplementedException();
        }
    }

    public boolean contains(Object element)
    {
        for (int i = headIndex; i < length; ++i)
        {
            int arrayIndex = ToArrayIndex(i);
            if (ObjectUtils.areEqual(internalArray[arrayIndex], element))
            {
                return true;
            }
        }

        return false;
    }

    public int HeadIndex()
    {
        return headIndex;
    }

    public int Length()
    {
        return length;
    }

    public int RealLength()
    {
        return length - headIndex;
    }

    public E[] InternalArray()
    {
        return internalArray;
    }
}
