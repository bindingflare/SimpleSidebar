package com.gmail.flintintoe.timer;

import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CustomSidebarUpdater extends BukkitRunnable {

    private HashMap<String, Integer> playersOnCooldown = new HashMap<>();
    private HashMap<String, Integer> playerSetSidebar = new HashMap<>();

    private Sidebar sidebar;

    private int interval;
    private boolean afkSb;
    private boolean afkUpdate;

    public CustomSidebarUpdater(Sidebar sidebar, int interval, boolean afkUpdate) {
        this.sidebar = sidebar;
        this.interval = interval;
        afkSb = interval == 0;
        this.afkUpdate = afkUpdate;
    }

    @Override
    public void run() {
        for (String playerName : playersOnCooldown.keySet()) {
            Player player = Bukkit.getPlayer(playerName);

            int timeLeft = playersOnCooldown.get(playerName);

            if (timeLeft >= 0) {
                sidebar.setSidebar(player, playerSetSidebar.get(playerName));
            } else if (timeLeft == -1 && afkSb) {
                sidebar.setAFKSidebar(player);
            } else if (timeLeft < -1 && afkUpdate) {
                sidebar.updateSidebar(player);
            }

            playersOnCooldown.put(playerName, timeLeft - 1);
        }
    }

    public boolean set(String playerName) {
        if (!playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, interval);
            playerSetSidebar.put(playerName, sidebar.getSidebarIndexOf(Bukkit.getPlayer(playerName)));
            // TODO need checker for above method that can return -1
            return true;
        }

        return false;
    }

    public boolean remove(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.remove(playerName);
            playerSetSidebar.remove(playerName);
            return true;
        }

        return false;
    }

    public boolean resetCooldown(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, interval);
            return true;
        }

        return false;
    }

    public int getTime(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            return playersOnCooldown.get(playerName);
        }
        return -(interval + 1);
    }

    public int getAfkTime(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            return playersOnCooldown.get(playerName);
        }
        return interval + 1;
    }
}