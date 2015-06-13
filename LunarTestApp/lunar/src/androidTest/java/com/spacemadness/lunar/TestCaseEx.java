package com.spacemadness.lunar;

import com.spacemadness.lunar.utils.StringUtils;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alementuev on 6/9/15.
 */
public class TestCaseEx extends TestCase
{
    private List<String> result;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        result = new ArrayList<>();
    }

    protected void assertResult(String... expected)
    {
        assertResult(result, expected);
    }

    protected void assertResult(List<String> actual, String... expected)
    {
        assertEquals("Expected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.size());

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("Expected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual.get(i));
        }
    }

    protected void assertResult(String[] actual, String... expected)
    {
        assertEquals("Expected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("Expected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(int[] actual, int... expected)
    {
        assertEquals("Expected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("Expected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(float[] actual, float... expected)
    {
        assertEquals("Expected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("Expected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(boolean[] actual, boolean... expected)
    {
        assertEquals("Expected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("Expected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void clearResult()
    {
        result.clear();
    }

    public List<String> getResultList()
    {
        return result;
    }
}
