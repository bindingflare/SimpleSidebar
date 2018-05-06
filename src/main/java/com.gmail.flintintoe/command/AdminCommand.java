package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AdminCommand implements CommandExecutor {
    private Messenger message;
    private Sidebar sidebar;
    private Config config;

    public AdminCommand(SimpleSidebar plugin) {
        message = plugin.getMessenger();
        sidebar = plugin.getSidebar();
        config = plugin.getPgConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        // Player
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                message.sendErrorMessage(player, "You need at least one argument for this command");
                return true;
            }

            if (args[0].equals("reload") && player.hasPermission("simplesidebaradmin.reload")) {
                message.sendToPlayer(player,"Reloading config...");
                config.reloadConfig();

                if (!config.getBoolean(ConfigFile.CONFIG, "plugin_enabled")) {
                    message.sendErrorMessage(player, "Cannot deactivate plugin through a config reload");
                }
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                message.sendErrorMessage(player, "Could not find target player");
                return true;
            }

            if (args.length == 1) {
                if (player.hasPermission("simplesidebaradmin.check")) {
                    int sidebarIndex = sidebar.getSidebarIndexOf(target);

                    if (sidebarIndex != -1) {
                        message.sendToPlayer(player, args[0] + "'s sidebar is " + sidebar.getSidebarName(sidebarIndex));
                        message.sendToPlayer(player, "  Index: " + (sidebarIndex + 1));
                        message.sendToPlayer(player, "  Aliases: " + Arrays.toString(sidebar.getSidebarAliases(sidebarIndex)));
                    } else {
                        message.sendErrorMessage(player, args[0] + " does not have a sidebar set");
                    }
                } else {
                    message.sendErrorMessage(player, "simplesidebaradmin.check permission required");
                }
                return true;

            } else if (args.length == 2) {
                if (player.hasPermission("simplesidebaradmin.set")) {
                    int returnCode = sidebar.querySidebar(target, args[1]);

                    if (returnCode == 0) {
                        message.sendToPlayer(player, "Set sidebar of " + args[0] + " to " + args[1]);
                        message.sendToPlayer(target, "Your sidebar has been changed to " + args[1]);
                    } else if (returnCode == 1) {
                        message.sendErrorMessage(player, "Either the sidebar with the query name does not exist, or the number is a negative number");
                    } else if (returnCode == 2) {
                        if (config.isAdminBypass()) {
                            message.sendToPlayer(player, "Set sidebar of " + args[0] + " to " + args[1] + " using admin bypass");
                            message.sendToPlayer(target, "Your sidebar has been changed to AFK sidebar " + args[1]);

                            sidebar.setAndUpdateSidebar(target, sidebar.getSidebarCount() - 1);
                        } else {
                            message.sendErrorMessage(player, "You do not have the permission to set " + args[0] + "'s sidebar to AFK sidebar " + args[1]);
                        }
                    } else if (returnCode == 3) {
                        message.sendErrorMessage(player, "Sidebar index is out of bounds");
                    } else if (returnCode == 4) {
                        message.sendErrorMessage(player, "Unexpected error");
                        message.sendToPlayer(player, "Please try again");
                    }
                } else {
                    message.sendErrorMessage(player, "simplesidebaradmin.set permission required");
                }
                return true;

            } else {
                message.sendErrorMessage(player, "Too many arguments");
                return true;
            }
        }
        // Console
        else {
            if (args.length == 0) {
                message.sendErrorMessage("You need at least one argument for this command");
                return true;
            }

            if (args[0].equals("reload")) {
                message.sendToConsole("Reloading config...");
                config.reloadConfig();

                if (!config.getBoolean(ConfigFile.CONFIG, "plugin_enabled")) {
                    message.sendErrorMessage("Cannot deactivate plugin through a config reload");
                }
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                message.sendErrorMessage("Could not find target player");
                return true;
            }

            if (args.length == 1) {
                int sidebarIndex = sidebar.getSidebarIndexOf(target);

                if (sidebarIndex != -1) {
                    message.sendToConsole(args[0] + "'s sidebar is " + sidebar.getSidebarName(sidebarIndex));
                    message.sendToConsole("  Index: " + (sidebarIndex + 1));
                    message.sendToConsole("  Aliases: " + Arrays.toString(sidebar.getSidebarAliases(sidebarIndex)));
                } else {
                    message.sendErrorMessage(args[0] + " does not have a sidebar set");
                }
                return true;

            } else if (args.length == 2) {
                int returnCode = sidebar.querySidebar(target, args[1]);

                if (returnCode == 0) {
                    message.sendToConsole("Set sidebar of " + args[0] + " to " + args[1]);
                    message.sendToPlayer(target, "Your sidebar has been changed to " + args[1]);
                } else if (returnCode == 1) {
                    message.sendToConsole("Either the sidebar with the query name does not exist, or the number is a negative number");
                } else if (returnCode == 2) {
                    if (config.isAdminBypass()) {
                        message.sendToConsole("Set sidebar of " + args[0] + " to " + args[1] + " using admin bypass");
                        message.sendToPlayer(target, "Your sidebar has been changed to AFK sidebar " + args[1]);

                        sidebar.setAndUpdateSidebar(target, sidebar.getSidebarCount() - 1);
                    } else {
                        message.sendErrorMessage("You do not have the permission to set " + args[0] + "'s sidebar to AFK sidebar" + args[1]);
                    }
                } else if (returnCode == 3) {
                    message.sendToConsole("Sidebar index is out of bounds");
                } else if (returnCode == 4) {
                    message.sendErrorMessage(target, "Unexpected error");
                    message.sendToConsole("Please try again");
                }
                return true;

            } else {
                message.sendErrorMessage("Too many arguments");
                return true;
            }
        }
    }
}