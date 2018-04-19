package com.gmail.flintintoe;

import com.gmail.flintintoe.command.AdminCommand;
import com.gmail.flintintoe.command.PlayerCommand;
import com.gmail.flintintoe.playerproperty.PlayerPermission;
import com.gmail.flintintoe.playerproperty.PlayerPlaceholder;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.config.ConfigManager;
import com.gmail.flintintoe.playerproperty.PlayerEconomy;
import com.gmail.flintintoe.event.EventManager;
import com.gmail.flintintoe.message.MessageManager;
import com.gmail.flintintoe.playerproperty.PlayerRegion;
import com.gmail.flintintoe.sidebar.SidebarManager;
import com.gmail.flintintoe.playerproperty.PlayerStatistic;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private MessageManager messageM;
    private ConfigManager configM;
    private SidebarManager sidebarM;
    private PlayerPlaceholder playerPholder;
    private PlayerEconomy playerEco;
    private PlayerStatistic playerStat;
    private PlayerRegion playerRegion;
    private PlayerPermission playerPerm;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // PRIORITY 1
        messageM = new MessageManager();
        configM = new ConfigManager(this);

        // PRIORITY 2
        if (configM.getBoolean(ConfigFile.config, "plugin_enabled")) {
            // PlayerXXXX classes
            playerPerm = new PlayerPermission(this);
            playerEco = new PlayerEconomy(this);
            playerStat = new PlayerStatistic(this);

            playerRegion = new PlayerRegion(this);
            playerRegion.setupWorldGuard();

            // PRIORITY 3
            // Placeholders is first
            playerPholder = new PlayerPlaceholder(this);
            sidebarM = new SidebarManager(this);
            // Commands
            this.getCommand("sidebar").setExecutor(new PlayerCommand(this));
            this.getCommand("sidebaradmin").setExecutor((new AdminCommand(this)));
            // Events
            pm.registerEvents(new EventManager(this), this);
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

    public PlayerEconomy getPlayerEconomy() {
        return playerEco;
    }

    public PlayerPlaceholder getPlayerPlaceholder() {
        return playerPholder;
    }

    public SidebarManager getSidebarManager() {
        return sidebarM;
    }

    public PlayerStatistic getPlayerStatistic() {
        return playerStat;
    }

    public PlayerRegion getPlayerRegion() {
        return playerRegion;
    }
}