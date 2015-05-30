package com.spacemadness.lunar.core;

/**
 * Created by weee on 5/28/15.
 */
public class NotificationInfo
{
    public String name;
    public NotificationDelegate del;

    public NotificationInfo(String name, NotificationDelegate del)
    {
        this.name = name;
        this.del = del;
    }
}
