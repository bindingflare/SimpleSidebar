package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.playerproperty.PlayerPermission;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
    private Messenger messenger;
    private Sidebar sidebar;
    private CommandOutput output;
    private PlayerPermission permission;

    public AdminCommand(SimpleSidebar plugin) {
        messenger = plugin.getMessenger();
        sidebar = plugin.getSidebar();
        output = plugin.getCommandOutput();
        permission = plugin.getPlayerPermission();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length == 0) {
            messenger.sendError(sender, "You need at least one argument for this command");
            return true;
        }

        if (args[0].equals("loadFiles") && permission.has(sender, "simplesidebaradmin.loadFiles")) {
            output.reloadPlugin(sender);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messenger.sendError(sender, "Could not find target player");
            return true;
        }

        if (args.length == 1) {
            if (permission.has(sender,"simplesidebaradmin.check")) {
                output.playerSidebarInfo(sender, target, sidebar.getSidebarIndexOf(target));
            } else {
                messenger.sendError(sender, "simplesidebaradmin.check permission required");
            }
            return true;

        } else if (args.length == 2) {
            if (permission.has(sender,"simplesidebaradmin.set")) {
                output.playerSetSidebar(sender, target, sidebar.querySidebarIndexOf(args[1]));
            } else {
                messenger.sendError(sender, "simplesidebaradmin.set permission required");
            }
            return true;

        } else {
            messenger.sendError(sender, "Too many arguments");
            return true;
        }
    }
}