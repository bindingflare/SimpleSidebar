package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PlayerCommand implements CommandExecutor {
    private Sidebar sidebar;
    private Messenger message;

    public PlayerCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        message = plugin.getMessenger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                if (player.hasPermission("simplesidebar.see")) {
                    int sidebarIndex = sidebar.getSidebarIndexOf(player);

                    if (sidebarIndex != -1) {
                        message.sendToPlayer(player, "Your sidebar is " + sidebar.getSidebarName(sidebarIndex));
                        message.sendToPlayer(player, "  Index: " + (sidebarIndex + 1));
                        message.sendToPlayer(player, "  Aliases: " + Arrays.toString(sidebar.getSidebarAliases(sidebarIndex)));
                    } else {
                        message.sendErrorMessage(player, "You do not have a sidebar set");
                    }
                } else {
                    message.sendErrorMessage(player, "simplesidebar.see permission required");
                }
                return true;

            } else if (args.length == 1) {
                if (player.hasPermission("simplesidebar.use")) {
                    int returnCode = sidebar.querySidebar(player, args[0]);

                    if (returnCode == 0) {
                        message.sendToPlayer(player, "Set your sidebar to " + args[0]);
                    } else if (returnCode == 1) {
                        message.sendErrorMessage(player, "Sidebar with the query name does not exist, or the number is a negative number");
                    } else if (returnCode == 2) {
                        message.sendErrorMessage(player, "You do not have the permission to set your sidebar to AFK sidebar " + args[0]);
                    } else if (returnCode == 3) {
                        message.sendErrorMessage(player, "Sidebar index is out of bounds");
                    } else if (returnCode == 4) {
                        message.sendErrorMessage(player, "Unexpected error");
                        message.sendToPlayer(player, "Please try again");
                    }
                } else {
                    message.sendErrorMessage(player, "simplesidebar.see permission required");
                }
                return true;

            } else {
                message.sendErrorMessage(player, "Too many arguments");
                return true;
            }
        } else {
            message.sendErrorMessage("This command is only for players");
            return true;
        }
    }
}
