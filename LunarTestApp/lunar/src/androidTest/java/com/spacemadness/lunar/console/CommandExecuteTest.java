package com.spacemadness.lunar.console;

import com.spacemadness.lunar.console.annotations.Arg;
import com.spacemadness.lunar.utils.StringUtils;

public class CommandExecuteTest extends CCommandTestCase
{
    public void testExecuteStringNoArgs()
    {
        execute("String ");
        assertResult("String");
    }

    public void testExecuteStringSingleArg()
    {
        execute("String '10 and up'");
        assertResult("String 10 and up");
    }

    public void testExecuteStringTwoArgs()
    {
        execute("String 10 '20 and up'");
        assertResult("String 10 20 and up");
    }

    public void testExecuteStringMultipleArgs()
    {
        execute("String 10 20 '30 and up'");
        assertResult("String 10 20 30 and up");
    }

    public void testExecuteStringMultipleArgsWrongCount()
    {
        execute("int 10 20 and up");
        assertResult(
                "  Wrong arguments",
                "  usage: int\n" +
                "         int <value>\n" +
                "         int <value1> <value2>\n" +
                "         int <value1> <value2> <value3>"
        );
    }

    public void testExecuteIntNoArgs()
    {
        execute("int ");
        assertResult("int");
    }

    public void testExecuteIntSingleArg()
    {
        execute("int 10");
        assertResult("int 10");
    }

    public void testExecuteIntTwoArgs()
    {
        execute("int 10 20");
        assertResult("int 10 20");
    }

    public void testExecuteIntMultipleArgs()
    {
        execute("int 10 20 30");
        assertResult("int 10 20 30");
    }

    public void testExecuteIntMultipleArgsWrongCount()
    {
        execute("int 10 20 30 40");
        assertResult(
                "  Wrong arguments",
                "  usage: int\n" +
                "         int <value>\n" +
                "         int <value1> <value2>\n" +
                "         int <value1> <value2> <value3>"
        );
    }

    public void testExecuteFloatNoArgs()
    {
        execute("float");
        assertResult("float");
    }

    public void testExecuteFloatSingleArg()
    {
        execute("float 1.0");
        assertResult("float 1.0");
    }

    public void testExecuteFloatTwoArgs()
    {
        execute("float 1.0 2.0");
        assertResult("float 1.0 2.0");
    }

    public void testExecuteFloatMultipleArgs()
    {
        execute("float 1.0 2.0 3.0");
        assertResult("float 1.0 2.0 3.0");
    }

    public void testExecuteFloatMultipleArgsWrongCount()
    {
        execute("float 1.0 2.0 3.0 4.0");
        assertResult(
                "  Wrong arguments",
                "  usage: float\n" +
                "         float <value>\n" +
                "         float <value1> <value2>\n" +
                "         float <value1> <value2> <value3>"
        );
    }

    public void testExecuteBoolNoArgs()
    {
        execute("boolean");
        assertResult("boolean");
    }

    public void testExecuteBoolSingleArg()
    {
        execute("boolean 1");
        assertResult("boolean true");
    }

    public void testExecuteBoolTwoArgs()
    {
        execute("boolean 1 0");
        assertResult("boolean true false");
    }

    public void testExecuteBoolMultipleArgs()
    {
        execute("boolean 1 0 1");
        assertResult("boolean true false true");
    }

    public void testExecuteBoolMultipleArgsWrongCount()
    {
        execute("boolean 1 0 1 0");
        assertResult(
                "  Wrong arguments",
                "  usage: boolean\n" +
                "         boolean <value>\n" +
                "         boolean <value1> <value2>\n" +
                "         boolean <value1> <value2> <value3>"
        );
    }

    @Override
    protected void runSetup()
    {
        super.runSetup();

        this.IsTrackTerminalLog = true;

        RegisterCommands(
            new Cmd_string(),
            new Cmd_int(),
            new Cmd_float(),
            new Cmd_boolean(),
            new Cmd_strings(),
            new Cmd_ints(),
            new Cmd_floats(),
            new Cmd_bools()
        );
    }

    class Cmd_string extends CCommand
    {
        public Cmd_string()
        {
            super("String");
        }

        void execute()
        {
            getResultList().add(this.Name);
        }

        void execute(@Arg("value") String value)
        {
            getResultList().add(this.Name + " " + value);
        }

        void execute(@Arg("value1") String value1, @Arg("value2") String value2)
        {
            getResultList().add(this.Name + " " + value1 + " " + value2);
        }

        void execute(@Arg("value1") String value1, @Arg("value2") String value2, @Arg("value3") String value3)
        {
            getResultList().add(this.Name + " " + value1 + " " + value2 + " " + value3);
        }
    }

    class Cmd_int extends CCommand
    {
        public Cmd_int()
        {
            super("int");
        }

        void execute()
        {
            getResultList().add(this.Name);
        }

        void execute(@Arg("value") int value)
        {
            getResultList().add(this.Name + " " + value);
        }

        void execute(@Arg("value1") int value1, @Arg("value2") int value2)
        {
            getResultList().add(this.Name + " " + value1 + " " + value2);
        }

        void execute(@Arg("value1") int value1, @Arg("value2") int value2, @Arg("value3") int value3)
        {
            getResultList().add(this.Name + " " + value1 + " " + value2 + " " + value3);
        }
    }

    class Cmd_float extends CCommand
    {
        public Cmd_float()
        {
            super("float");
        }

        void execute()
        {
            getResultList().add(this.Name);
        }

        void execute(@Arg("value") float value)
        {
            getResultList().add(this.Name + " " + value);
        }

        void execute(@Arg("value1") float value1, @Arg("value2") float value2)
        {
            getResultList().add(this.Name + " " + value1 + " " + value2);
        }

        void execute(@Arg("value1") float value1, @Arg("value2") float value2, @Arg("value3") float value3)
        {
            getResultList().add(this.Name + " " + value1 + " " + value2 + " " + value3);
        }
    }

    class Cmd_boolean extends CCommand
    {
        public Cmd_boolean()
        {
            super("boolean");
        }

        void execute()
        {
            getResultList().add(this.Name);
        }

        void execute(@Arg("value") boolean value)
        {
            getResultList().add(this.Name + " " + value);
        }

        void execute(@Arg("value1") boolean value1, @Arg("value2") boolean value2)
        {
            getResultList().add(this.Name + " " + value1 + " " + value2);
        }

        void execute(@Arg("value1") boolean value1, @Arg("value2") boolean value2, @Arg("value3") boolean value3)
        {
            getResultList().add(this.Name + " " + value1 + " " + value2 + " " + value3);
        }
    }

    class Cmd_strings extends CCommand
    {
        public Cmd_strings()
        {
            super("strings");
        }

        void execute()
        {
            getResultList().add("strings no args");
        }

        void execute(String[] args)
        {
            getResultList().add("strings " + StringUtils.Join(args));
        }
    }

    class Cmd_ints extends CCommand
    {
        public Cmd_ints()
        {
            super("ints");
        }

        void execute()
        {
            getResultList().add("ints no args");
        }

        void execute(int[] args)
        {
            getResultList().add("ints " + StringUtils.Join(args));
        }
    }

    class Cmd_floats extends CCommand
    {
        public Cmd_floats()
        {
            super("floats");
        }

        void execute()
        {
            getResultList().add("floats no args");
        }

        void execute(float[] args)
        {
            getResultList().add("floats " + StringUtils.Join(args));
        }
    }

    class Cmd_bools extends CCommand
    {
        public Cmd_bools()
        {
            super("bools");
        }

        void execute()
        {
            getResultList().add("bools no args");
        }

        void execute(boolean[] args)
        {
            getResultList().add("bools " + StringUtils.Join(args));
        }
    }
}
