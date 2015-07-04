package com.spacemadness.lunar.core;

/**
 * Created by alementuev on 7/1/15.
 */
public interface Notification
{
    Object Get(String key);
    String getString(String key);
    boolean getBool(String key, boolean defaultValue);
    int getInt(String key, int defaultValue);
    float getFloat(String key, float defaultValue);

    String getName();
    Object getSender();
}
