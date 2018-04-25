package com.gmail.flintintoe;

import com.gmail.flintintoe.command.AdminCommand;
import com.gmail.flintintoe.command.PlayerCommand;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.event.PlayerEvent;
import com.gmail.flintintoe.message.MessageManager;
import com.gmail.flintintoe.placeholder.Placeholder;
import com.gmail.flintintoe.playerproperty.PlayerEconomy;
import com.gmail.flintintoe.playerproperty.PlayerRegion;
import com.gmail.flintintoe.playerproperty.PlayerStatistic;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private MessageManager messageM;
    private Config configM;
    private Sidebar sidebar;
    private Placeholder placeholder;
    private PlayerEconomy playerEco;
    private PlayerStatistic playerStat;
    private PlayerRegion playerRegion;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // PRIORITY 1
        messageM = new MessageManager();
        configM = new Config(this);

        // PRIORITY 2
        if (configM.getBoolean(ConfigFile.config, "plugin_enabled")) {
            // PlayerXXXX classes
            playerStat = new PlayerStatistic(this);

            playerEco = new PlayerEconomy();
            if (!playerEco.setupEconomy(this)) {
                messageM.sendToConsole("Vault dependency not found. Economy features will be disabled");
                configM.isEconomyEnabled = false;
            }

            playerRegion = new PlayerRegion();
            if (!playerRegion.setupWorldGuard(this)) {
                messageM.sendToConsole("WorldGaurd and WorldEdit dependency not found. Region features will be disabled");
                configM.isRegionEnabled = false;
            }

            // PRIORITY 3
            // Placeholders is first
            placeholder = new Placeholder(this);
            if (configM.afkTimer != 0) {
                placeholder.setupPholder(this);
            }

            sidebar = new Sidebar(this);
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
            sidebar.getGlobalUpdater().cancel();
        } else {
            sidebar.getCustomUpdater().cancel();
        }
    }

    public Config getConfigMan() {
        return configM;
    }

    public MessageManager getMessenger() {
        return messageM;
    }

    public PlayerEconomy getPlEconomy() {
        return playerEco;
    }

//    public Placeholder getPlaceholder() {
//        return placeholder;
//    }

    public Sidebar getSidebar() {
        return sidebar;
    }

    public PlayerStatistic getPlStatistic() {
        return playerStat;
    }

    public PlayerRegion getPlRegion() {
        return playerRegion;
    }
}