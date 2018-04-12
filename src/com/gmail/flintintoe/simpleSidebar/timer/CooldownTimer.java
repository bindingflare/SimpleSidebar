package com.gmail.flintintoe.simpleSidebar.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CooldownTimer extends BukkitRunnable {

    // FIXME Requires redo on storage structure
    // Using hashmap <String, Integer> where string is the playerName and Integer is the time left
    // Would work better with resets

    private static List<String> cooldownPlayers = new ArrayList<>();
    private static List<String> resetPlayerList = new ArrayList<>();

    private int seconds;
    private String playerName;

    public CooldownTimer(Player player, int seconds) {
        this.seconds = seconds;
        playerName = player.getDisplayName();

        cooldownPlayers.add(playerName);
    }

    @Override
    public void run() {
        seconds--;

        // This if is first so that this runnable ends when seconds reaches 0
        if (seconds <= 0) {
            // Remove player
            cooldownPlayers.remove(playerName);

            // End this timer
            // Not merging with below through resetPlayerList.add(playerName) as it can cause duplicate playerNames
            try {
                this.cancel();
            } catch (IllegalStateException ignore) {
            }
        }
        if (!isPlayerOnReset(playerName)) {
            cooldownPlayers.remove(playerName);
            resetPlayerList.remove(playerName);
            // End this timer
            try {
                this.cancel();
            } catch (IllegalStateException ignore) {
            }
        }
    }

    // Special return: Returns whether the player is on cooldown
    public static boolean isPlayerOnCooldown(Player player) {
        String playerName = player.getDisplayName();

        for (int i = 0; i < cooldownPlayers.size(); i++) {
            if (playerName.equals(cooldownPlayers.get(i))) {
                return true;
            }
        }
        return false;
    }

    // Special return: Returns whether the player is on cooldown
    public static boolean isPlayerOnCooldown(String playerName) {
        for (int i = 0; i < cooldownPlayers.size(); i++) {
            if (playerName.equals(cooldownPlayers.get(i))) {
                return true;
            }
        }
        return false;
    }

    // Special return: Returns whether the player is on cooldown
    public static boolean isPlayerOnReset(Player player) {
        String playerName = player.getDisplayName();

        for (int i = 0; i < resetPlayerList.size(); i++) {
            if (playerName.equals(resetPlayerList.get(i))) {
                return true;
            }
        }
        return false;
    }

    // Special return: Returns whether the player is on cooldown
    public static boolean isPlayerOnReset(String playerName) {
        for (int i = 0; i < resetPlayerList.size(); i++) {
            if (playerName.equals(resetPlayerList.get(i))) {
                return true;
            }
        }
        return false;
    }

    // Special return: Returns whether reset was successful
    public static boolean resetPlayer(Player player, int seconds) {
        if (!CooldownTimer.isPlayerOnReset(player)) {
            resetPlayerList.add(player.getDisplayName());

            return true;
        }
        return false;
    }
}
