package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 6/1/15.
 */
public interface ListOptionsFilter
{
    boolean accept(CCommand.Option opt);
}
