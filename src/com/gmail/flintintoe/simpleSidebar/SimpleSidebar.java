package com.gmail.flintintoe.simpleSidebar;

import com.gmail.flintintoe.simpleSidebar.command.AdminCommand;
import com.gmail.flintintoe.simpleSidebar.command.PlayerCommand;
import com.gmail.flintintoe.simpleSidebar.config.ConfigFile;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.economy.ServerEconomy;
import com.gmail.flintintoe.simpleSidebar.event.PlayerEvent;
import com.gmail.flintintoe.simpleSidebar.sidebar.PlaceholderManager;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import com.gmail.flintintoe.simpleSidebar.timer.SidebarUpdateManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private MessageManager messageM;
    private ConfigManager configM;
    private SidebarManager sidebarM;
    private PlaceholderManager placeholderM;
    private ServerEconomy economy;
    private SidebarUpdateManager sbUpdateM;

    // TODO Use these booleans in case of lack of dependencies
    private boolean isEconomyEnabled = true;
    private boolean isRegionEnabled = true;
    private boolean isSidebarEnabled = true;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // Message
        messageM = new MessageManager(this);
        // Config
        configM = new ConfigManager(this);
        if (configM.getBoolean(ConfigFile.config, "plugin_enabled")) {
            // Economy
            economy = new ServerEconomy(this);

            if (!economy.setupEconomy()) {
                messageM.sendToConsole("Vault is not detected. Economy functions of this plugin will not work.");

                // Disable economy
                isEconomyEnabled = false;
            }
            // Permission
            setupPerms();
            // Sidebar
            // Placeholders is first
            placeholderM = new PlaceholderManager(this);
            sidebarM = new SidebarManager(this);
            sbUpdateM = new SidebarUpdateManager(this);
            sbUpdateM.runTaskTimer(this, 20L, 20L);

            // Command
            this.getCommand("sidebar").setExecutor(new PlayerCommand(this));
            this.getCommand("sidebarAdmin").setExecutor((new AdminCommand(this)));
            // Events
            pm.registerEvents(new PlayerEvent(this), this);
        } else {
            // Disable plugin
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        sbUpdateM.cancel();
    }

    private void setupPerms() {
        // TODO
    }

    public ConfigManager getConfigManager() {
        return configM;
    }

    public MessageManager getMessageManager() { return messageM; }

    public ServerEconomy getEconomy() {
        return economy;
    }

    public PlaceholderManager getPlaceholderManager() {
        return placeholderM;
    }

    public SidebarManager getSidebarManager() { return sidebarM; }

    public SidebarUpdateManager getSidebarUpdateManager() {
        return sbUpdateM;
    }

    public boolean economyEnabled() {
        return isEconomyEnabled;
    }


}
