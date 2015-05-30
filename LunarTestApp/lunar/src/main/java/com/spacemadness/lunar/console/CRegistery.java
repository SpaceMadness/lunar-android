package com.spacemadness.lunar.console;

/**
 * Created by alementuev on 5/29/15.
 */
static class CRegistery
{
    private static CommandLookup m_commandsLookup = new CommandLookup();
    private static CommandList m_commands = new CommandList(); // TODO: use fast list

    //////////////////////////////////////////////////////////////////////////////

    #region Commands resolver

    [System.Diagnostics.Conditional("UNITY_EDITOR")]
    public static void ResolveCommands()
    {
        List<CCommand> commands = RuntimeResolver.ResolveCommands();
        for (int i = 0; i < commands.Count; ++i)
        {
            Register(commands[i]);
        }
    }

    #endregion

    //////////////////////////////////////////////////////////////////////////////

    #region Commands registry

    public static boolean Register(CCommand cmd)
    {
        if (cmd.Name.StartsWith("@"))
        {
            cmd.Flags |= CCommandFlags.Hidden;
        }

        return AddCommand(cmd);
    }

    public static boolean Unregister(CCommand cmd)
    {
        return RemoveCommand(cmd);
    }

    internal static boolean Unregister(ListCommandsFilter<CCommand> filter)
    {
        boolean unregistered = false;

        for (LinkedListNode<CCommand> node = m_commands.First; node != null;)
        {
            LinkedListNode<CCommand> next = node.Next;
            CCommand cmd = node.Value;
            if (filter(cmd))
            {
                unregistered |= Unregister(cmd);
            }

            node = next;
        }

        return unregistered;
    }

    public static void Clear()
    {
        m_commands.Clear();
        m_commandsLookup.Clear();
    }

    internal static IList<CCommand> ListCommands(String prefix = null, CommandListOptions options = CommandListOptions.None)
    {
        return ListCommands(ReusableLists.NextAutoRecycleList<CCommand>(), prefix, options);
    }

    internal static IList<CCommand> ListCommands(IList<CCommand> outList, String prefix = null, CommandListOptions options = CommandListOptions.None)
    {
        return ListCommands(outList, delegate(CCommand cmd)
        {
            return ShouldListCommand(cmd, prefix, options);
        });
    }

    internal static IList<CCommand> ListCommands(ListCommandsFilter<CCommand> filter)
    {
        return ListCommands(ReusableLists.NextAutoRecycleList<CCommand>(), filter);
    }

    internal static IList<CCommand> ListCommands(IList<CCommand> outList, ListCommandsFilter<CCommand> filter)
    {
        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        foreach (CCommand command in m_commands)
        {
            if (filter(command))
            {
                outList.Add(command);
            }
        }

        return outList;
    }

    internal static boolean ShouldListCommand(CCommand cmd, String prefix, CommandListOptions options = CommandListOptions.None)
    {
        if (cmd.IsDebug && (options & CommandListOptions.Debug) == 0)
        {
            return false;
        }

        if (cmd.IsSystem && (options & CommandListOptions.System) == 0)
        {
            return false;
        }

        if (cmd.IsHidden && (options & CommandListOptions.Hidden) == 0)
        {
            return false;
        }

        return prefix == null || StringUtils.StartsWithIgnoreCase(cmd.Name, prefix);
    }

    private static boolean AddCommand(CCommand cmd)
    {
        String name = cmd.Name;
        for (LinkedListNode<CCommand> node = m_commands.First; node != null; node = node.Next)
        {
            CCommand otherCmd = node.Value;
            if (cmd == otherCmd)
            {
                return false; // no duplicates
            }

            String otherName = otherCmd.Name;
            int compare = name.CompareTo(otherName);
            if (compare < 0)
            {
                m_commands.AddBefore(node, cmd);
                m_commandsLookup.Add(cmd.Name, cmd);
                return true;
            }

            if (compare == 0)
            {
                node.Value = cmd;
                return true;
            }
        }

        m_commands.AddLast(cmd);
        m_commandsLookup.Add(cmd.Name, cmd);

        return true;
    }

    private static boolean RemoveCommand(CCommand command)
    {
        if (m_commands.Remove(command))
        {
            m_commandsLookup.Remove(command.Name);
            return true;
        }

        return false;
    }

    public static CCommand FindCommand(String name)
    {
        CCommand cmd;
        if (name != null && m_commandsLookup.TryGetValue(name, out cmd))
        {
            return cmd;
        }

        return null;
    }

    #endregion

    //////////////////////////////////////////////////////////////////////////////

    #region Delegate commands

    public static boolean RegisterDelegate(Delegate action)
    {
        if (action == null)
        {
            throw new NullPointerException("Action is null");
        }

        return RegisterDelegate(action.Method.Name, action);
    }

    public static boolean RegisterDelegate(String name, Delegate action)
    {
        if (name == null)
        {
            throw new NullPointerException("Name is null");
        }
        
        if (action == null)
        {
            throw new NullPointerException("Action is null");
        }

        CCommand existingCmd = FindCommand(name);
        if (existingCmd != null)
        {
            CDelegateCommand delegateCmd = existingCmd as CDelegateCommand;
            if (delegateCmd != null)
            {
                Log.w("Overriding command: {0}", name);
                delegateCmd.ActionDelegate = action;

                return true;
            }

            Log.e("Another command with the same name exists: {0}", name);
            return false;
        }
        
        return Register(new CDelegateCommand(name, action));
    }

    public static boolean Unregister(String commandName)
    {
        CCommand cmd = FindCommand(commandName);
        if (cmd != null)
        {
            if (cmd is CDelegateCommand)
            {
                Unregister(cmd);
                return true;
            }

            Log.e("Unable to unregister a non-delegate command: {0}", cmd);
            return false;
        }

        return false;
    }

    public static boolean Unregister(Delegate del)
    {
        return Unregister(delegate(CCommand cmd)
        {
            CDelegateCommand delegateCmd = cmd as CDelegateCommand;
            return delegateCmd != null && delegateCmd.ActionDelegate == del;
        });
    }

    public static boolean UnregisterAll(Object target)
    {
        return target != null && Unregister(delegate(CCommand cmd)
        {
            CDelegateCommand delegateCmd = cmd as CDelegateCommand;
            return delegateCmd != null && delegateCmd.ActionDelegate.Target == target;
        });
    }

    #endregion

    //////////////////////////////////////////////////////////////////////////////

    #region Cvars

    public static boolean Register(CVar cvar)
    {
        return Register(new CVarCommand(cvar));
    }

    public static boolean Unregister(CVar cvar)
    {
        CCommand cmd = FindCvarCommand(cvar.Name);
        return cmd != null && Unregister(cmd);
    }

    public static IList<CVar> ListVars(String prefix = null, CommandListOptions options = CommandListOptions.None)
    {
        return ListVars(ReusableLists.NextAutoRecycleList<CVar>(), prefix, options);
    }

    public static IList<CVar> ListVars(IList<CVar> outList, String prefix = null, CommandListOptions options = CommandListOptions.None)
    {
        return ListVars(outList, delegate(CVarCommand cmd)
        {
            return ShouldListCommand(cmd, prefix, options);
        });
    }

    internal static IList<CVar> ListVars(ListCommandsFilter<CVarCommand> filter)
    {
        return ListVars(ReusableLists.NextAutoRecycleList<CVar>(), filter);
    }

    internal static IList<CVar> ListVars(IList<CVar> outList, ListCommandsFilter<CVarCommand> filter)
    {
        if (filter == null)
        {
            throw new NullPointerException("Filter is null");
        }

        foreach (CCommand cmd in m_commands)
        {
            CVarCommand varCmd = cmd as CVarCommand;
            if (varCmd != null && filter(varCmd))
            {
                outList.Add(varCmd.cvar);
            }
        }

        return outList;
    }

    internal static CVarCommand FindCvarCommand(String name)
    {
        foreach (CCommand cmd in m_commands)
        {
            CVarCommand varCmd = cmd as CVarCommand;
            if (varCmd != null && varCmd.cvar.Name == name)
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

    #endregion

    //////////////////////////////////////////////////////////////////////////////

    #region Aliases

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
            CAliasCommand cmd = existingCmd as CAliasCommand;
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
        CAliasCommand cmd = FindCommand(name) as CAliasCommand;
        return cmd != null && Unregister(cmd);
    }

    internal static IList<CAliasCommand> ListAliases(String prefix = null, CommandListOptions options = CommandListOptions.None)
    {
        return ListAliases(ReusableLists.NextAutoRecycleList<CAliasCommand>(), prefix, options);
    }

    internal static IList<CAliasCommand> ListAliases(IList<CAliasCommand> outList, String prefix = null, CommandListOptions options = CommandListOptions.None)
    {
        foreach (CCommand cmd in m_commands)
        {
            CAliasCommand aliasCmd = cmd as CAliasCommand;
            if (aliasCmd != null && ShouldListCommand(aliasCmd, prefix, options))
            {
                outList.Add(aliasCmd);
            }
        }

        return outList;
    }

    #endregion

    public static void GetSuggested(String token, CommandList outList)
    {
        foreach (CCommand command in m_commands)
        {
            if (command.StartsWith(token))
            {
                outList.AddLast(command);
            }
        }
    }
}
