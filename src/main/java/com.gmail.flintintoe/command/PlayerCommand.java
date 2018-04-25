package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.message.MessageManager;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {
    private Sidebar sidebar;
    private MessageManager message;
    private Config config;

    public PlayerCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        message = plugin.getMessenger();
        config = plugin.getConfigMan();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            message.sendToConsole("Only a player can run this command");
            return true;
        } else {
            if (sender.hasPermission("simplesidebar.use")) {
                Player player = (Player) sender;

                // Help message
                if (args.length == 0) {
                    // TODO Maybe also have a help menu printed
                    message.sendToPlayer(player, "This command requires at least 1 argument");
                }
                // Set sidebar of self
                else if (args.length == 1) {
                    int sidebarIndex;

                    try {
                        sidebarIndex = Integer.parseInt(args[0]);
                    } catch (Exception e) {
                        message.sendToPlayer(player, "Argument must be a number");
                        return true;
                    }

                    if (!config.allowChangeAfk && sidebarIndex == sidebar.getSidebarCount() - 1) {
                        message.sendToPlayer(player, "You cannot set your sidebar to the AFK sidebar");
                        return true;
                    }

                    if (!sidebar.setSidebar(player, sidebarIndex)) {
                        message.sendToPlayer(player, sidebarIndex + " is not an registered sidebar index");
                        return true;
                    }
                }
                // Too many arguments
                else {
                    message.sendToPlayer(player, "Too many arguments!");
                    return true;
                }
            } else {
                message.sendToPlayer((Player) sender, "You do not have the permission to use this command");
            }
        }

        return false;
    }
}
