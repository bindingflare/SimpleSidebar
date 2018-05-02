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

public class PlayerAdminCommand implements CommandExecutor {
    private Sidebar sidebar;
    private Messenger message;

    public PlayerAdminCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        message = plugin.getMessenger();
        Config config = plugin.getPgConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        // Player
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (sender.hasPermission("simplesidebar.admin")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target == null) {
                        message.sendToPlayer(player, "Error code AC-001-TARGET_NULL");
                        return true;
                    }

                    int sidebarIndex = sidebar.getSidebarIndexOf(target);

                    String name = sidebar.getSidebarName(sidebarIndex);
                    String[] aliases = sidebar.getSidebarAliases(sidebarIndex);

                    if (name != null) {
                        message.sendToPlayer(player, args[0] + " is using sidebar " + name + " (Index: " + sidebarIndex + ")");
                        message.sendToPlayer(player, "Aliases: " + Arrays.toString(aliases));
                        return true;
                    } else {
                        message.sendToPlayer(player, "Error code AC-002-NAME_NOT_FOUND");
                    }
                } else if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target == null) {
                        message.sendToPlayer(player, "Error code AC-003-TARGET_NULL");
                        return true;
                    }

                    int sidebarIndex;

                    try {
                        sidebarIndex = Integer.parseInt(args[1]);
                    } catch (Exception e) {
                        message.sendToPlayer(player, "Error code AC-004-ARG_NOT_INTEGER");
                        return true;
                    }

                    if (sidebarIndex >= 0 && sidebarIndex < sidebar.getSidebarCount()) {
                        message.sendToPlayer(player, "Setting sidebar of " + args[0] + " to sidebar index " + sidebarIndex);
                        sidebar.setSidebar(target, sidebarIndex);
                        return true;
                    } else {
                        message.sendToPlayer(player, "Error code AC-005-INDEX_OUT_OF_BOUNDS");
                        return true;
                    }
                }
            }
        }

        return false;
    }
}