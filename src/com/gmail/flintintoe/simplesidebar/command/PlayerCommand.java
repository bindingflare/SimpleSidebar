package com.gmail.flintintoe.simplesidebar.command;

import com.gmail.flintintoe.simplesidebar.SimpleSidebar;
import com.gmail.flintintoe.simplesidebar.config.ConfigManager;
import com.gmail.flintintoe.simplesidebar.message.MessageManager;
import com.gmail.flintintoe.simplesidebar.sidebar.SidebarManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {
    private SidebarManager sidebarM;
    private MessageManager messageM;
    private ConfigManager configM;

    public PlayerCommand(SimpleSidebar plugin) {
        sidebarM = plugin.getSidebarManager();
        messageM = plugin.getMessageManager();
        configM = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            messageM.sendToConsole("Only a player can run this command");
            return true;
        } else {
            Player player = (Player) sender;

            // Help message
            if (args.length == 0) {
                // TODO
            }
            // Set sidebar of self
            else if (args.length == 1) {
                int sidebarIndex;

                try {
                    sidebarIndex = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    messageM.sendToPlayer(player, "Argument must be a number");
                    return true;
                }

                if (!configM.allowChangeAfk && sidebarIndex == sidebarM.getSidebarCount() - 1) {
                    messageM.sendToPlayer(player, "You cannot set your sidebar to the AFK sidebar");
                    return true;
                }

                if (!sidebarM.setSidebar(player, sidebarIndex)) {
                    messageM.sendToPlayer(player, sidebarIndex + " is not an registered sidebar index");
                    return true;
                }
            }
            // Too many arguments
            else {
                messageM.sendToPlayer(player, "Too many arguments!");
                return true;
            }
        }

        return false;
    }
}
