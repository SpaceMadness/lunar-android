package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 5/29/15.
 */
class CAliasCommand extends CCommand
{
    public CAliasCommand(String name, String alias)
    {
        super(name);

        if (alias == null)
        {
            throw new NullPointerException("Alias is null");
        }

        this.Alias = alias;
    }

    private boolean Execute()
    {
        return ExecCommand(this.Alias, true);
    }

    public String Alias; // FIXME: public String Alias { get; internal set; }
}