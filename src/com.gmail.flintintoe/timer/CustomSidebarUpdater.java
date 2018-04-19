package com.gmail.flintintoe.timer;

import com.gmail.flintintoe.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CustomSidebarUpdater extends BukkitRunnable {

    private HashMap<String, Integer> playersOnCooldown = new HashMap<>();
    private HashMap<String, Integer> playerSetSidebar = new HashMap<>();

    private SidebarManager sidebarM;

    private int interval;
    private boolean afkSb;
    private boolean afkUpdate;

    public CustomSidebarUpdater(SidebarManager sidebarM, int interval, boolean afkUpdate) {
        this.sidebarM = sidebarM;
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
                sidebarM.setSidebar(player, playerSetSidebar.get(playerName));
            } else if (timeLeft == -1 && afkSb) {
                    sidebarM.setAFKSidebar(player);
            } else if (timeLeft < -1 && afkUpdate) {
                sidebarM.updateSidebar(player);
            }

            playersOnCooldown.put(playerName, timeLeft - 1);
        }
    }

    public boolean set(String playerName) {
        if (!playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, interval);
            playerSetSidebar.put(playerName, sidebarM.getSidebarIndexOf(Bukkit.getPlayer(playerName)));
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