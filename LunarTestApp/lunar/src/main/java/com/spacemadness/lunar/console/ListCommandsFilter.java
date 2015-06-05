package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 6/1/15.
 */
public interface ListCommandsFilter<T extends CCommand>
{
    boolean accept(T command);
}
