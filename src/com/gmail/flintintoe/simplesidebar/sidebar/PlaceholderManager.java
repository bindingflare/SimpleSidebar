package com.gmail.flintintoe.simplesidebar.sidebar;

import com.gmail.flintintoe.simplesidebar.SimpleSidebar;
import com.gmail.flintintoe.simplesidebar.config.ConfigManager;
import com.gmail.flintintoe.simplesidebar.economy.EconomyManager;
import com.gmail.flintintoe.simplesidebar.statistic.PlayerStatistic;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class PlaceholderManager {
    private ConfigManager configM;
    private EconomyManager economyM;
    private SidebarManager sidebarM;
    private PlayerStatistic playerStat;

    public PlaceholderManager(SimpleSidebar plugin) {
        configM = plugin.getConfigManager();
        economyM = plugin.getEconomyManager();
        sidebarM = plugin.getSidebarManager();
        playerStat = plugin.getStatManager();
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
            ZoneId currentZone = ZoneId.systemDefault();
            ZonedDateTime currentDateTime = ZonedDateTime.now(currentZone);

            DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
            DateTimeFormatter dateTimeFormatter = formatterBuilder.appendPattern("dd MM yyyy").toFormatter();

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
            string = string.replaceAll("%balance%", "" + economyM.getBalance(player));
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
        if (string.contains("%afk_time%")) {
            if (configM.afkTimer != 0) {
                string = string.replaceAll("%afk_time%", "" + (-sidebarM.getCustomUpdater().getTime(player.getDisplayName())));
            } else {
                string = string.replaceAll("%afk_time%", "");
            }
        }
        if (string.contains("%afk_timeLeft%")) {
            if (configM.afkTimer != 0) {
                string = string.replaceAll("%afk_timeLeft%", "" + (sidebarM.getCustomUpdater().getTime(player.getDisplayName())));
            } else {
                string = string.replaceAll("%afk_timeLeft%", "");
            }
        }

        // Player statistics

        while (string.contains("%stat_")) {
            string = setPlayerPlaceholder("%stat_%", string, player);
        }

//            while (string.contains("stat_block_")) {
//                String propertyTag = getPTag(string);
//
//                String statResult = Statistic.valueOf( , propertyTag);
//            }

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
        String propertyTag = getPTag(tagHeader, string);

        String replacement = "";

        if ("%balance_".equals(tagHeader)) {
            replacement += economyM.getBalance(player);
        } else if ("%stat_".equals(tagHeader)) {
            replacement += playerStat.getPlayerStat(player, propertyTag);
        }

        return string.replace(tagHeader + propertyTag + "%", replacement);
    }

    private String setRegionPlaceholders(String string, Player player) {
        String[] regions = getRegionList(player);
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

    private String[] getRegionList(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();

        ApplicableRegionSet set = WGBukkit.getRegionManager(world).getApplicableRegions(location);

        String[] regions = new String[set.size()];

        // Keeping all parent classes for now
        // This part could change (or another method be added)
        int count = 0;
        for (ProtectedRegion region : set) {
            regions[count] = region.getId();
            count++;
        }

        return regions;
    }
}
