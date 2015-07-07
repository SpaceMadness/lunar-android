package com.spacemadness.lunar.console;

public interface ListCommandsFilter<T extends CCommand>
{
    boolean accept(T command);
}
