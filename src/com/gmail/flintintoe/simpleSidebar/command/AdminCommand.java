package com.gmail.flintintoe.simpleSidebar.command;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
    private SimpleSidebar plugin;

    public AdminCommand(SimpleSidebar plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        // Player
        if (sender instanceof Player) {
            if (args.length == 2) {
                // Player who sent the command
                Player player = ((Player) sender).getPlayer();
                // Player where the command will apply to
                Player target = Bukkit.getPlayer(args[1]);

                // Test first if target is valid
                if (target != null) {
                    String pageName = args[1];
                    // Then test if the sidebarName is valid
                    if (!plugin.sidebar.setSidebar(target, pageName)) {
                        SimpleSidebar.sendToPlayer(player, "The page you requested to be set to " + target.getDisplayName() + " does not exist.");
                        return true;
                    } else {
                        SimpleSidebar.sendToPlayer(player, "Set the sidebar of " + target.getDisplayName() + " to " + args[0] + ".");
                        return true;
                    }
                } else {
                    SimpleSidebar.sendToPlayer(player, "The player " + args[1] + "does not exist.");
                    return true;
                }
            }
        }
        // Server console
        else {
            // Nothing here
        }
        return false;
    }
}
