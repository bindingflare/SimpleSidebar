package com.gmail.flintintoe.simpleSidebar.timer;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LimitedSidebarUpdater extends BukkitRunnable {

    private HashMap<String, Integer> playersOnCooldown = new HashMap<>();

    private SidebarManager sidebarM;
    private ConfigManager configM;

    public LimitedSidebarUpdater(SimpleSidebar plugin) {
        configM = plugin.getConfigManager();
        sidebarM = plugin.getSidebarManager();
    }

    @Override
    // FIXME null pointer error in line 35
    public void run() {
        for (String playerName : playersOnCooldown.keySet()) {
            Player player = Bukkit.getPlayer(playerName);

            int timeLeft = playersOnCooldown.get(playerName);
            timeLeft--;

            playersOnCooldown.put(playerName, timeLeft);

            if (timeLeft > 0) {
                sidebarM.updateSidebar(player);
            } else if (timeLeft == 0) {
                sidebarM.setAFKSidebar(player);
            }
        }
    }

    public boolean add(String playerName, int duration) {
        if (!playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, duration);
            return true;
        }

        return false;
    }

    public boolean remove(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.remove(playerName);
            return true;
        }

        return false;
    }

    public boolean reset(String playerName) {
        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, configM.duration);
            return true;
        }

        return false;
    }
}