package com.spacemadness.lunar.console;

import com.spacemadness.lunar.core.Each;
import com.spacemadness.lunar.debug.Log;
import com.spacemadness.lunar.utils.ClassUtils;
import com.spacemadness.lunar.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alementuev on 5/29/15.
 */
public class CRegistery
{
    private static Map<String, CCommand> m_commandsLookup = new HashMap<String, CCommand>(); // FIXME: rename
    private static List<CCommand> m_commands = new ArrayList<CCommand>(); // FIXME: rename and use linked list

    //////////////////////////////////////////////////////////////////////////////
    // Commands resolver

    public static void ResolveCommands()
    {
        List<CCommand> commands = RuntimeResolver.ResolveCommands();
        for (int i = 0; i < commands.size(); ++i)
        {
            Log.d("Resolve command: " + commands.get(i).Name);
            Register(commands.get(i));
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Commands registry

    public static boolean Register(CCommand cmd)
    {
        if (cmd.Name.startsWith("@"))
        {
            cmd.Flags |= CCommandFlags.Hidden;
        }

        return AddCommand(cmd);
    }

    public static boolean Unregister(CCommand cmd)
    {
        return RemoveCommand(cmd);
    }

    static boolean Unregister(ListCommandsFilter<CCommand> filter)
    {
        boolean unregistered = false;

        for (int i = m_commands.size() - 1; i >= 0; --i)
        {
            CCommand cmd = m_commands.get(i);
            if (filter.accept(cmd))
            {
                unregistered |= Unregister(cmd);
            }
        }

        return unregistered;
    }

    public static void Clear()
    {
        m_commands.clear();
        m_commandsLookup.clear();
    }

    public static void iterateCommands(Each<CCommand> each)
    {
        if (each == null)
        {
            throw new NullPointerException("Each is null");
        }

        for (CCommand command : m_commands)
        {
            each.onElement(command);
        }
    }

    public static List<CCommand> ListCommands()
    {
        return ListCommands((String) null);
    }

    public static List<CCommand> ListCommands(String prefix)
    {
        return ListCommands(prefix, 0);
    }

    public static List<CCommand> ListCommands(String prefix, int options)
    {
        return ListCommands(new ArrayList<CCommand>(), prefix, options);
    }

    public static List<CCommand> ListCommands(List<CCommand> outList, final String prefix, final int options)
    {
        return ListCommands(outList, new ListCommandsFilter<CCommand>()
        {
            @Override
            public boolean accept(CCommand command)
            {
                return ShouldListCommand(command, prefix, options);
            }
        });
    }

    public static List<CCommand> ListCommands(ListCommandsFilter<CCommand> filter)
    {
        return ListCommands(new ArrayList<CCommand>(), filter);
    }

    static List<CCommand> ListCommands(List<CCommand> outList, ListCommandsFilter<CCommand> filter)
    {
        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        for (CCommand command : m_commands)
        {
            if (filter.accept(command))
            {
                outList.add(command);
            }
        }

        return outList;
    }

    public static boolean ShouldListCommand(CCommand cmd, String prefix)
    {
        return ShouldListCommand(cmd, prefix, CommandListOptions.None);
    }

    public static boolean ShouldListCommand(CCommand cmd, String prefix, int options)
    {
        if (cmd.IsDebug() && (options & CommandListOptions.Debug) == 0)
        {
            return false;
        }

        if (cmd.IsSystem() && (options & CommandListOptions.System) == 0)
        {
            return false;
        }

        if (cmd.IsHidden() && (options & CommandListOptions.Hidden) == 0)
        {
            return false;
        }

        return prefix == null || StringUtils.StartsWithIgnoreCase(cmd.Name, prefix);
    }

    private static boolean AddCommand(CCommand cmd)
    {
        String name = cmd.Name;
        for (int i = 0; i < m_commands.size(); ++i)
        {
            CCommand otherCmd = m_commands.get(i);
            if (cmd == otherCmd)
            {
                return false; // no duplicates
            }

            String otherName = otherCmd.Name;
            int compare = name.compareTo(otherName);
            if (compare < 0)
            {
                m_commands.add(i, cmd);
                m_commandsLookup.put(cmd.Name, cmd);
                return true;
            }

            if (compare == 0)
            {
                m_commands.set(i, cmd);
                return true;
            }
        }

        m_commands.add(cmd);
        m_commandsLookup.put(cmd.Name, cmd);

        return true;
    }

    private static boolean RemoveCommand(CCommand command)
    {
        if (m_commands.remove(command))
        {
            m_commandsLookup.remove(command.Name);
            return true;
        }

        return false;
    }

    public static CCommand FindCommand(String name)
    {
        return m_commandsLookup.get(name);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Cvars

    public static boolean Register(CVar cvar)
    {
        return Register(new CVarCommand(cvar));
    }

    public static boolean Unregister(CVar cvar)
    {
        CCommand cmd = FindCvarCommand(cvar.Name());
        return cmd != null && Unregister(cmd);
    }

    public static List<CVar> ListVars(String prefix)
    {
        return ListVars(prefix, 0);
    }

    public static List<CVar> ListVars(String prefix, int options)
    {
        return ListVars(new ArrayList<CVar>(), prefix, options);
    }

    public static List<CVar> ListVars(List<CVar> outList, final String prefix, final int options)
    {
        return ListVars(outList, new ListCommandsFilter<CVarCommand>()
        {
            @Override
            public boolean accept(CVarCommand command)
            {
                return ShouldListCommand(command, prefix, options);
            }
        });
    }

    public static List<CVar> ListVars(ListCommandsFilter<CVarCommand> filter)
    {
        return ListVars(new ArrayList<CVar>(), filter);
    }

    public static List<CVar> ListVars(List<CVar> outList, ListCommandsFilter<CVarCommand> filter)
    {
        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        for (CCommand cmd : m_commands)
        {
            CVarCommand varCmd = ClassUtils.as(cmd, CVarCommand.class);
            if (varCmd != null && filter.accept(varCmd))
            {
                outList.add(varCmd.cvar);
            }
        }

        return outList;
    }

    public static CVarCommand FindCvarCommand(String name)
    {
        for (CCommand cmd : m_commands)
        {
            CVarCommand varCmd = ClassUtils.as(cmd, CVarCommand.class);
            if (varCmd != null && varCmd.cvar.Name().equals(name))
            {
                return varCmd;
            }
        }

        return null;
    }

    public static CVar FindCvar(String name)
    {
        CVarCommand cmd = FindCvarCommand(name);
        return cmd != null ? cmd.cvar : null;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Aliases

    public static void AddAlias(String alias, String commandLine)
    {
        if (alias == null)
        {
            throw new NullPointerException("Alias is null");
        }

        if (commandLine == null)
        {
            throw new NullPointerException("Command line is null");
        }

        CCommand existingCmd = FindCommand(alias);
        if (existingCmd != null)
        {
            CAliasCommand cmd = ClassUtils.as(existingCmd, CAliasCommand.class);
            if (cmd == null)
            {
                throw new CCommandException("Can't override command with alias: " + alias);
            }

            cmd.Alias = commandLine;
        }
        else
        {
            Register(new CAliasCommand(alias, commandLine));
        }
    }

    public static boolean RemoveAlias(String name)
    {
        CAliasCommand cmd = ClassUtils.as(FindCommand(name), CAliasCommand.class);
        return cmd != null && Unregister(cmd);
    }

    public static List<CAliasCommand> ListAliases()
    {
        return ListAliases(null);
    }

    public static List<CAliasCommand> ListAliases(String prefix)
    {
        return ListAliases(prefix, 0);
    }

    public static List<CAliasCommand> ListAliases(String prefix, int options)
    {
        return ListAliases(new ArrayList<CAliasCommand>(), prefix, options);
    }

    public static List<CAliasCommand> ListAliases(List<CAliasCommand> outList, String prefix, int options)
    {
        for (CCommand cmd : m_commands)
        {
            CAliasCommand aliasCmd = ClassUtils.as(cmd, CAliasCommand.class);
            if (aliasCmd != null && ShouldListCommand(aliasCmd, prefix, options))
            {
                outList.add(aliasCmd);
            }
        }

        return outList;
    }

    public static void GetSuggested(String token, List<CCommand> outList)
    {
        for (CCommand command : m_commands)
        {
            if (command.StartsWith(token))
            {
                outList.add(command);
            }
        }
    }
}
