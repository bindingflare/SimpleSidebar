package com.gmail.flintintoe.simpleSidebar.timer;

import com.gmail.flintintoe.simpleSidebar.MessageManager;
import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.config.ConfigFile;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class SidebarUpdateManager extends BukkitRunnable {

    private HashMap<String, Integer> playersOnCooldown = new HashMap<>();

    private SidebarManager sidebarM;
    private MessageManager messageM;
    private ConfigManager configM;

    private final int duration;

    public SidebarUpdateManager(SimpleSidebar plugin) {
        sidebarM = plugin.getSidebarManager();
        messageM = plugin.getMessageManager();
        configM = plugin.getConfigManager();

        duration = configM.getValue(ConfigFile.config, "AFKTimer");
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getDisplayName();

            if (playersOnCooldown.containsKey(playerName)) {
                // Need to either decrement duration left or change to AFK mode
                int timeLeft = playersOnCooldown.get(playerName);

                if (timeLeft > 0) {
                    // 1 duration has passed
                    playersOnCooldown.put(playerName, playersOnCooldown.get(playerName) - 1);
                } else {
                    // Set AFK
                    sidebarM.setAFKSidebar(player);
                    messageM.sendToConsole(playerName + " is now afk");
                }

            } else {
                // Log this an create
                sidebarM.setSidebar(player, 0);
                messageM.sendToConsole("Found " + playerName + " with no sidebar and was set the default sidebar");
            }
        }

    }

    public boolean setPlayerCooldown(Player player) {
        String playerName = player.getDisplayName();

        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.put(playerName, duration);
            return true;
        }

        return false;
    }

    public boolean removePlayerCooldown(Player player) {
        String playerName = player.getDisplayName();

        if (playersOnCooldown.containsKey(playerName)) {
            playersOnCooldown.remove(playerName);
            return true;
        }

        return false;
    }
}
