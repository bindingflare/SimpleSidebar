package com.gmail.flintintoe.simplesidebar;

import com.gmail.flintintoe.simplesidebar.command.AdminCommand;
import com.gmail.flintintoe.simplesidebar.command.PlayerCommand;
import com.gmail.flintintoe.simplesidebar.config.ConfigFile;
import com.gmail.flintintoe.simplesidebar.config.ConfigManager;
import com.gmail.flintintoe.simplesidebar.economy.EconomyManager;
import com.gmail.flintintoe.simplesidebar.event.PlayerEvent;
import com.gmail.flintintoe.simplesidebar.message.MessageManager;
import com.gmail.flintintoe.simplesidebar.sidebar.PlaceholderManager;
import com.gmail.flintintoe.simplesidebar.sidebar.SidebarManager;
import com.gmail.flintintoe.simplesidebar.statistic.PlayerStatistic;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private MessageManager messageM;
    private ConfigManager configM;
    private SidebarManager sidebarM;
    private PlaceholderManager placeholderM;
    private EconomyManager economyM;
    private PlayerStatistic playerStat;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // PRIORITY 1
        messageM = new MessageManager();
        configM = new ConfigManager(this);
        playerStat = new PlayerStatistic(this);

        // PRIORITY 2
        if (configM.getBoolean(ConfigFile.config, "plugin_enabled")) {
            economyM = new EconomyManager(this);
            // TODO Setup perms

            // PRIORITY 3
            // Placeholders is first
            placeholderM = new PlaceholderManager(this);
            sidebarM = new SidebarManager(this);
            // Commands
            this.getCommand("sidebar").setExecutor(new PlayerCommand(this));
            this.getCommand("sidebaradmin").setExecutor((new AdminCommand(this)));
            // Events
            pm.registerEvents(new PlayerEvent(this), this);
        } else {
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (configM.afkTimer == 0) {
            sidebarM.getGlobalUpdater().cancel();
        } else {
            sidebarM.getCustomUpdater().cancel();
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

    public PlayerStatistic getStatManager() {
        return playerStat;
    }
}