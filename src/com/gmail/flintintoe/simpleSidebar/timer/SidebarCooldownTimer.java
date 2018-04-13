package com.gmail.flintintoe.simpleSidebar.timer;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.config.ConfigFile;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class SidebarCooldownTimer extends BukkitRunnable {

    /*
     * NOT IN USE
     * Functionality will now be taken over by SidebarUpdateManager class
     */

    private HashMap<String, Integer> cooldownPlayers = new HashMap<>();

    private SidebarManager sidebarM;

    private int duration;
    private String playerName;

    public SidebarCooldownTimer(SimpleSidebar plugin, Player player) {
        sidebarM = plugin.getSidebarManager();
        duration = plugin.getConfigManager().getValue(ConfigFile.config, "AFK_timer");

        playerName = player.getDisplayName();
        cooldownPlayers.put(playerName, duration);
    }

    @Override
    public void run() {
        if (cooldownPlayers.containsKey(playerName)) {
            // Player still part of cooldownPlayers
            int timeLeft = cooldownPlayers.get(playerName);

            if (timeLeft != 0) {
                cooldownPlayers.put(playerName, timeLeft - 1);

                sidebarM.updateSidebar(Bukkit.getPlayer(playerName));
            } else
                // End this timer
                try {
                    this.cancel();
                } catch (IllegalStateException e) {
                    // Do nothing
                }
        } else {
            // End this timer
            try {
                this.cancel();
            } catch (IllegalStateException e) {
                // Do nothing
            }
        }
    }

    public boolean reset(Player player) {
        cooldownPlayers.put(player.getDisplayName(), duration);

        return true;
    }

    public boolean stop(Player player) {
        cooldownPlayers.remove(player.getDisplayName());

        return true;
    }

    public boolean stopPlayerCooldown(String playerName) {
        cooldownPlayers.remove(playerName);

        return true;
    }

//    private static List<String> cooldownPlayers = new ArrayList<>();
//    private static List<String> resetPlayerList = new ArrayList<>();
//
//    private int seconds;
//    private String playerName;
//
//    public CooldownTimer(Player player, int seconds) {
//        this.seconds = seconds;
//        playerName = player.getDisplayName();
//
//        cooldownPlayers.add(playerName);
//    }
//
//    @Override
//    public void run() {
//        seconds--;
//
//        // This if is first so that this runnable ends when seconds reaches 0
//        if (seconds <= 0) {
//            // Remove player
//            cooldownPlayers.remove(playerName);
//
//            // End this timer
//            // Not merging with below through resetPlayerList.add(playerName) as it can cause duplicate playerNames
//            try {
//                this.cancel();
//            } catch (IllegalStateException ignore) {
//            }
//        }
//        if (!isPlayerOnReset(playerName)) {
//            cooldownPlayers.remove(playerName);
//            resetPlayerList.remove(playerName);
//            // End this timer
//            try {
//                this.cancel();
//            } catch (IllegalStateException ignore) {
//            }
//        }
//    }
//
//    // Special return: Returns whether the player is on cooldown
//    public static boolean isPlayerOnCooldown(Player player) {
//        String playerName = player.getDisplayName();
//
//        for (int i = 0; i < cooldownPlayers.size(); i++) {
//            if (playerName.equals(cooldownPlayers.get(i))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Special return: Returns whether the player is on cooldown
//    public static boolean isPlayerOnCooldown(String playerName) {
//        for (int i = 0; i < cooldownPlayers.size(); i++) {
//            if (playerName.equals(cooldownPlayers.get(i))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Special return: Returns whether the player is on cooldown
//    public static boolean isPlayerOnReset(Player player) {
//        String playerName = player.getDisplayName();
//
//        for (int i = 0; i < resetPlayerList.size(); i++) {
//            if (playerName.equals(resetPlayerList.get(i))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Special return: Returns whether the player is on cooldown
//    public static boolean isPlayerOnReset(String playerName) {
//        for (int i = 0; i < resetPlayerList.size(); i++) {
//            if (playerName.equals(resetPlayerList.get(i))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Special return: Returns whether reset was successful
//    public static boolean resetPlayer(Player player, int seconds) {
//        if (!CooldownTimer.isPlayerOnReset(player)) {
//            resetPlayerList.add(player.getDisplayName());
//
//            return true;
//        }
//        return false;
//    }
}
