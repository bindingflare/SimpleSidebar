package com.gmail.flintintoe.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Messenger {
    private ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public void send(String message) {
        console.sendMessage("[SimpleSidebar] " + message);
    }

    public void send(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(colorCode("[&aSidebar&r] " + message));
        } else {
            console.sendMessage("[SimpleSidebar] " + message);
        }
    }

    public void sendError(String errorString) {
        console.sendMessage("[SimpleSidebar] An error occurred: " + errorString);
    }

    public void sendError(CommandSender sender, String errorString) {
        if (sender instanceof Player) {
            sender.sendMessage(colorCode("[&aSidebar&r] An error occurred: " + errorString));
        } else {
            console.sendMessage("[SimpleSidebar] An error occurred: " + errorString);
        }
    }

    public void sendFatalError(CommandSender sender, String errorString) {
        if (sender instanceof Player) {
            sender.sendMessage(colorCode("[&aSidebar&r] An error occurred: " + errorString));
            sender.sendMessage(colorCode("[&aSidebar&r] Please contact your server administrator"));
        } else {
            console.sendMessage("[SimpleSidebar] An error occurred: " + errorString);
            console.sendMessage(colorCode("Use the link https://github.com/flintintoe/SimpleSidebar/issues if you believe this is an error"));
        }
    }

    private String colorCode(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
