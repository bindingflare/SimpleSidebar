package com.gmail.flintintoe.sidebar.tracker;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.sidebar.sidebars.Sidebars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * Tracks and updates the sidebars of online players in the Spigot server.
 *
 * @since v0.8.0_pre1
 */
public class Tracker extends BukkitRunnable {

    private HashMap<String, Integer> playerCooldowns = new HashMap<>();
    private HashMap<String, Integer> playerSidebarIndexes = new HashMap<>();

    private Config config;

    private Sidebars sidebars;

    private int ticks;
    private int ticksLeft;

    public Tracker(SimpleSidebar plugin) {
        config = plugin.getConfigManager();

        sidebars = plugin.getSidebarManager().getSidebars();

        ticksLeft = config.getUpdateTimer();
        ticks = config.getUpdateTimer();
    }

    @Override
    public void run() {
        for (String playerName : playerCooldowns.keySet()) {
            int timeLeft = playerCooldowns.get(playerName);

            if (ticksLeft == 0 && playerSidebarIndexes.get(playerName) != -1) {
                Player player = Bukkit.getPlayer(playerName);

                if (timeLeft > 0) {
                    sidebars.setSidebar(player, playerSidebarIndexes.get(playerName));
                } else if (timeLeft == 0) {
                    sidebars.setSidebar(player, sidebars.getSidebarCount() - 1);
                } else if (config.isAfkPhUpdate()) {
                    sidebars.setSidebar(player, sidebars.getSidebarCount() - 1);
                }
            }

            playerCooldowns.put(playerName, timeLeft - 1);
        }

        if (ticksLeft == 0) {
            ticksLeft = ticks;
        } else {
            ticksLeft--;
        }
    }

    public void add(String playerName) {
        if (config.isSetOnLogin()) {
            playerSidebarIndexes.put(playerName, -1);
        } else {
            playerSidebarIndexes.put(playerName, 0);
        }
        playerCooldowns.put(playerName, config.getAfkTimer());
    }

    public void set(String playerName, int sidebarIndex) {
        playerSidebarIndexes.put(playerName, sidebarIndex);
    }

    public void update(String playerName) {
        if (playerCooldowns.containsKey(playerName)) {
            playerCooldowns.put(playerName, config.getAfkTimer());
        }
    }

    public void remove(String playerName) {
        if (playerCooldowns.containsKey(playerName)) {
            playerCooldowns.remove(playerName);
            playerSidebarIndexes.remove(playerName);
        }
    }

    public boolean has(String playerName) {
        return playerCooldowns.containsKey(playerName);
    }

    public double getCooldown(String playerName) {
        if (playerCooldowns.containsKey(playerName)) {
            return playerCooldowns.get(playerName) / 10;
        }
        return -(config.getAfkTimer() / 10 + 1);
    }

    public int getIndex(String playerName) {
        if (playerSidebarIndexes.containsKey(playerName)) {
            return playerSidebarIndexes.get(playerName);
        }
        return -1;
    }
}