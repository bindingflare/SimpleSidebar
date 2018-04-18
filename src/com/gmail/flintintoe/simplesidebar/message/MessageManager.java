package com.gmail.flintintoe.simplesidebar.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MessageManager {
    private ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    private String consoleHeader = "[SimpleSidebar] ";
    private String header = "[&aSidebar&r] ";

    public void sendToPlayer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', header + message));
    }

    public void sendToConsole(String message) {
        console.sendMessage(consoleHeader + message);
    }
}
