package com.gmail.flintintoe.playerproperty;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerStatistic {

    private Messenger messageM;

    public PlayerStatistic(SimpleSidebar plugin) {
        messageM = plugin.getMessenger();
    }

    public int getPlayerStat(Player player, String statName) {
        int result = -1;

        try {
            result = player.getStatistic(Statistic.valueOf(statName));
        } catch (Exception e) {
            messageM.sendToConsole("Error code PS-001-INVALID_PARAMETERS");
            messageM.sendToConsole("Stat required additional parameters or returned null. Parameters used: None");
        }
        return result;
    }

    public int getPlayerStat(Player player, String statName, Material material) {
        int result = -1;

        try {
            result = player.getStatistic(Statistic.valueOf(statName), material);
        } catch (Exception e) {
            messageM.sendToConsole("Error code PS-002-INVALID_PARAMETERS");
            messageM.sendToConsole("Stat required additional parameters or returned null. Parameters used: Material");
        }
        return result;
    }

    public int getPlayerStat(Player player, String statName, EntityType entityType) {
        int result = -1;

        try {
            result = player.getStatistic(Statistic.valueOf(statName), entityType);
        } catch (Exception e) {
            messageM.sendToConsole("Error code PS-002-INVALID_PARAMETERS");
            messageM.sendToConsole("Stat required additional parameters or returned null. Parameters used: Entity");
        }
        return result;
    }
}
