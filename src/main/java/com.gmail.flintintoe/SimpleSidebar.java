package com.gmail.flintintoe;

import com.gmail.flintintoe.command.AdminCommand;
import com.gmail.flintintoe.command.PlayerCommand;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.config.ConfigFile;
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
        // PRIORITY 1
        messenger = new Messenger();


        config = new Config(this);

        // PRIORITY 2
        if (config.getBoolean(ConfigFile.config, "plugin_enabled")) {
            // PlayerXXXX classes
            pStatistic = new PlayerStatistic(this);

            pEconomy = new PlayerEconomy();
            if (!pEconomy.setupEconomy(this)) {
                messenger.sendToConsole("Vault dependency not found. Economy features will be disabled");
            }

            pRegion = new PlayerRegion();
            if (!pRegion.setupWorldGuard(this)) {
                messenger.sendToConsole("WorldGaurd and WorldEdit dependency not found. Region features will be disabled");
            }

            // PRIORITY 3
            // Placeholders is first
            ph = new Placeholder(this);
            if (config.getAfkTimer() != 0) {
                ph.setCustomUpd(this);
            }

            sidebar = new Sidebar(this);

            // Sidebar
            sidebar.setupUpdater(this);
            if (!sidebar.loadSidebars()) {
                messenger.sendToConsole("Fatal: Sidebar module has failed to load sidebars");
                messenger.sendToConsole("Info: Disabling plugin...");
                this.getServer().getPluginManager().disablePlugin(this);
            }
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
        if (config.getAfkTimer() == 0) {
            sidebar.getGlobalUpdater().cancel();
        } else {
            sidebar.getCustomUpdater().cancel();
        }
    }

    public Config getConfigMan() {
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