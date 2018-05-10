package com.gmail.flintintoe;

import com.gmail.flintintoe.command.AdminCommand;
import com.gmail.flintintoe.command.CommandOutput;
import com.gmail.flintintoe.command.PlayerCommand;
import com.gmail.flintintoe.config.PluginConfig;
import com.gmail.flintintoe.event.PlayerEvent;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.placeholder.Placeholder;
import com.gmail.flintintoe.placeholder.RemotePlaceholder;
import com.gmail.flintintoe.playerproperty.PlayerEconomy;
import com.gmail.flintintoe.playerproperty.PlayerPermission;
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
    private SidebarRunnable sRunnable;

    private Placeholder placeholder;
    private RemotePlaceholder rPlaceholder;

    private PlayerEconomy pEconomy;
    private PlayerStatistic pStatistic;
    private PlayerRegion pRegion;
    private PlayerPermission pPermission;

    private CommandOutput output;

    @Override
    public void onEnable() {
        // Creating instances of sub-classes
        messenger = new Messenger();
        config = new PluginConfig(this);

        pStatistic = new PlayerStatistic(this);
        pEconomy = new PlayerEconomy();
        pPermission = new PlayerPermission();
        pRegion = new PlayerRegion();

        sRunnable = new SidebarRunnable(this);
        placeholder = new Placeholder(this);
        rPlaceholder = new RemotePlaceholder(this);
        sidebar = new Sidebar(this);
        output = new CommandOutput(this);

        // Setting up sub-classes
        // messenger requires no setup
        if (!config.setup(this)) {
            messenger.send("Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        config.checkConfig(this);
        // pStatistic requires no setup
        // pEconomy, pPermission requires vault
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            messenger.send("Vault dependency not found. Economy and permissions features will be disabled");

            if (!pEconomy.setup(this)) {
                messenger.sendError("Economy plugin not found");
            }
            if (!pPermission.setup(this)) {
                messenger.sendError("Permissions plugin not found");
            }
        }
        if (!pRegion.setupWorldGuard(this)) {
            messenger.send("WorldGuard and WorldEdit dependency not found. Region features will be disabled");
        }
        sRunnable.runTaskTimer(this, 20L, 20L);
        placeholder.setup(this);
        rPlaceholder.setup(this);
        sidebar.loadSidebars();
        sRunnable.setSidebarObject(sidebar);
        // output requires no setup

        // Commands
        getCommand("sidebar").setExecutor(new PlayerCommand(this));
        getCommand("sidebaradmin").setExecutor((new AdminCommand(this)));

        // Events
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
    }

    @Override
    public void onDisable() {
        sRunnable.cancel();
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

    public PlayerPermission getPlayerPermission() {
        return pPermission;
    }

    public SidebarRunnable getsRunnable() {
        return sRunnable;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
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

    public RemotePlaceholder getRemotePlaceholder() {
        return rPlaceholder;
    }
}