package com.spacemadness.lunar.debug;

import com.spacemadness.lunar.utils.StringUtils;

import java.util.List;
import static com.spacemadness.lunar.utils.StringUtils.*;

public class Assert // FIXME: rename methods to assert*
{
    public static void IsTrue(boolean condition)
    {
        if (IsEnabled && !condition)
            AssertHelper("Assertion failed: 'true' expected");
    }

    public static void IsTrue(boolean condition, String message)
    {
        if (IsEnabled && !condition)
            AssertHelper(message);
    }

    public static void IsTrue(boolean condition, String format, Object... args)
    {
        if (IsEnabled && !condition)
            AssertHelper(format, args);
    }

    public static void IsTrue(boolean condition, String format, Object arg0)
    {
        if (IsEnabled && !condition)
            AssertHelper(format, arg0);
    }

    public static void IsTrue(boolean condition, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && !condition)
            AssertHelper(format, arg0, arg1);
    }

    public static void IsTrue(boolean condition, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && !condition)
            AssertHelper(format, arg0, arg1, arg2);
    }

    public static void IsFalse(boolean condition)
    {
        if (IsEnabled && condition)
            AssertHelper("Assertion failed: 'false' expected");
    }

    public static void IsFalse(boolean condition, String message)
    {
        if (IsEnabled && condition)
            AssertHelper(message);
    }

    public static void IsFalse(boolean condition, String format, Object... args)
    {
        if (IsEnabled && condition)
            AssertHelper(format, args);
    }

    public static void IsFalse(boolean condition, String format, Object arg0)
    {
        if (IsEnabled && condition)
            AssertHelper(format, arg0);
    }

    public static void IsFalse(boolean condition, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && condition)
            AssertHelper(format, arg0, arg1);
    }

    public static void IsFalse(boolean condition, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && condition)
            AssertHelper(format, arg0, arg1, arg2);
    }

    public static void IsNull(Object obj)
    {
        if (IsEnabled && obj != null)
            AssertHelper("Assertion failed: expected 'null' but was '{0}'", obj);
    }

    public static void IsNull(Object obj, String message)
    {
        if (IsEnabled && obj != null)
            AssertHelper(message);
    }

    public static void IsNull(Object obj, String format, Object... args)
    {
        if (IsEnabled && obj != null)
            AssertHelper(format, args);
    }

    public static void IsNull(Object obj, String format, Object arg0)
    {
        if (IsEnabled && obj != null)
            AssertHelper(format, arg0);
    }

    public static void IsNull(Object obj, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && obj != null)
            AssertHelper(format, arg0, arg1);
    }

    public static void IsNull(Object obj, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && obj != null)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsNotNull(Object obj)
    {
        if (IsEnabled && obj == null)
            AssertHelper("Assertion failed: Object is 'null'");
    }


    public static void IsNotNull(Object obj, String message)
    {
        if (IsEnabled && obj == null)
            AssertHelper(message);
    }


    public static void IsNotNull(Object obj, String format, Object... args)
    {
        if (IsEnabled && obj == null)
            AssertHelper(format, args);
    }

    public static void IsNotNullElement(List<?> list)
    {
        if (IsEnabled)
        {
            int index = 0;
            for (Object t : list)
            {
                Assert.IsNotNull(t, "Element at {0} is null", ToString(index));
                ++index;
            }
        }
    }


    public static void IsNotNull(Object obj, String format, Object arg0)
    {
        if (IsEnabled && obj == null)
            AssertHelper(format, arg0);
    }


    public static void IsNotNull(Object obj, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && obj == null)
            AssertHelper(format, arg0, arg1);
    }


    public static void IsNotNull(Object obj, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && obj == null)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreEqual(boolean expected, boolean actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", Boolean.toString(expected), Boolean.toString(actual));
    }


    public static void AreEqual(boolean expected, boolean actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreEqual(boolean expected, boolean actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreEqual(boolean expected, boolean actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreEqual(boolean expected, boolean actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(boolean expected, boolean actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreEqual(byte expected, byte actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", ToString(expected), ToString(actual));
    }


    public static void AreEqual(byte expected, byte actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreEqual(byte expected, byte actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreEqual(byte expected, byte actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreEqual(byte expected, byte actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(byte expected, byte actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }

    public static void AreEqual(short expected, short actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", ToString(expected), ToString(actual));
    }


    public static void AreEqual(short expected, short actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreEqual(short expected, short actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreEqual(short expected, short actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreEqual(short expected, short actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(short expected, short actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }

    public static void AreEqual(char expected, char actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", ToString(expected), ToString(actual));
    }


    public static void AreEqual(char expected, char actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreEqual(char expected, char actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreEqual(char expected, char actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreEqual(char expected, char actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(char expected, char actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreEqual(int expected, int actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", ToString(expected), ToString(actual));
    }


    public static void AreEqual(int expected, int actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreEqual(int expected, int actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreEqual(int expected, int actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreEqual(int expected, int actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(int expected, int actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreEqual(long expected, long actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", ToString(expected), ToString(actual));
    }


    public static void AreEqual(long expected, long actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreEqual(long expected, long actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreEqual(long expected, long actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreEqual(long expected, long actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(long expected, long actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreEqual(float expected, float actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", ToString(expected), ToString(actual));
    }


    public static void AreEqual(float expected, float actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreEqual(float expected, float actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreEqual(float expected, float actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreEqual(float expected, float actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(float expected, float actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreEqual(double expected, double actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", ToString(expected), ToString(actual));
    }


    public static void AreEqual(double expected, double actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreEqual(double expected, double actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreEqual(double expected, double actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreEqual(double expected, double actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(double expected, double actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreEqual(Object expected, Object actual)
    {
        if (IsEnabled && !(expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper("Assertion failed: expected '{0}' but was '{1}'", toString(expected), toString(actual));
    }


    public static void AreEqual(Object expected, Object actual, String message)
    {
        if (IsEnabled && !(expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(message);
    }


    public static void AreEqual(Object expected, Object actual, String format, Object... args)
    {
        if (IsEnabled && !(expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(format, args);
    }


    public static void AreEqual(Object expected, Object actual, String format, Object arg0)
    {
        if (IsEnabled && !(expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(format, arg0);
    }


    public static void AreEqual(Object expected, Object actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && !(expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(format, arg0, arg1);
    }


    public static void AreEqual(Object expected, Object actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && !(expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(boolean expected, boolean actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(boolean expected, boolean actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(boolean expected, boolean actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(boolean expected, boolean actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(boolean expected, boolean actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(boolean expected, boolean actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(byte expected, byte actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(byte expected, byte actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(byte expected, byte actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(byte expected, byte actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(byte expected, byte actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(byte expected, byte actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(short expected, short actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(short expected, short actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(short expected, short actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(short expected, short actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(short expected, short actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(short expected, short actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(char expected, char actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(char expected, char actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(char expected, char actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(char expected, char actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(char expected, char actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(char expected, char actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(int expected, int actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(int expected, int actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(int expected, int actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(int expected, int actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(int expected, int actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(int expected, int actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(long expected, long actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(long expected, long actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(long expected, long actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(long expected, long actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(long expected, long actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(long expected, long actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(ulong expected, ulong actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(ulong expected, ulong actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(ulong expected, ulong actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(ulong expected, ulong actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(ulong expected, ulong actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(ulong expected, ulong actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(float expected, float actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(float expected, float actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(float expected, float actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(float expected, float actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(float expected, float actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(float expected, float actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(double expected, double actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: values are equal '{0}'", ToString(expected));
    }


    public static void AreNotEqual(double expected, double actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotEqual(double expected, double actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotEqual(double expected, double actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(double expected, double actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(double expected, double actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotEqual(Object expected, Object actual)
    {
        if (IsEnabled && (expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper("Assertion failed: Objects are equal '{0}'", toString(expected));
    }


    public static void AreNotEqual(Object expected, Object actual, String message)
    {
        if (IsEnabled && (expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(message);
    }


    public static void AreNotEqual(Object expected, Object actual, String format, Object... args)
    {
        if (IsEnabled && (expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(format, args);
    }


    public static void AreNotEqual(Object expected, Object actual, String format, Object arg0)
    {
        if (IsEnabled && (expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(format, arg0);
    }


    public static void AreNotEqual(Object expected, Object actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && (expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotEqual(Object expected, Object actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && (expected != null && actual != null && expected.equals(actual) || expected == null && actual == null))
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreSame(Object expected, Object actual)
    {
        if (IsEnabled && expected != actual)
            AssertHelper("Assertion failed: Object references are not the same '{0}' but was '{1}'", toString(expected), toString(actual));
    }


    public static void AreSame(Object expected, Object actual, String message)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(message);
    }


    public static void AreSame(Object expected, Object actual, String format, Object... args)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, args);
    }


    public static void AreSame(Object expected, Object actual, String format, Object arg0)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0);
    }


    public static void AreSame(Object expected, Object actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreSame(Object expected, Object actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected != actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void AreNotSame(Object expected, Object actual)
    {
        if (IsEnabled && expected == actual)
            AssertHelper("Assertion failed: Object references are the same '{0}'", toString(expected));
    }


    public static void AreNotSame(Object expected, Object actual, String message)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(message);
    }


    public static void AreNotSame(Object expected, Object actual, String format, Object... args)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, args);
    }


    public static void AreNotSame(Object expected, Object actual, String format, Object arg0)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0);
    }


    public static void AreNotSame(Object expected, Object actual, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1);
    }


    public static void AreNotSame(Object expected, Object actual, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && expected == actual)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void Contains
    <T>(
    T expected, ICollection
    <T>collection)

    {
        if (IsEnabled && (collection == null || !collection.Contains(expected)))
        {
            if (collection == null)
                AssertHelper("Assertion failed: collection is null");
            else
                AssertHelper("Assertion failed: collection doesn't contain the item {0}", expected);
        }
    }


    public static void NotContains
    <T>(
    T expected, ICollection
    <T>collection)

    {
        if (IsEnabled && (collection != null && collection.Contains(expected)))
        {
            if (collection == null)
                AssertHelper("Assertion failed: collection is null");
            else
                AssertHelper("Assertion failed: collection contains the item {0}", expected);
        }
    }


    public static void Fail()
    {
        if (IsEnabled)
            AssertHelper("Assertion failed");
    }


    public static void Fail(String message)
    {
        if (IsEnabled)
            AssertHelper(message);
    }


    public static void Fail(String format, Object... args)
    {
        if (IsEnabled)
            AssertHelper(format, args);
    }


    public static void Fail(String format, Object arg0)
    {
        if (IsEnabled)
            AssertHelper(format, arg0);
    }


    public static void Fail(String format, Object arg0, Object arg1)
    {
        if (IsEnabled)
            AssertHelper(format, arg0, arg1);
    }


    public static void Fail(String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void Greater
    <T>(
    T a, T
    b)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) <= 0)
            AssertHelper("Assertion failed: '{0}' is not greater than '{1}'", a, b);
    }


    public static void Greater
    <T>(
    T a, T
    b,
    String message
    )
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) <= 0)
            AssertHelper(message);
    }


    public static void Greater
    <T>(
    T a, T
    b,
    String format, Object
    ...args)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) <= 0)
            AssertHelper(format, args);
    }


    public static void Greater
    <T, Object>(
    T a, T
    b,
    String format, Object
    arg0)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) <= 0)
            AssertHelper(format, arg0);
    }


    public static void Greater
    <T, Object, Object>(
    T a, T
    b,
    String format, Object
    arg0,
    Object arg1
    )
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) <= 0)
            AssertHelper(format, arg0, arg1);
    }


    public static void Greater
    <T, Object, Object, Object>(
    T a, T
    b,
    String format, Object
    arg0,
    Object arg1, Object
    arg2)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) <= 0)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void GreaterOrEqual
    <T>(
    T a, T
    b)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) < 0)
            AssertHelper("Assertion failed: '{0}' is not greater or equal to '{1}'", a, b);
    }


    public static void GreaterOrEqual
    <T>(
    T a, T
    b,
    String message
    )
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) < 0)
            AssertHelper(message);
    }


    public static void GreaterOrEqual
    <T>(
    T a, T
    b,
    String format, Object
    ...args)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) < 0)
            AssertHelper(format, args);
    }


    public static void GreaterOrEqual
    <T, Object>(
    T a, T
    b,
    String format, Object
    arg0)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) < 0)
            AssertHelper(format, arg0);
    }


    public static void GreaterOrEqual
    <T, Object, Object>(
    T a, T
    b,
    String format, Object
    arg0,
    Object arg1
    )
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) < 0)
            AssertHelper(format, arg0, arg1);
    }


    public static void GreaterOrEqual
    <T, Object, Object, Object>(
    T a, T
    b,
    String format, Object
    arg0,
    Object arg1, Object
    arg2)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) < 0)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void Less
    <T>(
    T a, T
    b)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) >= 0)
            AssertHelper("Assertion failed: '{0}' is not less than '{1}'", a, b);
    }


    public static void Less
    <T>(
    T a, T
    b,
    String message
    )
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) >= 0)
            AssertHelper(message);
    }


    public static void Less
    <T>(
    T a, T
    b,
    String format, Object
    ...args)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) >= 0)
            AssertHelper(format, args);
    }


    public static void Less
    <T, Object>(
    T a, T
    b,
    String format, Object
    arg0)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) >= 0)
            AssertHelper(format, arg0);
    }


    public static void Less
    <T, Object, Object>(
    T a, T
    b,
    String format, Object
    arg0,
    Object arg1
    )
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) >= 0)
            AssertHelper(format, arg0, arg1);
    }


    public static void Less
    <T, Object, Object, Object>(
    T a, T
    b,
    String format, Object
    arg0,
    Object arg1, Object
    arg2)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) >= 0)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void LessOrEqual
    <T>(
    T a, T
    b)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) > 0)
            AssertHelper("Assertion failed: '{0}' is not less or equal to '{1}'", a, b);
    }


    public static void LessOrEqual
    <T>(
    T a, T
    b,
    String message
    )
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) > 0)
            AssertHelper(message);
    }


    public static void LessOrEqual
    <T>(
    T a, T
    b,
    String format, Object
    ...args)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) > 0)
            AssertHelper(format, args);
    }


    public static void LessOrEqual
    <T, Object>(
    T a, T
    b,
    String format, Object
    arg0)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) > 0)
            AssertHelper(format, arg0);
    }


    public static void LessOrEqual
    <T, Object, Object>(
    T a, T
    b,
    String format, Object
    arg0,
    Object arg1
    )
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) > 0)
            AssertHelper(format, arg0, arg1);
    }


    public static void LessOrEqual
    <T, Object, Object, Object>(
    T a, T
    b,
    String format, Object
    arg0,
    Object arg1, Object
    arg2)
    where T
    :IComparable<T>

    {
        if (IsEnabled && a.CompareTo(b) > 0)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsInstanceOfType(Type type, Object o)
    {
        if (IsEnabled && (type == null || !type.IsInstanceOfType(o)))
            AssertHelper("Assertion failed: expected type of '{0}' but was '{1}'", type, o != null ? o.GetType() : (Type) null);
    }


    public static void IsInstanceOfType(Type type, Object o, String message)
    {
        if (IsEnabled && (type == null || !type.IsInstanceOfType(o)))
            AssertHelper(message);
    }


    public static void IsInstanceOfType(Type type, Object o, String format, Object... args)
    {
        if (IsEnabled && (type == null || !type.IsInstanceOfType(o)))
            AssertHelper(format, args);
    }


    public static void IsInstanceOfType(Type type, Object o, String format, Object arg0)
    {
        if (IsEnabled && (type == null || !type.IsInstanceOfType(o)))
            AssertHelper(format, arg0);
    }


    public static void IsInstanceOfType(Type type, Object o, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && (type == null || !type.IsInstanceOfType(o)))
            AssertHelper(format, arg0, arg1);
    }


    public static void IsInstanceOfType(Type type, Object o, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && (type == null || !type.IsInstanceOfType(o)))
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsInstanceOfType
    <T>(
    Object o
    )

    {
        if (IsEnabled && !(o is T))
        AssertHelper("Assertion failed: expected type of '{0}' but was '{1}'", typeof(T), o != null ? o.GetType() : (Type) null);
    }


    public static void IsInstanceOfType
    <T>(
    Object o, String
    message)

    {
        if (IsEnabled && !(o is T))
        AssertHelper(message);
    }


    public static void IsInstanceOfType
    <T>(
    Object o, String
    format,Object...args)

    {
        if (IsEnabled && !(o is T))
        AssertHelper(format, args);
    }


    public static void IsInstanceOfType
    <T, Object>(
    Object o, String
    format,
    Object arg0
    )

    {
        if (IsEnabled && !(o is T))
        AssertHelper(format, arg0);
    }


    public static void IsInstanceOfType
    <T, Object, Object>(
    Object o, String
    format,
    Object arg0, Object
    arg1)

    {
        if (IsEnabled && !(o is T))
        AssertHelper(format, arg0, arg1);
    }


    public static void IsInstanceOfType
    <T, Object, Object, Object>(
    Object o, String
    format,
    Object arg0, Object
    arg1,
    Object arg2
    )

    {
        if (IsEnabled && !(o is T))
        AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsNotInstanceOfType(Type type, Object o)
    {
        if (IsEnabled && (type != null && type.IsInstanceOfType(o)))
            AssertHelper("Assertion failed: Object '{0}' is subtype of '{1}'", type, o != null ? o.GetType() : (Type) null);
    }


    public static void IsNotInstanceOfType(Type type, Object o, String message)
    {
        if (IsEnabled && (type != null && type.IsInstanceOfType(o)))
            AssertHelper(message);
    }


    public static void IsNotInstanceOfType(Type type, Object o, String format, Object... args)
    {
        if (IsEnabled && (type != null && type.IsInstanceOfType(o)))
            AssertHelper(format, args);
    }


    public static void IsNotInstanceOfType(Type type, Object o, String format, Object arg0)
    {
        if (IsEnabled && (type != null && type.IsInstanceOfType(o)))
            AssertHelper(format, arg0);
    }


    public static void IsNotInstanceOfType(Type type, Object o, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && (type != null && type.IsInstanceOfType(o)))
            AssertHelper(format, arg0, arg1);
    }


    public static void IsNotInstanceOfType(Type type, Object o, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && (type != null && type.IsInstanceOfType(o)))
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsNotInstanceOfType
    <T>(
    Object o
    )

    {
        if (IsEnabled && (o is T))
        AssertHelper("Assertion failed: Object '{0}' is subtype of '{1}'", typeof(T), o != null ? o.GetType() : (Type) null);
    }


    public static void IsNotInstanceOfType
    <T>(
    Object o, String
    message)

    {
        if (IsEnabled && (o is T))
        AssertHelper(message);
    }


    public static void IsNotInstanceOfType
    <T>(
    Object o, String
    format,Object...args)

    {
        if (IsEnabled && (o is T))
        AssertHelper(format, args);
    }


    public static void IsNotInstanceOfType
    <T, Object>(
    Object o, String
    format,
    Object arg0
    )

    {
        if (IsEnabled && (o is T))
        AssertHelper(format, arg0);
    }


    public static void IsNotInstanceOfType
    <T, Object, Object>(
    Object o, String
    format,
    Object arg0, Object
    arg1)

    {
        if (IsEnabled && (o is T))
        AssertHelper(format, arg0, arg1);
    }


    public static void IsNotInstanceOfType
    <T, Object, Object, Object>(
    Object o, String
    format,
    Object arg0, Object
    arg1,
    Object arg2
    )

    {
        if (IsEnabled && (o is T))
        AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsEmpty(String str)
    {
        if (IsEnabled && !String.IsNullOrEmpty(str))
            AssertHelper("Assertion failed: String is not empty '{0}'", str);
    }


    public static void IsEmpty(String str, String message)
    {
        if (IsEnabled && !String.IsNullOrEmpty(str))
            AssertHelper(message);
    }


    public static void IsEmpty(String str, String format, Object... args)
    {
        if (IsEnabled && !String.IsNullOrEmpty(str))
            AssertHelper(format, args);
    }


    public static void IsEmpty(String str, String format, Object arg0)
    {
        if (IsEnabled && !String.IsNullOrEmpty(str))
            AssertHelper(format, arg0);
    }


    public static void IsEmpty(String str, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && !String.IsNullOrEmpty(str))
            AssertHelper(format, arg0, arg1);
    }


    public static void IsEmpty(String str, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && !String.IsNullOrEmpty(str))
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsNotEmpty(String str)
    {
        if (IsEnabled && String.IsNullOrEmpty(str))
            AssertHelper("Assertion failed: String is null or empty '{0}'", str);
    }


    public static void IsNotEmpty(String str, String message)
    {
        if (IsEnabled && String.IsNullOrEmpty(str))
            AssertHelper(message);
    }


    public static void IsNotEmpty(String str, String format, Object... args)
    {
        if (IsEnabled && String.IsNullOrEmpty(str))
            AssertHelper(format, args);
    }


    public static void IsNotEmpty(String str, String format, Object arg0)
    {
        if (IsEnabled && String.IsNullOrEmpty(str))
            AssertHelper(format, arg0);
    }


    public static void IsNotEmpty(String str, String format, Object arg0, Object arg1)
    {
        if (IsEnabled && String.IsNullOrEmpty(str))
            AssertHelper(format, arg0, arg1);
    }


    public static void IsNotEmpty(String str, String format, Object arg0, Object arg1, Object arg2)
    {
        if (IsEnabled && String.IsNullOrEmpty(str))
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsEmpty
    <T>(
    ICollection<T> collection
    )

    {
        if (IsEnabled && collection != null && collection.Count == 0)
            AssertHelper("Assertion failed: collection is null or not empty '{0}'", collection);
    }


    public static void IsEmpty
    <T>(
    ICollection<T> collection, String
    message)

    {
        if (IsEnabled && collection != null && collection.Count == 0)
            AssertHelper(message);
    }


    public static void IsEmpty
    <T>(
    ICollection<T> collection, String
    format,Object...args)

    {
        if (IsEnabled && collection != null && collection.Count == 0)
            AssertHelper(format, args);
    }


    public static void IsEmpty
    <T, Object>(
    ICollection<T> collection, String
    format,
    Object arg0
    )

    {
        if (IsEnabled && collection != null && collection.Count == 0)
            AssertHelper(format, arg0);
    }


    public static void IsEmpty
    <T, Object, Object>(
    ICollection<T> collection, String
    format,
    Object arg0, Object
    arg1)

    {
        if (IsEnabled && collection != null && collection.Count == 0)
            AssertHelper(format, arg0, arg1);
    }


    public static void IsEmpty
    <T, Object, Object, Object>(
    ICollection<T> collection, String
    format,
    Object arg0, Object
    arg1,
    Object arg2
    )

    {
        if (IsEnabled && collection != null && collection.Count == 0)
            AssertHelper(format, arg0, arg1, arg2);
    }


    public static void IsNotEmpty
    <T>(
    ICollection<T> collection
    )

    {
        if (IsEnabled && collection != null && collection.Count != 0)
            AssertHelper("Assertion failed: collection is null or empty '{0}'", collection);
    }


    public static void IsNotEmpty
    <T>(
    ICollection<T> collection, String
    message)

    {
        if (IsEnabled && collection != null && collection.Count != 0)
            AssertHelper(message);
    }


    public static void IsNotEmpty
    <T>(
    ICollection<T> collection, String
    format,Object...args)

    {
        if (IsEnabled && collection != null && collection.Count != 0)
            AssertHelper(format, args);
    }


    public static void IsNotEmpty
    <T, Object>(
    ICollection<T> collection, String
    format,
    Object arg0
    )

    {
        if (IsEnabled && collection != null && collection.Count != 0)
            AssertHelper(format, arg0);
    }


    public static void IsNotEmpty
    <T, Object, Object>(
    ICollection<T> collection, String
    format,
    Object arg0, Object
    arg1)

    {
        if (IsEnabled && collection != null && collection.Count != 0)
            AssertHelper(format, arg0, arg1);
    }


    public static void IsNotEmpty
    <T, Object, Object, Object>(
    ICollection<T> collection, String
    format,
    Object arg0, Object
    arg1,
    Object arg2
    )

    {
        if (IsEnabled && collection != null && collection.Count != 0)
            AssertHelper(format, arg0, arg1, arg2);
    }

    private static void AssertHelper(String format, Object... args)
    {
        String message = StringUtils.TryFormat(format, args);
        String stackTrace = StackTrace.ExtractStackTrace(3);

        Platform.AssertMessage(message, stackTrace);

        try
        {
            if (callback != null)
                callback(message, stackTrace);
        }
        catch (Exception)
        {
        }
    }

    private static String toString(Object obj)
    {
        return obj != null ? obj.toString() : "null";
    }

    private static boolean IsEnabled;
}
