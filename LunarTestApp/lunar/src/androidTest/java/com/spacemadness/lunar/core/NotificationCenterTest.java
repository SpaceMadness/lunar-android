package com.spacemadness.lunar.core;

import com.spacemadness.lunar.MockNotificationCenter;
import com.spacemadness.lunar.utils.StringUtils;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationCenterTest extends TestCase
{
    private MockNotificationCenter notificationCenter;
    private List<MockNotificationListener> callbacks;

    public void testPostNotifications() throws Exception
    {
        final Object sender = new Object();

        notificationCenter.Register("foo", notificationDelegate1);
        notificationCenter.Register("foo", notificationDelegate2);
        notificationCenter.Register("foo", notificationDelegate3);

        notificationCenter.Post(sender, "foo");

        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate1, notificationDelegate2, notificationDelegate3);
    }

    public void testPostNotificationsImmediately() throws Exception
    {
        final Object sender = new Object();

        notificationCenter.Register("foo", notificationDelegate1);
        notificationCenter.Register("foo", notificationDelegate2);
        notificationCenter.Register("foo", notificationDelegate3);

        notificationCenter.PostImmediately(sender, "foo");
        assertCallbacks(notificationDelegate1, notificationDelegate2, notificationDelegate3);
    }

    public void testPostNotificationsWithData() throws Exception
    {
        final Object sender = new Object();

        notificationCenter.Register("foo", notificationDelegate1);
        notificationCenter.Register("foo", notificationDelegate2);
        notificationCenter.Register("foo", notificationDelegate3);

        notificationCenter.Post(sender, "foo",
                "key1", "value1", //
                "key2", "value2", //
                "key3", "value3"
        );

        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate1, notificationDelegate2, notificationDelegate3);

        assertSame(sender, notificationDelegate1.getSender());
        assertEquals("value1", (String) notificationDelegate1.getData().get("key1"));
        assertEquals("value2", (String) notificationDelegate1.getData().get("key2"));
        assertEquals("value3", (String) notificationDelegate1.getData().get("key3"));

        assertSame(sender, notificationDelegate1.getSender());
        assertEquals("value1", (String) notificationDelegate2.getData().get("key1"));
        assertEquals("value2", (String) notificationDelegate2.getData().get("key2"));
        assertEquals("value3", (String) notificationDelegate2.getData().get("key3"));

        assertSame(sender, notificationDelegate1.getSender());
        assertEquals("value1", (String) notificationDelegate3.getData().get("key1"));
        assertEquals("value2", (String) notificationDelegate3.getData().get("key2"));
        assertEquals("value3", (String) notificationDelegate3.getData().get("key3"));
    }

    public void testUnregisterCallbacks() throws Exception
    {
        final Object sender = new Object();

        notificationCenter.Register("foo", notificationDelegate1);
        notificationCenter.Register("foo", notificationDelegate2);
        notificationCenter.Register("foo", notificationDelegate3);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate1, notificationDelegate2, notificationDelegate3);

        notificationCenter.Unregister("foo", notificationDelegate2);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate1, notificationDelegate3);

        notificationCenter.Unregister("foo", notificationDelegate1);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate3);

        notificationCenter.Unregister("foo", notificationDelegate3);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks();
    }

    public void testUnregisterAllCallbacks() throws Exception
    {
        final Object sender = new Object();

        notificationCenter.Register("foo1", notificationDelegate1);
        notificationCenter.Register("foo1", notificationDelegate2);
        notificationCenter.Register("foo2", notificationDelegate1);
        notificationCenter.Register("foo3", notificationDelegate1);
        notificationCenter.Register("foo3", notificationDelegate3);

        notificationCenter.UnregisterAll(notificationDelegate1);

        notificationCenter.Post(sender, "foo1");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate2);

        notificationCenter.Post(sender, "foo2");
        notificationCenter.waitUntilNotificationsDispatched();

        notificationCenter.Post(sender, "foo3");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate3);
    }

    public void testUnregisterCallbackInsideSelf() throws Exception
    {
        final Object sender = new Object();

        NotificationDelegate listener = new MockNotificationListener("delete self")
        {
            @Override
            public void onNotification(Notification n)
            {
                super.onNotification(n);
                notificationCenter.Unregister("foo", this);
            }
        };

        notificationCenter.Register("foo", notificationDelegate1);
        notificationCenter.Register("foo", listener);
        notificationCenter.Register("foo", notificationDelegate2);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();

        assertCallbacks(notificationDelegate1, listener, notificationDelegate2);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate1, notificationDelegate2);
    }

    public void testUnregisterCallbackInsideOtherCallback() throws Exception
    {
        final Object sender = new Object();

        NotificationDelegate listener = new MockNotificationListener("delete other")
        {
            @Override
            public void onNotification(Notification n)
            {
                super.onNotification(n);
                notificationCenter.Unregister("foo", notificationDelegate2);
            }
        };

        notificationCenter.Register("foo", notificationDelegate1);
        notificationCenter.Register("foo", listener);
        notificationCenter.Register("foo", notificationDelegate2);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate1, listener);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertCallbacks(notificationDelegate1, listener);
    }

    public void testObjectsReuse() throws Exception
    {
        final Object sender = new Object();

        notificationCenter.Register("foo", notificationDelegate1);
        notificationCenter.Register("foo", notificationDelegate2);
        notificationCenter.Register("foo", notificationDelegate3);

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertEquals(1, notificationCenter.getNotificationPoolSize());

        notificationCenter.Post(sender, "foo");
        notificationCenter.waitUntilNotificationsDispatched();
        assertEquals(1, notificationCenter.getNotificationPoolSize());
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        notificationCenter = new MockNotificationCenter();
        callbacks = new ArrayList<>();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        notificationCenter.waitUntilNotificationsDispatched();
    }

    private MockNotificationListener notificationDelegate1 = new MockNotificationListener("1");
    private MockNotificationListener notificationDelegate2 = new MockNotificationListener("2");
    private MockNotificationListener notificationDelegate3 = new MockNotificationListener("3");

    private void assertCallbacks(NotificationDelegate... expected)
    {
        assertEquals("Expected: " + StringUtils.Join(expected) + "\nActual: " + StringUtils.Join(callbacks), expected.length, callbacks.size());

        String message = "";
        for (int i = 0; i < expected.length; ++i)
        {
            if (expected[i] != callbacks.get(i))
            {
                message += expected[i] + "!=" + callbacks.get(i);
            }
        }

        assertTrue(message, message.length() == 0);

        callbacks.clear();
    }

    private class MockNotificationListener implements NotificationDelegate
    {
        private final String name;

        private Map<String, Object> data;
        private Object sender;

        public MockNotificationListener(String name)
        {
            this.name = name;
        }

        @Override
        public void onNotification(Notification n)
        {
            Map<String, Object> data = ((NotificationObject) n).getData();
            this.data = data != null ? new HashMap<>(data) : null;
            this.sender = n.getSender();

            callbacks.add(this);
        }

        public String getName()
        {
            return name;
        }

        public Map<String, Object> getData()
        {
            return data;
        }

        public Object getSender()
        {
            return sender;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}