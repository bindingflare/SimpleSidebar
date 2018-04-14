package com.gmail.flintintoe.simpleSidebar.sidebar;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.economy.EconomyManager;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class PlaceholderManager {
    EconomyManager economyM;

    public PlaceholderManager(SimpleSidebar plugin) {
        economyM = plugin.getEconomyManager();
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
            string = string.replace("%z%", "" + player.getLocation().getBlockZ());
        }
        // Date and time
        // TODO Fix formatting of date and time
        ZonedDateTime currentTime = ZonedDateTime.now();
        ZoneId currentZone = ZoneId.systemDefault();

        if (string.contains("%year%")) {
            string = string.replaceAll("%year%", "" + currentTime.getYear());
        }
        if (string.contains("%month%")) {
            string = string.replaceAll("%month%", "" + currentTime.getMonth());
        }
        if (string.contains("%day%")) {
            string = string.replaceAll("%day%", "" + currentTime.getDayOfMonth());
        }
        if (string.contains("%hour%")) {
            string = string.replaceAll("%hour%", "" + currentTime.getHour());
        }
        if (string.contains("%minute%")) {
            string = string.replaceAll("%minute%", "" + currentTime.getMinute());
        }
        if (string.contains("%second%")) {
            string = string.replaceAll("%second%", "" + currentTime.getSecond());
        }
        if (string.contains("%timezone%")) {
            string = string.replaceAll("%timezone%", "" + currentZone);
        }

        // TODO Location of someone else
        // FIXME Possible logical error of duplicate lines

        // Player balance
        if (string.contains("%balance%")) {
            string = string.replaceAll("%balance%", "" + economyM.getBalance(player));
        }

        // Region
        if (string.contains("%region_")) {
            String[] regions = getRegionList(player);

            // FIXME Possible issue where region placeholders will not AT LEAST be replaced with blank

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
//            if (string.contains("%region_1%")) {
//                if (regions.length > 0) {
//                    string = string.replaceAll("%region_1%", regions[0]);
//                } else {
//                    // Replace with String of 20 spaces
//                    string = string.replaceAll("%region_1%", "");
//                }
//            }
//            if (string.contains("%region_2%")) {
//                if (regions.length > 1) {
//                    string = string.replaceAll("%region_2%", regions[1]);
//                } else {
//                    // Replace with String of 21 spaces
//                    string = string.replaceAll("%region_2%", "");
//                }
//            }
//            if (string.contains("%region_3%")) {
//                if (regions.length > 2) {
//                    string = string.replaceAll("%region_3%", regions[2]);
//                } else {
//                    // Replace with String of 22 spaces
//                    string = string.replaceAll("%region_3%", "");
//                }
//            }
        }

        // Handle still incomplete %region_x% placeholders
        while (string.contains("%region_")) {
            String tag = string.substring(string.indexOf("%region_"), string.replaceFirst("%", " ").indexOf("%") + 1);

            string = string.replaceAll(tag, "");
        }


        // TODO Regions of other locations

        // Player statistics

        // TODO Redo this part of code (For now keeping the bottom code alive might work)

        // MINE_BLOCK
//            if (string.contains("%stat_MINE_BLOCK_")) {
//                while (string.contains("%stat_MINE_BLOCK_")) {
//                    String materialName = "" + string.subSequence(string.indexOf("%stat_MINE_BLOCK_") - 17, string.replaceFirst("%", "").indexOf("%") - 1);
//
//                    // Get the material to check stats with
//                    Material material = Material.getMaterial(materialName);
//                    String statResult = "";
//
//                    try {
//                        statResult += player.getStatistic(Statistic.MINE_BLOCK, material);
//                    } catch (Exception e) {
//                        statResult += "(&4ERROR&r)";
//                    }
//                    // Replace raw placeholder
//                    string.replaceAll("%stat_MINE_BLOCK_" + materialName + "%",
//                            "" + statResult);
//                }
//            } else {
        // Basic handler for any other stat
        // FIXME Possible error where the colour would be reset after &r(&4ERROR&r) due to &r
        while (string.contains("%stat_")) {
            String temp = string;

            String tag = string.substring(string.indexOf("%stat_"), string.replaceFirst("%", " ").indexOf("%") + 1);

            // Try to get that specific stat
            String statResult = "";
            try {
                statResult += player.getStatistic(Statistic.valueOf(tag));
            } catch (Exception e) {
                statResult += "&r(&4ERROR&r)";
            }
            // Replace raw placeholder
            string = string.replaceAll("%" + tag + "%", "" + statResult);
        }
        //           }

        // More coming soon-ish


        return string;
    }

    private static String[] getRegionList(Player player) {

        World world = player.getWorld();
        Location location = player.getLocation();

        ApplicableRegionSet set = WGBukkit.getRegionManager(world).getApplicableRegions(location);

        String[] regions = new String[set.size()];

        // Keeping all parent classes for now
        // This part could change (or another method be added
        int count = 0;
        for (ProtectedRegion region : set) {

            regions[count] = region.getId();
            count++;
        }

        return regions;
    }
}
