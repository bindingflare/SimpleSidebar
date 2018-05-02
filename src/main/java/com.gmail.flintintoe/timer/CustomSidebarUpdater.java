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
    private int sbCount;

    private boolean afkSb;
    private boolean afkUpdate;

    public CustomSidebarUpdater(Sidebar sidebar, int interval, int sbCount, boolean afkUpdate) {
        this.sidebar = sidebar;
        this.interval = interval;
        afkSb = interval == 0;
        this.sbCount = sbCount;
        this.afkUpdate = afkUpdate;
    }

    @Override
    public void run() {
        for (String playerName : playersOnCooldown.keySet()) {
            int timeLeft = playersOnCooldown.get(playerName);

            Player player = Bukkit.getPlayer(playerName);

            if (timeLeft >= 0) {
                sidebar.setSidebar(player, playerSetSidebar.get(playerName));
            } else if (timeLeft == -1 && afkSb) {
                sidebar.setAFKSidebar(player);
            } else if (afkUpdate) {
                sidebar.updateSidebar(player);
            }

            playersOnCooldown.put(playerName, timeLeft - 1);
        }
    }

    public void set(Player player, int sidebarIndex) {
        String playerName = player.getDisplayName();

        if (!playersOnCooldown.containsKey(playerName) && sidebarIndex >= 0 && sidebarIndex < sbCount) {
            playersOnCooldown.put(playerName, interval);
            playerSetSidebar.put(playerName, sidebarIndex);
        }
    }

    public void remove(Player player) {
        String playerName = player.getDisplayName();

        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.remove(playerName);
            playerSetSidebar.remove(playerName);
        }
    }

    public void resetCooldown(Player player) {
        String playerName = player.getDisplayName();

        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, interval);
        }
    }

    public int getTime(Player player) {
        String playerName = player.getDisplayName();

        if (playersOnCooldown.containsKey(playerName)) {
            return playersOnCooldown.get(playerName);
        }
        return -(interval + 1);
    }

    public int getAfkTime(Player player) {
        String playerName = player.getDisplayName();

        if (playersOnCooldown.containsKey(playerName)) {
            return playersOnCooldown.get(playerName);
        }
        return interval + 1;
    }
}