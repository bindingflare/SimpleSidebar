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

public class PlaceholderManager {
    EconomyManager economyM;

    public PlaceholderManager(SimpleSidebar plugin) {
        economyM = plugin.getEconomyManager();
    }

    public String setPlaceholders(Player player, String string) {
        // Player name
        if (string.contains("%player%")) string = string.replaceAll("%player%", player.getName());

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

        // TODO Location of someone else
        // FIXME Possible logical error of duplicate lines

        // Server economyM (Player balance)
        if (string.contains("%balance%")) {
            string = string.replaceAll("%balance%", "" + economyM.getBalance(player));
        }

        // Region
        if (string.contains("%region_")) {
            String[] regions = getRegionList(player);

            // FIXME Possible issue where region placeholders will not AT LEAST be replaced with blank

            // Better way to handle variations of placeholder %region_x%
            for (int i = 0; i < regions.length; i++) {
                if (string.contains("%region_" + i + "%")) {
                    string = string.replaceAll("%region_" + i + "%", regions[i]);
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
            String tag = getVariableTag("%region_", string);

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

            String tag = getVariableTag("%stat_", string);

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

    // Special return: Returns the full variable tag using the tagStart (Start of tag, which is constant)
    private static String getVariableTag(String tagStart, String string) {
        return string.substring(string.indexOf(tagStart), string.replaceFirst("%", "").indexOf("%") - 1);
    }
}
