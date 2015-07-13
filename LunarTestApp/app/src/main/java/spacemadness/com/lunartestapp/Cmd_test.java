package spacemadness.com.lunartestapp;

import com.spacemadness.lunar.console.CCommand;
import com.spacemadness.lunar.console.annotations.Command;
import com.spacemadness.lunar.console.annotations.CommandOption;

@Command(Name = "test", Values = "arg1,arg2,arg3")
public class Cmd_test extends CCommand
{
    @CommandOption(ShortName = "o1")
    private boolean opt1;

    @CommandOption(ShortName = "o2")
    private String opt2;

    @CommandOption(ShortName = "o3", Values = "val1,val2,val3")
    private String opt3;

    void execute(String arg)
    {
    }
}
