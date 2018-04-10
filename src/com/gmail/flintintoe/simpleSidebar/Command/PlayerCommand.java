package com.gmail.flintintoe.simpleSidebar.Command;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {

    private SimpleSidebar plugin;

    public PlayerCommand(SimpleSidebar plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            SimpleSidebar.sendToConsole(" Only a player can run this command");
        }

        Player player = ((Player) sender).getPlayer();

        // Help message
        if (args.length == 0) {
            // TODO
        }
        // Set sidebar of self
        else if (args.length == 1) {
            String sidebarName = args[0];

            // TODO Set sidebar
        }
        // Too many arguments
        else {
            SimpleSidebar.sendToPlayer(player, "Too many arguments!");
            return true;
        }

        return false;
    }
}
