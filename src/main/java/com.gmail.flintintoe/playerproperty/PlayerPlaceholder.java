package com.gmail.flintintoe.playerproperty;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.ConfigManager;
import com.gmail.flintintoe.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class PlayerPlaceholder {
    private ConfigManager configM;
    private SidebarManager sidebarM;
    private PlayerEconomy playerEco;
    private PlayerStatistic playerStat;
    private PlayerRegion playerRegion;

    public PlayerPlaceholder(SimpleSidebar plugin) {
        configM = plugin.getConfigManager();
        playerEco = plugin.getPlayerEconomy();
        sidebarM = plugin.getSidebarManager();
        playerStat = plugin.getPlayerStatistic();
        playerRegion = plugin.getPlayerRegion();
    }

    public String setPlaceholders(Player player, String line) {
        String temp = line;

        // Player name
        if (temp.contains("%player%")) {
            temp = temp.replaceAll("%player%", player.getDisplayName());
        }

        // Player self location
        if (temp.contains("%x%")) {
            temp = temp.replaceAll("%x%", "" + player.getLocation().getBlockX());
        }
        if (temp.contains("%y%")) {
            temp = temp.replaceAll("%y%", "" + player.getLocation().getBlockY());
        }
        if (temp.contains("%z%")) {
            temp = temp.replaceAll("%z%", "" + player.getLocation().getBlockZ());
        }
        // Player location
        while (temp.contains("%x_")) {
            temp = setLocationPlaceholder("%x_", temp);
        }
        while (temp.contains("%y_")) {
            temp = setLocationPlaceholder("%y_", temp);
        }
        while (temp.contains("%z_")) {
            temp = setLocationPlaceholder("%z_", temp);
        }

        // Date and time
        while (temp.contains("%date_")) {
            String propertyTag = getPTag("%date_", temp);

            ZoneId currentZone = ZoneId.systemDefault();
            ZonedDateTime currentDateTime = ZonedDateTime.now(currentZone);

            DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
            DateTimeFormatter dateTimeFormatter = formatterBuilder.appendPattern(propertyTag).toFormatter();

            temp = temp.replace("%date%", currentDateTime.format(dateTimeFormatter));
        }
        while (temp.contains("%time_")) {
            ZoneId currentZone = ZoneId.systemDefault();
            ZonedDateTime currentDateTime = ZonedDateTime.now(currentZone);

            DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
            DateTimeFormatter dateTimeFormatter = formatterBuilder.appendPattern("need help").toFormatter();

            temp = temp.replace("%time%", currentDateTime.format(dateTimeFormatter));
        }

        // Player self balance
        if (temp.contains("%balance%")) {
            temp = temp.replaceAll("%balance%", "" + playerEco.getBalance(player));
        }
        // Player balance
        while (temp.contains("%balance_")) {
            temp = setPlayerPlaceholder("%balance_", temp, player);
        }

        // Region
        if (temp.contains("%region_")) {
            temp = setRegionPlaceholders(temp, player);
        }
        // Other player's region
        if (temp.contains("%regionof_")) {
            String propertyTag = getPTag("%regionof_", temp);

            Player target = Bukkit.getPlayer(propertyTag);

            temp = temp.replaceAll("%regionof_" + propertyTag + "_", "%region_");

            if (target != null) {
                setRegionPlaceholders(temp, target);
            } else {
                temp = temp.replaceAll("%region_" + getPTag("%region_", temp) + "%", "");
            }
        }

        // Afk duration
        if (configM.afkTimer != 0) {
            if (temp.contains("%afk_time%")) {
                temp = temp.replaceAll("%afk_time%", "" + sidebarM.getCustomUpdater().getAfkTime(player.getDisplayName()));
            }

            if (temp.contains("%afk_timeLeft%")) {
                temp = temp.replaceAll("%afk_timeLeft%", "" + sidebarM.getCustomUpdater().getTime(player.getDisplayName()));
            }

        }

        // Player statistics

        while (temp.contains("%stat_")) {
            // TODO Fix statistic placeholder
            temp = setPlayerPlaceholder("%stat_%", temp, player);
        }

        // More coming soon-ish
        return temp;
    }

    private String getPTag(String tagHeader, String string) {
        return string.substring(string.indexOf(tagHeader), string.replaceFirst("%", " ").indexOf("%") + 1);
    }

    private String getMiddlePTag(String tagHeader, String string) {
        return string.substring(string.indexOf(tagHeader), string.replaceFirst("_", " ").indexOf("_") + 1);
    }

    private String setLocationPlaceholder(String tagHeader, String string) {
        String propertyTag = getPTag(tagHeader, string);

        Player target = Bukkit.getPlayer(propertyTag);
        String replacement = "";

        if (target != null) {
            if ("%x_".equals(tagHeader)) {
                replacement += target.getLocation().getBlockX();
            } else if ("%y_".equals(tagHeader)) {
                replacement += target.getLocation().getBlockY();
            } else if ("%z_".equals(tagHeader)) {
                replacement += target.getLocation().getBlockZ();
            }
        }
        return string.replace(tagHeader + propertyTag + "%", replacement);
    }

    private String setPlayerPlaceholder(String tagHeader, String string, Player player) {
        String propertyTag = getPTag(tagHeader, string);

        String replacement = "";

        if ("%balance_".equals(tagHeader)) {
            replacement += playerEco.getBalance(player);
        } else if ("%stat_".equals(tagHeader)) {
            replacement += playerStat.getPlayerStat(player, propertyTag);
        }

        return string.replace(tagHeader + propertyTag + "%", replacement);
    }

    private String setRegionPlaceholders(String string, Player player) {
        String[] regions = playerRegion.getRegionList(player);
        String temp = string;

        // Better way to handle variations of placeholder %region_x%

        for (int i = 0; i < regions.length; i++) {
            StringBuilder sb = new StringBuilder();

            sb.append("%region_");
            sb.append(i + 1);
            sb.append("%");

            if (temp.contains("%region_" + (i + 1) + "%")) {
                temp = temp.replaceAll(sb.toString(), regions[i]);
            }
        }

        // Handle still incomplete %region_x% placeholders
        while (temp.contains("%region_")) {
            String propertyTag = getPTag("%region_", temp);

            temp = temp.replaceAll("%region_" + propertyTag + "%", "");
        }

        return temp;
    }


}
