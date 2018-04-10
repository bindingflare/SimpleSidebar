package com.gmail.flintintoe.simpleSidebar.sidebar;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Placeholder {

    public static String setPlaceholders(Player player, String string) {
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

        // Region
        if (string.contains("%region")) {
            String[] regions = getRegionList(player);

            if (string.contains("%region_1%")) {
                if (regions.length > 0) {
                    string = string.replaceAll("%region_1%", regions[2]);
                } else {
                    // Replace with String of 20 spaces
                    string = string.replaceAll("%region_1%", "");
                }
            }
            if (string.contains("%region_2%")) {
                if (regions.length > 1) {
                    string = string.replaceAll("%region_2%", regions[2]);
                } else {
                    // Replace with String of 21 spaces
                    string = string.replaceAll("%region_2%", "");
                }
            }
            if (string.contains("%region_3%")) {
                if (regions.length > 2) {
                    string = string.replaceAll("%region_3%", regions[2]);
                } else {
                    // Replace with String of 22 spaces
                    string = string.replaceAll("%region_3%", "");
                }
            }
        }

        // TODO Regions of other locations

        // Player statistics
        if (string.contains("%stat_")) {

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
            while (string.contains("%stat_")) {
                String temp = string;

                int startIndex = temp.indexOf("%stat_") - 6;
                int endIndex = temp.replaceFirst("%", "").indexOf("%") - 1;

                String statName = string.substring(startIndex, endIndex);

                // Try to get that specific stat
                String statResult = "";
                try {
                    statResult += player.getStatistic(Statistic.valueOf(statName));
                } catch (Exception e) {
                    statResult += "(&4ERROR&r)";
                }
                // Replace raw placeholder
                string = string.replaceAll("%stat_" + statName + "%", "" + statResult);
            }
            //           }

            // More coming soon-ish
        }

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
