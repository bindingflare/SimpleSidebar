package com.gmail.flintintoe.simpleSidebar.command;

import com.gmail.flintintoe.simpleSidebar.MessageManager;
import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {
    private SidebarManager sidebarM;
    private MessageManager messageM;

    public PlayerCommand(SimpleSidebar plugin) {
        sidebarM = plugin.getSidebarManager();
        messageM = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            messageM.sendToConsole(" Only a player can run this command");
        }

        Player player = ((Player) sender).getPlayer();

        // Help message
        if (args.length == 0) {
            // TODO
        }
        // Set sidebar of self
        else if (args.length == 1) {
            if(!sidebarM.setSidebar(player, args[0])) {

            }
        }
        // Too many arguments
        else {
            messageM.sendToPlayer(player, "Too many arguments!");
            return true;
        }

        return false;
    }
}
