package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.MessageManager;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
    private Sidebar sidebar;
    private MessageManager message;

    public AdminCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        message = plugin.getMessenger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        // Player
        if (sender instanceof Player) {
            if (sender.hasPermission("simplesidebar.admin")) {
                if (args.length == 2) {
                    // Player who sent the command
                    Player player = (Player) sender;
                    // Player where the command will apply to
                    Player target = Bukkit.getPlayer(args[0]);

                    // Test first if target is valid
                    if (target != null) {
                        int sidebarIndex;

                        try {
                            sidebarIndex = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            message.sendToPlayer(player, "Argument must be a number");
                            return true;
                        }
                        // Then test if the sidebarName is valid
                        if (!sidebar.setSidebar(target, sidebarIndex)) {
                            message.sendToPlayer(player, "The page you requested to be set to " + target.getDisplayName() + " failed.");
                            return true;
                        } else {
                            message.sendToPlayer(player, "Set the sidebar of " + target.getDisplayName() + " to " + args[0] + ".");
                            return true;
                        }
                    } else {
                        message.sendToPlayer(player, "The player " + args[1] + "does not exist.");
                        return true;
                    }
                } else {
                    message.sendToPlayer((Player) sender, "You do not have the permission to use this command");
                }
            }
        } else {
            // Server console
            if (args.length == 2) {
                // Player where the command will apply to
                Player target = Bukkit.getPlayer(args[0]);

                // Test first if target is valid
                if (target != null) {
                    int sidebarIndex;

                    try {
                        sidebarIndex = Integer.parseInt(args[1]);
                    } catch (Exception e) {
                        message.sendToConsole("Argument must be a number");
                        return true;
                    }
                    // Then test if the sidebarName is valid
                    if (!sidebar.setSidebar(target, sidebarIndex)) {
                        message.sendToConsole("The page you requested to be set to " + target.getDisplayName() + " failed.");
                        return true;
                    } else {
                        message.sendToConsole("Set the sidebar of " + target.getDisplayName() + " to " + args[0] + ".");
                        return true;
                    }
                } else {
                    message.sendToConsole("The player " + args[1] + "does not exist.");
                    return true;
                }
            }
        }

        return false;
    }
}