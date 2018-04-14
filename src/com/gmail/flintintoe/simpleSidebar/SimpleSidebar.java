package com.gmail.flintintoe.simpleSidebar;

import com.gmail.flintintoe.simpleSidebar.command.AdminCommand;
import com.gmail.flintintoe.simpleSidebar.command.PlayerCommand;
import com.gmail.flintintoe.simpleSidebar.config.ConfigFile;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.economy.EconomyManager;
import com.gmail.flintintoe.simpleSidebar.event.PlayerEvent;
import com.gmail.flintintoe.simpleSidebar.sidebar.PlaceholderManager;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private MessageManager messageM;
    private ConfigManager configM;
    private SidebarManager sidebarM;
    private PlaceholderManager placeholderM;
    private EconomyManager economyM;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // PRIORITY 1
        messageM = new MessageManager(this);
        configM = new ConfigManager(this);

        // PRIORITY 2
        if (configM.getBoolean(ConfigFile.config, "plugin_enabled")) {
            economyM = new EconomyManager(this);
            // TODO Setup perms

            // PRIORITY 3
            // Placeholders is first
            placeholderM = new PlaceholderManager(this);
            sidebarM = new SidebarManager();
            // Commands
            this.getCommand("sidebar").setExecutor(new PlayerCommand(this));
            this.getCommand("sidebaradmin").setExecutor((new AdminCommand(this)));
            // Events
            pm.registerEvents(new PlayerEvent(this), this);
        } else {
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public ConfigManager getConfigManager() {
        return configM;
    }

    public MessageManager getMessageManager() {
        return messageM;
    }

    public EconomyManager getEconomyManager() {
        return economyM;
    }

    public PlaceholderManager getPlaceholderManager() {
        return placeholderM;
    }

    public SidebarManager getSidebarManager() {
        return sidebarM;
    }

//    public boolean economyEnabled() {
//        return isEconomyEnabled;
//    }
}
