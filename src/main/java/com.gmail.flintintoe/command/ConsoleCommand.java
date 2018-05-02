package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ConsoleCommand implements CommandExecutor {
    private Sidebar sidebar;
    private Messenger message;

    public ConsoleCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        message = plugin.getMessenger();
        Config config = plugin.getPgConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    message.sendToConsole("Error code CC-001-TARGET_NULL");
                    return true;
                }

                int sidebarIndex = sidebar.getSidebarIndexOf(target);

                String name = sidebar.getSidebarName(sidebarIndex);
                String[] aliases = sidebar.getSidebarAliases(sidebarIndex);

                if (name != null) {
                    message.sendToConsole(args[0] + " is using sidebar " + name + " (Index: " + sidebarIndex + ")");
                    message.sendToConsole("Aliases: " + Arrays.toString(aliases));
                    return true;
                } else {
                    message.sendToConsole("Error code CC-002-NAME_NOT_FOUND");
                }
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    message.sendToConsole("Error code CC-003-TARGET_NULL");
                    return true;
                }

                int sidebarIndex;

                try {
                    sidebarIndex = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    message.sendToConsole("Error code CC-004-ARG_NOT_INTEGER");
                    return true;
                }

                if (sidebarIndex >= 0 && sidebarIndex < sidebar.getSidebarCount()) {
                    message.sendToConsole("Setting sidebar of " + args[0] + " to sidebar index " + sidebarIndex);
                    sidebar.setSidebar(target, sidebarIndex);
                    return true;
                } else {
                    message.sendToConsole("Error code CC-005-INDEX_OUT_OF_BOUNDS");
                    return true;
                }
            }
        }
        return true;
    }
}
