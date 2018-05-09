package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {
    private Sidebar sidebar;
    private Messenger messenger;
    private CommandOutput output;

    public PlayerCommand(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        messenger = plugin.getMessenger();
        output = plugin.getCommandOutput();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {

            if (args.length == 0) {
                if (sender.hasPermission("simplesidebar.see")) {
                    output.playerSidebarInfo(sender, sidebar.getSidebarIndexOf((Player) sender));
                } else {
                    messenger.sendError(sender, "simplesidebar.see permission required");
                }
                return true;

            } else if (args.length == 1) {
                if (sender.hasPermission("simplesidebar.use")) {
                    output.playerSetSidebar(sender, sidebar.getSidebarIndexOf(((Player) sender), args[0]));
                } else {
                    messenger.sendError(sender, "simplesidebar.see permission required");
                }
                return true;

            } else {
                messenger.sendError(sender, "Too many arguments");
                return true;
            }
        } else {
            messenger.sendError(sender, "This command is only for players");
            return true;
        }
    }
}
