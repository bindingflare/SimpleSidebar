package com.gmail.flintintoe.simpleSidebar.timer;

import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LimitedSidebarUpdater extends BukkitRunnable {

    private HashMap<String, Integer> playersOnCooldown = new HashMap<>();
    private HashMap<String, Integer> playerSetSidebar = new HashMap<>();

    private SidebarManager sidebarM;

    private int defaultDuration;
    private boolean afkSb;

    public LimitedSidebarUpdater(SidebarManager sidebarM, int defaultDuration, boolean afkSb) {
        this.sidebarM = sidebarM;
        this.defaultDuration = defaultDuration;
        this.afkSb = afkSb;
    }

    @Override
    public void run() {
        for (String playerName : playersOnCooldown.keySet()) {
            Player player = Bukkit.getPlayer(playerName);

            int timeLeft = playersOnCooldown.get(playerName);

            if (timeLeft >= 0) {
                sidebarM.setSidebar(player, playerSetSidebar.get(playerName));
            } else if (timeLeft == -1) {
                sidebarM.setAFKSidebar(player);
            } else if (timeLeft < -1) {
                sidebarM.updateSidebar(player);
            }

            playersOnCooldown.put(playerName, timeLeft - 1);
        }
    }

    public boolean set(String playerName, int duration) {
        if (!playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, duration);
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
            playersOnCooldown.put(playerName, defaultDuration);
            return true;
        }

        return false;
    }

    public int getTime(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            return playersOnCooldown.get(playerName);
        }

        return -(defaultDuration + 1);
    }
}