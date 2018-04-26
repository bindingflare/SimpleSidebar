package com.gmail.flintintoe;

import com.gmail.flintintoe.command.AdminCommand;
import com.gmail.flintintoe.command.PlayerCommand;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.event.PlayerEvent;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.placeholder.Placeholder;
import com.gmail.flintintoe.playerproperty.PlayerEconomy;
import com.gmail.flintintoe.playerproperty.PlayerRegion;
import com.gmail.flintintoe.playerproperty.PlayerStatistic;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private Messenger messenger;
    private Config config;
    private Sidebar sidebar;
    private Placeholder ph;
    private PlayerEconomy pEconomy;
    private PlayerStatistic pStatistic;
    private PlayerRegion pRegion;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        // PRIORITY 1 //
        // Messenger
        messenger = new Messenger();

        // Config
        config = new Config(this);
        if (!config.setupConfig(this)) {
            messenger.sendToConsole("Disabling plugin...");
        }
        config.loadConfig();
        config.checkConfig(this);

        // PRIORITY 2 //
        // Statistic
        pStatistic = new PlayerStatistic(this);

        // Economy
        pEconomy = new PlayerEconomy();
        if (!pEconomy.setupEconomy(this)) {
            messenger.sendToConsole("Vault dependency not found. Economy features will be disabled");
        }

        // Region
        pRegion = new PlayerRegion();
        if (!pRegion.setupWorldGuard(this)) {
            messenger.sendToConsole("WorldGaurd and WorldEdit dependency not found. Region features will be disabled");
        }

        // PRIORITY 3 //
        // Placeholder
        ph = new Placeholder(this);

        // Sidebar
        sidebar = new Sidebar(this);
        sidebar.setupUpdater(this);
        sidebar.loadSidebars();

        // Placeholder Ext.
        if (config.getAfkTimer() != 0) {
            ph.setCustomUpd(this);
        }

        // Commands
        this.getCommand("sidebar").setExecutor(new PlayerCommand(this));
        this.getCommand("sidebaradmin").setExecutor((new AdminCommand(this)));

        // Events
        pm.registerEvents(new PlayerEvent(this), this);
    }

    @Override
    public void onDisable() {
        if (config.getAfkTimer() == 0) {
            sidebar.getGlobalUpdater().cancel();
        } else {
            sidebar.getCustomUpdater().cancel();
        }
    }

    public Config getPgConfig() {
        return config;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public PlayerEconomy getPlEconomy() {
        return pEconomy;
    }

    public Placeholder getPlaceholder() {
        return ph;
    }

    public Sidebar getSidebar() {
        return sidebar;
    }

    public PlayerStatistic getPlStatistic() {
        return pStatistic;
    }

    public PlayerRegion getPlRegion() {
        return pRegion;
    }
}