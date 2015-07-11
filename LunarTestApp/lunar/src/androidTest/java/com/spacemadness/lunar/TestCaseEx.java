package com.spacemadness.lunar;

import android.content.Context;
import android.test.AndroidTestCase;

import com.spacemadness.lunar.console.AppTerminalImp;
import com.spacemadness.lunar.utils.ClassUtilsEx;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TestCaseEx extends AndroidTestCase
{
    private List<String> result;

    //////////////////////////////////////////////////////////////////////////////
    // Lifecycle

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        ClassUtilsEx.setField(Config.class, null, "isTesting", true);
        ClassUtilsEx.setField(Config.class, null, "isUI", false);
        ClassUtilsEx.setField(AppTerminalImp.class, null, "runtimePlatformFactory", new RuntimePlatformFactory()
        {
            @Override
            public RuntimePlatform createRuntimePlatform(Context context)
            {
                return TestCaseEx.this.createRuntimePlatform(getContext());
            }
        });

        AppTerminal.initialize(getContext());

        MockRuntimePlatform.deleteConfigsDir();

        result = new ArrayList<>();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        MockRuntimePlatform.deleteConfigsDir();
        AppTerminal.destroy();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Assert helpers

    protected void assertResult(String... expected)
    {
        assertResult(result, expected);
    }

    protected void assertResult(List<String> actual, String... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual), expected.length, actual.size());

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual.get(i));
        }
    }

    protected void assertResult(String[] actual, String... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(int[] actual, int... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(float[] actual, float... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(boolean[] actual, boolean... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Inheritance

    protected MockRuntimePlatform createRuntimePlatform(Context context)
    {
        return new MockRuntimePlatform(context);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Helpers

    protected void waitUntilNotificationsDispatched() throws InterruptedException
    {
        MockRuntimePlatform.waitUntilNotificationsDispatched();
    }

    protected void waitUntilTimersDispatched() throws InterruptedException
    {
        MockRuntimePlatform.waitUntilTimersDispatched();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Results

    protected void clearResult()
    {
        result.clear();
    }

    public List<String> getResultList()
    {
        return result;
    }
}
