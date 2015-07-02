package com.spacemadness.lunar.core;

/**
 * Created by alementuev on 7/1/15.
 */
public interface Notification
{
    <T> T Get(String key, Class<? extends T> cls);

    Object Get(String key);

    String getName();

    Object getSender();
}
