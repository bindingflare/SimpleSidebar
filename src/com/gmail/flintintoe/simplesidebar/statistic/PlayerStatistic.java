package com.gmail.flintintoe.simplesidebar.statistic;

import com.gmail.flintintoe.simplesidebar.SimpleSidebar;
import com.gmail.flintintoe.simplesidebar.message.MessageManager;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class PlayerStatistic {

    private MessageManager messageM;

    public PlayerStatistic (SimpleSidebar plugin) {
        messageM = plugin.getMessageManager();
    }

    public int getPlayerStat(Player player, String statName) {
        int result = -1;
        // TODO Need to check for material and entity in case of some statistics
        // For now, using try catch
        try {
            result = player.getStatistic(Statistic.valueOf(statName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
