package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {
    private Sidebar sidebar;
    private Messenger message;
    private Config config;

    public PlayerCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        message = plugin.getMessenger();
        config = plugin.getPgConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            message.sendToConsole("Only a player can run this command");
        } else {
            Player player = (Player) sender;

            if (sender.hasPermission("simplesidebar.use")) {
                // Help message
                if (args.length == 0) {
                    // TODO Maybe also have a help menu printed
                    message.sendToPlayer(player, "This command requires at least 1 argument");
                }
                // Set sidebar of self
                else if (args.length == 1) {
                    // -2 so that when sidebar count is 0 and sidebarIndex is -1... that thing does not happen
                    int sidebarIndex = -2;

                    try {
                        sidebarIndex = Integer.parseInt(args[0]);
                    } catch (Exception e) {
                        if (!sidebar.setSidebar(player, args[0])) {
                            message.sendToPlayer(player, "Sidebar with name " + args[0] + " could not be found");
                        }
                        return true;
                    }
                    if (!config.isAllowAfkSet() && sidebarIndex == sidebar.getSidebarCount() - 1) {
                        message.sendToPlayer(player, "You cannot set your sidebar to the AFK sidebar");
                    } else if (sidebarIndex > -1 && sidebarIndex < sidebar.getSidebarCount()) {
                        sidebar.setSidebar(player, sidebarIndex);
                    }
                    return true;
                } else {
                    message.sendToPlayer(player, "Too many arguments!");
                }
            } else {
                message.sendToPlayer(player, "You do not have the permission to use this command");
            }
        }

        return false;
    }
}
