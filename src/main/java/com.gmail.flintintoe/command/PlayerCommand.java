package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
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
    private Config config;

    public PlayerCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        message = plugin.getMessenger();
        config = plugin.getPgConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player && sender.hasPermission("simplesidebar.use")) {
            Player player = (Player) sender;

            if (args.length == 0) {
                // Get sidebar index of player
                int sidebarIndex = sidebar.getSidebarIndexOf(player);

                String name = sidebar.getSidebarName(sidebarIndex);
                String[] aliases = sidebar.getSidebarAliases(sidebarIndex);

                if (name != null) {
                    message.sendToPlayer(player, "You are using sidebar " + name + " (Index: " + sidebarIndex + ")");
                    message.sendToPlayer(player, "Aliases: " + Arrays.toString(aliases));
                    return true;
                } else {
                    message.sendToPlayer(player, "Error code AC-002-NAME_NOT_FOUND");
                }
            } else if (args.length == 1) {
                // Check using name, alias
                if (sidebar.setSidebar(player, args[0])) {
                    message.sendToConsole("Setting your sidebar to sidebar name or alias " + args[0]);
                }

                // Check using sidebar index
                int sidebarIndex;

                try {
                    sidebarIndex = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    message.sendToPlayer(player, "Error code PC-001-ARG_NOT_INTEGER");
                    return true;
                }

                if (!config.isAllowAfkSet() && sidebarIndex == sidebar.getSidebarCount() - 1) {
                    message.sendToPlayer(player, "Error code PC-002-AFK_SIDEBAR_ACCESS_DENIED");
                    message.sendToPlayer(player, "You cannot set your sidebar to the AFK sidebar");
                    return true;
                } else if (sidebarIndex >= 0 && sidebarIndex < sidebar.getSidebarCount()) {
                    message.sendToPlayer(player, "Setting your sidebar to " + sidebarIndex + "...");
                    sidebar.setSidebar(player, sidebarIndex);
                    return true;
                } else {
                    message.sendToPlayer(player, "Error code PC-003-INDEX_OUT_OF_BOUNDS");
                    return true;
                }
            }
        }

        return false;
    }
}
