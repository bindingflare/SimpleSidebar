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

    public String setPlaceholders(Player player, String string) {
        // Player name
        if (string.contains("%player%")) {
            string = string.replaceAll("%player%", player.getDisplayName());
        }

        // Player self location
        if (string.contains("%x%")) {
            string = string.replaceAll("%x%", "" + player.getLocation().getBlockX());
        }
        if (string.contains("%y%")) {
            string = string.replaceAll("%y%", "" + player.getLocation().getBlockY());
        }
        if (string.contains("%z%")) {
            string = string.replaceAll("%z%", "" + player.getLocation().getBlockZ());
        }
        // Player location
        while (string.contains("%x_")) {
            string = setLocationPlaceholder("%x_", string);
        }
        while (string.contains("%y_")) {
            string = setLocationPlaceholder("%y_", string);
        }
        while (string.contains("%z_")) {
            string = setLocationPlaceholder("%z_", string);
        }

        // Date and time
        if (string.contains("%date%")) {
            var currentZone = ZoneId.systemDefault();
            var currentDateTime = ZonedDateTime.now(currentZone);

            var formatterBuilder = new DateTimeFormatterBuilder();
            var dateTimeFormatter = formatterBuilder.appendPattern("dd MM yyyy").toFormatter();

            string = string.replaceAll("%date%", currentDateTime.format(dateTimeFormatter));
        }
        if (string.contains("%time%")) {
            ZoneId currentZone = ZoneId.systemDefault();
            ZonedDateTime currentDateTime = ZonedDateTime.now(currentZone);

            DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
            DateTimeFormatter dateTimeFormatter = formatterBuilder.appendPattern("need help").toFormatter();

            string = string.replaceAll("%time%", currentDateTime.format(dateTimeFormatter));
        }

        // Player specific date and time
        if (string.contains("%date_")) {
            // TODO Custom date format
        }
        if (string.contains("%time_")) {
            // TODO Custom time format
        }

        // Player self balance
        if (string.contains("%balance%")) {
            string = string.replaceAll("%balance%", "" + playerEco.getBalance(player));
        }
        // Player balance
        while (string.contains("%balance_")) {
            string = setPlayerPlaceholder("%balance_", string, player);
        }

        // Region
        if (string.contains("%region_")) {
            string = setRegionPlaceholders(string, player);
        }
        // Other player's region
        if (string.contains("%regionof_")) {
            String propertyTag = getPTag("%regionof_", string);

            Player target = Bukkit.getPlayer(propertyTag);

            string = string.replaceAll("%regionof_" + propertyTag + "_", "%region_");

            if (target != null) {
                setRegionPlaceholders(string, target);
            } else {
                string = string.replaceAll("%region_" + getPTag("%region_", string) + "%", "");
            }
        }

        // Afk duration
        if (configM.afkTimer != 0) {
            if (string.contains("%afk_time%")) {
                string = string.replaceAll("%afk_time%", "" + sidebarM.getCustomUpdater().getAfkTime(player.getDisplayName()));
            }

            if (string.contains("%afk_timeLeft%")) {
                string = string.replaceAll("%afk_timeLeft%", "" + sidebarM.getCustomUpdater().getTime(player.getDisplayName()));
            }

        }

        // Player statistics

        while (string.contains("%stat_")) {
            // TODO Fix statistic placeholder
            string = setPlayerPlaceholder("%stat_%", string, player);
        }

        // More coming soon-ish
        return string;
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
        var propertyTag = getPTag(tagHeader, string);

        String replacement = "";

        if ("%balance_".equals(tagHeader)) {
            replacement += playerEco.getBalance(player);
        } else if ("%stat_".equals(tagHeader)) {
            replacement += playerStat.getPlayerStat(player, propertyTag);
        }

        return string.replace(tagHeader + propertyTag + "%", replacement);
    }

    private String setRegionPlaceholders(String string, Player player) {
        var regions = playerRegion.getRegionList(player);
        var temp = string;

        // Better way to handle variations of placeholder %region_x%

        for (int i = 0; i < regions.length; i++) {
            var sb = new StringBuilder();

            sb.append("%region_");
            sb.append(i + 1);
            sb.append("%");

            if (temp.contains("%region_" + (i + 1) + "%")) {
                temp = temp.replaceAll(sb.toString(), regions[i]);
            }
        }

        // Handle still incomplete %region_x% placeholders
        while (temp.contains("%region_")) {
            var propertyTag = getPTag("%region_", temp);

            temp = temp.replaceAll("%region_" + propertyTag + "%", "");
        }

        return temp;
    }


}
