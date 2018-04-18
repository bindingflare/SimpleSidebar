package com.gmail.flintintoe.simpleSidebar.sidebar;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.economy.EconomyManager;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
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

    public PlaceholderManager(SimpleSidebar plugin) {
        configM = plugin.getConfigManager();
        economyM = plugin.getEconomyManager();
        sidebarM = plugin.getSidebarManager();
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
        if (string.contains("%x_")) {
            while (string.contains("%x_")) {
                String propertyTag = getFirstPropertyTag(string);

                Player target = Bukkit.getPlayer(propertyTag);
                String replacement = "";

                if (target != null) {
                    replacement += player.getLocation().getBlockX();
                }
                string = string.replace("%x_" + propertyTag + "%", replacement);
            }
        }
        if (string.contains("%y_")) {
            while (string.contains("%y_")) {
                String propertyTag = getFirstPropertyTag(string);

                Player target = Bukkit.getPlayer(propertyTag);
                String replacement = "";

                if (target != null) {
                    replacement += player.getLocation().getBlockX();
                }
                string = string.replace("%y_" + propertyTag + "%", replacement);
            }
        }
        if (string.contains("%z_")) {
            while (string.contains("%z_")) {
                String propertyTag = getFirstPropertyTag(string);

                Player target = Bukkit.getPlayer(propertyTag);
                String replacement = "";

                if (target != null) {
                    replacement += player.getLocation().getBlockX();
                }
                string = string.replace("%z_" + propertyTag + "%", replacement);
            }
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
        if (string.contains("%balance_")) {
            while (string.contains("%balance_")) {
                String propertyTag = getFirstPropertyTag(string);

                Player target = Bukkit.getPlayer(propertyTag);

                String replacement = "";

                if (target != null) {
                    replacement += economyM.getBalance(target);
                }

                string.replace("%balance_" + propertyTag + "%", replacement);
            }
        }
        // Region
        if (string.contains("%region_")) {
            String[] regions = getRegionList(player);

            // Better way to handle variations of placeholder %region_x%
            for (int i = 0; i < regions.length; i++) {
                StringBuilder sb = new StringBuilder();

                sb.append("%region_");
                sb.append(i + 1);
                sb.append("%");

                if (string.contains("%region_" + (i + 1) + "%")) {
                    string = string.replaceAll(sb.toString(), regions[i]);
                }
            }
        }

        // Handle still incomplete %region_x% placeholders
        while (string.contains("%region_")) {
            // TODO Region list of other locations
            String tag = string.substring(string.indexOf("%region_"), string.replaceFirst("%", " ").indexOf("%") + 1);

            string = string.replaceAll(tag, "");
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
            String propertyTag = getFirstPropertyTag(string);

            String statResult = Statistic.valueOf(propertyTag).toString();
            String replacement = "";
        }

//            while (string.contains("stat_block_")) {
//                String propertyTag = getFirstPropertyTag(string);
//
//                String statResult = Statistic.valueOf( , propertyTag);
//            }

        // More coming soon-ish

        return string;
    }

    private String getFirstPropertyTag(String firstTag) {
        return firstTag.substring(firstTag.indexOf("%region_"), firstTag.replaceFirst("%", " ").indexOf("%") + 1);
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
