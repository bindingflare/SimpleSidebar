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

public class AdminCommand implements CommandExecutor {
    private Sidebar sidebar;
    private Messenger message;
    private Config config;

    public AdminCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        message = plugin.getMessenger();
        config = plugin.getPgConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        // Player
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (sender.hasPermission("simplesidebar.admin")) {
                if (args.length == 2) {
                    // Player where the command will apply to
                    Player target = Bukkit.getPlayer(args[0]);

                    int sidebarIndex = -2;

                    // Test first if target is valid
                    if (target != null) {
                        try {
                            sidebarIndex = Integer.parseInt(args[0]);
                        } catch (Exception e) {
                            // Then test if the sidebarName is valid
                            if (!sidebar.setSidebar(target, args[1])) {
                                message.sendToPlayer(player, "The sidebar " + args[1] + " was not found");
                            } else {
                                message.sendToPlayer(player, "Set the sidebar of " + args[0] + " to " + args[1]);
                            }
                            return true;
                        }
                        if (!config.isAllowAfkSet() && sidebarIndex == sidebar.getSidebarCount() - 1) {
                            message.sendToPlayer(player, "You cannot set" + args[0] + "'s sidebar to the AFK sidebar");
                        } else if (sidebarIndex > -1 && sidebarIndex < sidebar.getSidebarCount()) {
                            sidebar.setSidebar(target, sidebarIndex);
                        }
                        return true;
                    } else {
                        message.sendToPlayer(player, "The player " + args[0] + " was not found");
                    }
                } else {
                    message.sendToPlayer(player, "Too many arguments!");
                }
            } else {
                message.sendToPlayer(player, "You do not have the permission to use this command");
            }
        } else {
            // Server console
            if (args.length == 2) {
                // Player where the command will apply to
                Player target = Bukkit.getPlayer(args[0]);

                // Test first if target is valid
                if (target != null) {
                    int sidebarIndex = -2;

                    try {
                        sidebarIndex = Integer.parseInt(args[1]);
                    } catch (Exception e) {
                        if (!sidebar.setSidebar(target, args[1])) {
                            message.sendToConsole("The sidebar " + args[1] + " was not found");
                        } else {
                            message.sendToConsole("Set the sidebar of " + args[0] + " to " + args[1]);
                        }
                        return true;
                    }
                    if (!config.isAllowAfkSet() && sidebarIndex == sidebar.getSidebarCount() - 1) {
                        message.sendToConsole("You cannot set" + args[0] + "'s sidebar to the AFK sidebar");
                    } else if (sidebarIndex > -1 && sidebarIndex < sidebar.getSidebarCount()) {
                        sidebar.setSidebar(target, sidebarIndex);
                    }
                    return true;
                } else {
                    message.sendToConsole("The player " + args[0] + "was not found");
                }
            }
        }

        return false;
    }
}