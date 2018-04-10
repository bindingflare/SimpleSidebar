package com.gmail.flintintoe.simpleSidebar;

import com.gmail.flintintoe.simpleSidebar.Command.AdminCommand;
import com.gmail.flintintoe.simpleSidebar.Command.PlayerCommand;
import com.gmail.flintintoe.simpleSidebar.economy.ServerEconomy;
import com.gmail.flintintoe.simpleSidebar.event.PlayerEvent;
import com.gmail.flintintoe.simpleSidebar.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public Sidebar sidebar;

    private static String consoleHeader = "[SimpleSidebar] ";
    private static String header = "[&aSidebar&r] ";

    private static boolean isEconomy = true;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // Economy
        if (ServerEconomy.setupEconomy(this)) {
            console.sendMessage(header + "Vault is not detected. Economy functions of this plugin will not work.");

            // Disable economy
            isEconomy = false;
        }
        // Permissions
        setupPerms();
        // Config
        Configuration.setupConfig();
        //  Sidebars
        setupSidebar();
        // Commands
        this.getCommand("sidebar").setExecutor(new PlayerCommand(this));
        this.getCommand("sidebarAdmin").setExecutor((new AdminCommand(this)));
        // Events
        pm.registerEvents(new PlayerEvent(this), this);


    }

    @Override
    public void onDisable() {

    }

    private void setupPerms() {
        // TODO
    }

    private void setupSidebar() {
        // TODO
    }

    public static boolean economyEnabled() {
        return isEconomy;
    }

    public static void sendToPlayer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', header + message));
    }

    public static void sendToConsole(String message) {
        console.sendMessage(consoleHeader + message);
    }


}
