package com.gmail.flintintoe.sidebar.placeholder;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * Deals with grabbing the statistics of the Spigot server.
 *
 * @since v0.8.0_pre1
 */
class Stat {
    private Messenger messageM;

    Stat(SimpleSidebar plugin) {
        messageM = plugin.getMessenger();
    }

    int getPlayerStat(Player player, String statName) {
        int result = -1;

        try {
            result = player.getStatistic(org.bukkit.Statistic.valueOf(statName));
        } catch (Exception e) {
            messageM.sendError("Stat required additional parameters or returned null. Parameters used: None");
        }
        return result;
    }

    int getPlayerStat(Player player, String statName, Material material) {
        int result = -1;

        try {
            result = player.getStatistic(org.bukkit.Statistic.valueOf(statName), material);
        } catch (Exception e) {
            messageM.sendError("Stat required additional parameters or returned null. Parameters used: Material");
        }
        return result;
    }

    int getPlayerStat(Player player, String statName, EntityType entityType) {
        int result = -1;

        try {
            result = player.getStatistic(org.bukkit.Statistic.valueOf(statName), entityType);
        } catch (Exception e) {
            messageM.sendError("Stat required additional parameters or returned null. Parameters used: Entity");
        }
        return result;
    }
}
