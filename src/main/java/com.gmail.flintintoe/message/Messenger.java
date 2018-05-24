package com.gmail.flintintoe.message;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.config.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Manages the messages sent by this plugin.
 *
 * @since v0.8.0_pre1
 */
public class Messenger {
    private SimpleSidebar plugin;
    private ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    private String[] messages;

    private final String HEADER = "[&aSidebar&r]";
    private final String CONSOLE_HEADER = "[SimpleSidebar]";

    public Messenger(SimpleSidebar plugin) {
        this.plugin = plugin;
    }

    public void loadMessages() {
        Config config = plugin.getConfigManager();

        messages = getListAsArray("messages");
    }

    private String[] getListAsArray(String path) {
        List<String> list = plugin.getConfigManager().getStrings(ConfigFile.MESSAGES, path);
        String[] arr = new String[list.size()];

        return list.toArray(arr);
    }

    public void send(String message) {
        console.sendMessage(CONSOLE_HEADER + " " + message);
    }

    public void send(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(colorCode(HEADER + " " + message));
        } else {
            console.sendMessage(CONSOLE_HEADER + " " + message);
        }
    }

    public void send(CommandSender sender, int index) {
        send(sender, messages[index]);
    }

    public void sendError(String errorString) {
        console.sendMessage(CONSOLE_HEADER + " An error occurred: " + errorString);
    }

    public void sendError(CommandSender sender, String errorString) {
        if (sender instanceof Player) {
            sender.sendMessage(colorCode(HEADER + " An error occurred: " + errorString));
        } else {
            console.sendMessage(CONSOLE_HEADER + "An error occurred: " + errorString);
        }
    }

    public void sendFatalError(String errorString) {
        console.sendMessage(CONSOLE_HEADER + "A fatal error occurred: " + errorString);
        console.sendMessage(colorCode("Use the link https://github.com/flintintoe/SimpleSidebar/issues if you believe this is an error"));
    }

    public void sendFatalError(CommandSender sender, String errorString) {
        if (sender instanceof Player) {
            sender.sendMessage(colorCode(HEADER + " A fatal error occurred: " + errorString));
            sender.sendMessage(colorCode(HEADER + " Please contact your server administrator"));
        } else {
            console.sendMessage(CONSOLE_HEADER + " An error occurred: " + errorString);
            console.sendMessage(colorCode("Use the link https://github.com/flintintoe/SimpleSidebar/issues if you " +
                    "believe this is an error/ bug"));
        }
    }

    private String colorCode(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
