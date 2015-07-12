package com.spacemadness.lunar.console.commands;

import com.spacemadness.lunar.console.CCommand;

public class CCommandMock extends CCommand
{
    public CCommandMock(String name)
    {
        this(name, true);
    }

    public CCommandMock(String name, boolean succeed)
    {
        super(name);
        this.IsSucceed = succeed;
    }

    boolean Execute(String[] args)
    {
        return this.IsSucceed;
    }

    public boolean IsSucceed;
}