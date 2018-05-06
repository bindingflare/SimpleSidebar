package com.gmail.flintintoe.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Messenger {
    private ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public void sendToPlayer(Player player, String message) {
        player.sendMessage(colorCode("[&aSidebar&r] " + message));
    }

    public void sendToConsole(String message) {
        console.sendMessage("[SimpleSidebar] " + message);
    }

    public void sendErrorMessage(Player player, String errorCode) {
        player.sendMessage(colorCode("[&aSidebar&r] An error occurred: " + errorCode));
    }

    public void sendErrorMessage(String errorCode) {
        console.sendMessage("[SimpleSidebar] An error occurred: " + errorCode);
    }

    public void sendFatalErrorMessage(Player player, String errorCode) {
        player.sendMessage(colorCode("[&aSidebar&r] An error occurred: " + errorCode));
        player.sendMessage(colorCode("[&aSidebar&r] Please contact your server administrator"));
    }

    public void sendFatalErrorMessage(String errorCode) {
        console.sendMessage("[SimpleSidebar] An error occurred: " + errorCode);
        console.sendMessage(colorCode("Use the [link](https://github.com/flintintoe/SimpleSidebar/issues) if you believe this is an error"));
    }

    private String colorCode(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
