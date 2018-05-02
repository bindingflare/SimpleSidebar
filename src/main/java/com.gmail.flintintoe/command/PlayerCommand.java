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
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (sender.hasPermission("simplesidebar.use")) {
                if (args.length == 1) {
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
        }

        return false;
    }
}
