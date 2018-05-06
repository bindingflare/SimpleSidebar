package com.gmail.flintintoe.timer;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class SidebarRunnable extends BukkitRunnable {

    private HashMap<String, Integer> playersOnCooldown = new HashMap<>();
    private HashMap<String, Integer> playerSetSidebar = new HashMap<>();

    private Sidebar sidebar;
    private Config config;

    private int interval;

    public SidebarRunnable(SimpleSidebar plugin) {
        config = plugin.getPgConfig();
        interval = config.getUpdateTimer();
    }

    public void setSidebarObject(Sidebar sidebar) {
        this.sidebar = sidebar;
    }

    @Override
    public void run() {
        if (interval == 0) {
            for (String playerName : playersOnCooldown.keySet()) {
                int timeLeft = playersOnCooldown.get(playerName);

                if (!(playerSetSidebar.get(playerName) == -1)) {
                    Player player = Bukkit.getPlayer(playerName);

                    if (timeLeft >= 0) {
                        sidebar.setSidebar(player, playerSetSidebar.get(playerName));
                    } else if (timeLeft == -1) {
                        sidebar.setSidebar(player, sidebar.getSidebarCount() - 1);
                    } else if (config.isAfkPhUpdate()) {
                        sidebar.setSidebar(player, sidebar.getSidebarCount() - 1);
                    }
                }

                playersOnCooldown.put(playerName, timeLeft - config.getUpdateTimer());
            }

            interval = config.getUpdateTimer();
        }

        interval--;
    }

    public void set(String playerName, int sidebarIndex, int timeLeft) {
        playersOnCooldown.put(playerName, timeLeft);
        playerSetSidebar.put(playerName, sidebarIndex);
    }

    public void updateSidebarIndex(String playerName, int sidebarIndex) {
        if (playerSetSidebar.containsKey(playerName)) {
            playerSetSidebar.put(playerName, sidebarIndex);
        }
    }

    public void updateSidebarTime(String playerName, int timeLeft) {
        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, timeLeft);
        }
    }

    public void remove(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.remove(playerName);
            playerSetSidebar.remove(playerName);
        }
    }

    public int getTime(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            return playersOnCooldown.get(playerName);
        }
        return -(config.getAfkTimer() + 1);
    }

    public int getAfkTime(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            return playersOnCooldown.get(playerName);
        }
        return config.getAfkTimer() + 1;
    }
}