package com.gmail.flintintoe.sidebar.placeholder;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Deals with grabbing region information using the location of a player.
 *
 * @since v0.8.0_pre1
 */
class Region {
    private WorldGuardPlugin wGPlugin;

    private boolean isEnabled = false;

    Region(WorldGuardPlugin wGPlugin) {
        this.wGPlugin = wGPlugin;

        if (wGPlugin != null) {
            isEnabled = true;
        }
    }

    List<String> getRegionList(Player player) {
        List<String> regions = new ArrayList<>();

        if (isEnabled) {
            World world = player.getWorld();
            Location location = player.getLocation();

            Set<ProtectedRegion> set = wGPlugin.getRegionManager(world).getApplicableRegions(location).getRegions();

            // Keeping all parent classes for now
            // This part could change (or another method be added)
            for (ProtectedRegion region : set) {
                regions.add(region.getId());
            }
        }

        return regions;
    }
}
