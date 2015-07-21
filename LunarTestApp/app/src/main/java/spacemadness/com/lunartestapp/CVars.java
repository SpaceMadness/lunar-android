package spacemadness.com.lunartestapp;

import com.spacemadness.lunar.console.CVar;
import com.spacemadness.lunar.console.annotations.CVarContainer;

@CVarContainer
public final class CVars
{
    public static final CVar c_bool = new CVar("c_bool", true);
    public static final CVar c_int = new CVar("c_int", 10);
    public static final CVar c_float = new CVar("c_float", 3.14f);
    public static final CVar c_string = new CVar("c_string", "Some string");
}
