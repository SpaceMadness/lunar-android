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
        assertEquals("Expected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(result),
                expected.length, result.size());

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("Expected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(result),
                    expected[i], result.get(i));
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
