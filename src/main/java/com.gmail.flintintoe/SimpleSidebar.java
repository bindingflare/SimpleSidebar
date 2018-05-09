package com.gmail.flintintoe;

import com.gmail.flintintoe.command.AdminCommand;
import com.gmail.flintintoe.command.CommandOutput;
import com.gmail.flintintoe.command.PlayerCommand;
import com.gmail.flintintoe.config.PluginConfig;
import com.gmail.flintintoe.event.PlayerEvent;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.placeholder.Placeholder;
import com.gmail.flintintoe.playerproperty.PlayerEconomy;
import com.gmail.flintintoe.playerproperty.PlayerRegion;
import com.gmail.flintintoe.playerproperty.PlayerStatistic;
import com.gmail.flintintoe.sidebar.Sidebar;
import com.gmail.flintintoe.timer.SidebarRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSidebar extends JavaPlugin {

    private Messenger messenger;
    private PluginConfig config;
    private Sidebar sidebar;
    private SidebarRunnable runnable;
    private Placeholder ph;
    private PlayerEconomy pEconomy;
    private PlayerStatistic pStatistic;
    private PlayerRegion pRegion;
    private CommandOutput output;

    @Override
    public void onEnable() {
        // PRIORITY 1 //
        // Messenger
        messenger = new Messenger();

        // PluginConfig
        config = new PluginConfig(this);
        if (!config.setupConfig(this)) {
            messenger.send("Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        config.loadConfig();
        config.checkConfig(this);

        // PRIORITY 2 //
        // Statistic
        pStatistic = new PlayerStatistic(this);

        // Economy
        pEconomy = new PlayerEconomy();
        if (!pEconomy.setupEconomy(this)) {
            messenger.send("Vault dependency not found. Economy features will be disabled");
        }

        // Region
        pRegion = new PlayerRegion();
        if (!pRegion.setupWorldGuard(this)) {
            messenger.send("WorldGuard and WorldEdit dependency not found. Region features will be disabled");
        }

        // PRIORITY 3 //
        // Runnable
        runnable = new SidebarRunnable(this);
        runnable.runTaskTimer(this, 20L, 20L);

        // Placeholder
        ph = new Placeholder(this);

        // Sidebar
        sidebar = new Sidebar(this);
        sidebar.loadSidebars();

        // Set sidebar variable of SidebarRunnable after creating Sidebar object
        runnable.setSidebarObject(sidebar);

        // Commands
        getCommand("sidebar").setExecutor(new PlayerCommand(this));
        getCommand("sidebaradmin").setExecutor((new AdminCommand(this)));
        // Command output
        output = new CommandOutput(this);

        // Events
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
    }

    @Override
    public void onDisable() {
        runnable.cancel();
    }

    public PluginConfig getPluginConfig() {
        return config;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public PlayerEconomy getPlayerEconomy() {
        return pEconomy;
    }

    public SidebarRunnable getRunnable() {
        return runnable;
    }

    public Placeholder getPlaceholder() {
        return ph;
    }

    public Sidebar getSidebar() {
        return sidebar;
    }

    public PlayerStatistic getPlayerStatistic() {
        return pStatistic;
    }

    public PlayerRegion getPlayerRegion() {
        return pRegion;
    }

    public CommandOutput getCommandOutput() {
        return output;
    }
}