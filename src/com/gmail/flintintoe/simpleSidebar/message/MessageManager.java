package com.gmail.flintintoe.simpleSidebar.message;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MessageManager {
    private ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    private String consoleHeader = "[SimpleSidebar] ";
    private String header = "[&aSidebar&r] ";

    public MessageManager (SimpleSidebar plugin) {
        // Nothing here for now
    }

    public void sendToPlayer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', header + message));
    }

    public void sendToConsole(String message) {
        console.sendMessage(consoleHeader + message);
    }
}
