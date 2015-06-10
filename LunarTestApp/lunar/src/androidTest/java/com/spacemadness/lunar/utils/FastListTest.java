package com.spacemadness.lunar.utils;

import junit.framework.TestCase;

/**
 * Created by alementuev on 6/8/15.
 */
public class FastListTest extends TestCase
{
    public void testAddFirstItem()
    {
        Node node = new Node("value");

        FastList<Node> list = new FastList<>();
        list.AddFirstItem(node);

        assertList(list, node);
    }

    public void testAddMultipleFirstItems()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddFirstItem(node1);
        list.AddFirstItem(node2);
        list.AddFirstItem(node3);

        assertList(list, node3, node2, node1);
    }

    public void testAddLastItem()
    {
        Node node = new Node("value");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node);

        assertList(list, node);
    }

    public void testAddMultipleLastItems()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        assertList(list, node1, node2, node3);
    }

    public void testInsertBeforeFirstItem()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.InsertBeforeItem(node1, node3);

        assertList(list, node3, node1, node2);
    }

    public void testInsertBeforeSecondItem()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.InsertBeforeItem(node2, node3);

        assertList(list, node1, node3, node2);
    }

    public void testInsertAfterFirstItem()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.InsertAfterItem(node1, node3);

        assertList(list, node1, node3, node2);
    }

    public void testInsertAfterSecondItem()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.InsertAfterItem(node2, node3);

        assertList(list, node1, node2, node3);
    }

    public void testRemoveItem1()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        list.RemoveItem(node1);
        assertList(list, node2, node3);

        list.RemoveItem(node2);
        assertList(list, node3);

        list.RemoveItem(node3);
        assertList(list);
    }

    public void testRemoveItem2()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        list.RemoveItem(node3);
        assertList(list, node1, node2);

        list.RemoveItem(node2);
        assertList(list, node1);

        list.RemoveItem(node1);
        assertList(list);
    }

    public void testRemoveItem3()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        list.RemoveItem(node2);
        assertList(list, node1, node3);

        list.RemoveItem(node1);
        assertList(list, node3);

        list.RemoveItem(node3);
        assertList(list);
    }

    public void testRemoveFirstItem()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        list.RemoveFirstItem();
        assertList(list, node2, node3);

        list.RemoveFirstItem();
        assertList(list, node3);

        list.RemoveFirstItem();
        assertList(list);
    }

    public void testRemoveLastItem()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        list.RemoveLastItem();
        assertList(list, node1, node2);

        list.RemoveLastItem();
        assertList(list, node1);

        list.RemoveLastItem();
        assertList(list);
    }

    public void testContainsLastItem()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");
        Node node4 = new Node("value4");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        assertTrue(list.ContainsItem(node1));
        assertTrue(list.ContainsItem(node2));
        assertTrue(list.ContainsItem(node3));
        assertFalse(list.ContainsItem(node4));
    }

    public void testClear()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        list.Clear();

        assertList(list);
    }

    public void testCount()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();
        list.AddLastItem(node1);
        list.AddLastItem(node2);
        list.AddLastItem(node3);

        assertEquals(3, list.Count());
    }

    public void testCountEmpty()
    {
        FastList<Node> list = new FastList<>();
        assertEquals(0, list.Count());
    }

    public void testListFirst()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();

        list.AddFirstItem(node1);
        assertSame(node1, list.ListFirst());

        list.AddFirstItem(node2);
        assertSame(node2, list.ListFirst());

        list.AddFirstItem(node3);
        assertSame(node3, list.ListFirst());
    }

    public void testListLast()
    {
        Node node1 = new Node("value1");
        Node node2 = new Node("value2");
        Node node3 = new Node("value3");

        FastList<Node> list = new FastList<>();

        list.AddLastItem(node1);
        assertSame(node1, list.ListLast());

        list.AddLastItem(node2);
        assertSame(node2, list.ListLast());

        list.AddLastItem(node3);
        assertSame(node3, list.ListLast());
    }

    private static <T extends FastListNode> void assertList(FastList<T> actual, T... expected)
    {
        assertEquals(expected.length, actual.Count());
        int index = 0;
        for (FastListNode<T> node = actual.ListFirst(); node != null; node = node.ListNodeNext())
        {
            assertSame(expected[index++], node);
        }
        assertEquals(expected.length, index);
    }


    private static class Node extends FastListNode<Node>
    {
        public final String value;

        public Node(String value)
        {
            this.value = value;
        }
    }
}