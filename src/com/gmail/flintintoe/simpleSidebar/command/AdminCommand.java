package com.gmail.flintintoe.simpleSidebar.command;

import com.gmail.flintintoe.simpleSidebar.MessageManager;
import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
    private SidebarManager sidebarM;
    private MessageManager messageM;

    public AdminCommand(SimpleSidebar plugin) {
        sidebarM = plugin.getSidebarManager();
        messageM = plugin.getMessageManager();
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
                    if (!sidebarM.setSidebar(target, pageName)) {
                        messageM.sendToPlayer(player, "The page you requested to be set to " + target.getDisplayName() + " does not exist.");
                        return true;
                    } else {
                        messageM.sendToPlayer(player, "Set the sidebar of " + target.getDisplayName() + " to " + args[0] + ".");
                        return true;
                    }
                } else {
                    messageM.sendToPlayer(player, "The player " + args[1] + "does not exist.");
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
