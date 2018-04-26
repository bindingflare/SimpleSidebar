package com.gmail.flintintoe.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Messenger {
    private ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public void sendToPlayer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&aSidebar&r] " + message));
    }

    public void sendToConsole(String message) {
        console.sendMessage("[SimpleSidebar] " + message);
    }
}
