package com.gmail.flintintoe.simpleSidebar;

import com.gmail.flintintoe.simpleSidebar.command.AdminCommand;
import com.gmail.flintintoe.simpleSidebar.command.PlayerCommand;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.economy.ServerEconomy;
import com.gmail.flintintoe.simpleSidebar.event.PlayerEvent;
import com.gmail.flintintoe.simpleSidebar.sidebar.PlaceholderManager;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private MessageManager messageM;
    private ConfigManager configM;
    private SidebarManager sidebarM;
    private PlaceholderManager placeholderManager;
    private ServerEconomy economy;

    private boolean isEconomyEnabled = true;
    private boolean isRegionEnabled = true;
    private boolean isSidebarEnabled = true;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // Messaging
        messageM = new MessageManager(this);
        // Economy
        economy = new ServerEconomy(this);

        if (!economy.setupEconomy()) {
            messageM.sendToConsole("Vault is not detected. Economy functions of this plugin will not work.");

            // Disable economy
            isEconomyEnabled = false;
        }
        // Permissions
        setupPerms();
        // Config
        configM = new ConfigManager(this);
        //  Sidebars
        sidebarM = new SidebarManager(this);
        // Commands
        this.getCommand("sidebar").setExecutor(new PlayerCommand(this));
        this.getCommand("sidebarAdmin").setExecutor((new AdminCommand(this)));
        // Events
        pm.registerEvents(new PlayerEvent(this), this);


    }

    @Override
    public void onDisable() {
        // Nothing to do here yet
    }

    private void setupPerms() {
        // TODO
    }

    public MessageManager getMessageManager() { return messageM; }

    public ConfigManager getConfigManager() { return configM; }

    public SidebarManager getSidebarManager() { return sidebarM; }

    public PlaceholderManager getPlaceholderManager() { return placeholderManager; }

    public ServerEconomy getEconomy() { return economy; }

    public boolean economyEnabled() {
        return isEconomyEnabled;
    }


}
