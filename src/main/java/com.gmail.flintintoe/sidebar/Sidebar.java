package com.gmail.flintintoe.sidebar;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.message.MessageManager;
import com.gmail.flintintoe.placeholder.Placeholder;
import com.gmail.flintintoe.timer.CustomSidebarUpdater;
import com.gmail.flintintoe.timer.SidebarUpdater;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class Sidebar {
    private Placeholder placeholder;

    private Config config;
    private MessageManager messenger;

    // BukkitRunnables
    private SidebarUpdater globalUpdater;
    private CustomSidebarUpdater customUpdater;

    // Sidebar number -> 0: name 1: header 2: entries
    private String[][] sidebars;
    private int sidebarCount = 0;

    public Sidebar(SimpleSidebar plugin) {
        placeholder = new Placeholder(plugin);

        config = plugin.getConfigMan();
        messenger = plugin.getMessenger();

        // Setup sidebar
        if (!setupSidebar(plugin)) {
            messenger.sendToConsole("Fatal: Sidebar module has failed to finish setup process");
            messenger.sendToConsole("Info: Disabling plugin...");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
        // Load sidebars
        if (!loadSidebars()) {
            messenger.sendToConsole("Fatal: Sidebar module has failed to load sidebars");
            messenger.sendToConsole("Info: Disabling plugin...");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        messenger.sendToConsole("Info: Sidebar module enabled");
    }

    private boolean setupSidebar(SimpleSidebar plugin) {
        // Use custom updater or the standard
        if (config.updatePlaceholderSync && config.afkTimer != 0) {
            globalUpdater = new SidebarUpdater(this);
            globalUpdater.runTaskTimer(plugin, 20L, config.updateTimer * 20);
        } else {
            customUpdater = new CustomSidebarUpdater(this, config.afkTimer, config.afkPlaceholderUpdate);
            customUpdater.runTaskTimer(plugin, 20L, config.updateTimer * 20);
        }
        return true;
    }

    private boolean loadSidebars() {

        // TODO

        return true;
    }

    public boolean setSidebar(Player player, String sidebarName) {

        // TODO

        return true;
    }

    public boolean setSidebar(Player player, int sidebarIndex) {
        // Return false if sidebarIndex out of bounds
        if (sidebarIndex < 0 || sidebarIndex > sidebarCount - 1) {
            return false;
        }

        // TODO

        return true;
    }

    public boolean setAFKSidebar(Player player) {

        // TODO

        return true;
    }

    public boolean updateSidebar(Player player) {

        // TODO

        return true;
    }

    public int getSidebarIndexOf(Player player) {
        int sidebarIndex;

        try {
            sidebarIndex = Integer.parseInt(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName());
        } catch (Exception e) {
            sidebarIndex = -1;
        }
        return sidebarIndex;
    }

    public CustomSidebarUpdater getCustomUpdater() {
        return customUpdater;
    }

    public SidebarUpdater getGlobalUpdater() {
        return globalUpdater;
    }

    public int getSidebarCount() {
        return sidebarCount;
    }
}
