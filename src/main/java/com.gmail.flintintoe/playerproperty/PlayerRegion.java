package com.gmail.flintintoe.playerproperty;

import com.gmail.flintintoe.SimpleSidebar;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerRegion {
    private WorldGuardPlugin wGPlugin;

    public boolean setupWorldGuard(SimpleSidebar plugin) {
        Plugin wGPlugin = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(wGPlugin instanceof WorldGuardPlugin)) {
            return false;
        }

        this.wGPlugin = (WorldGuardPlugin) wGPlugin;
        plugin.getPgConfig().setRegionEnabled(true);
        return true;
    }

    public List<String> getRegionList(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();

        Set<ProtectedRegion> set = wGPlugin.getRegionManager(world).getApplicableRegions(location).getRegions();

        List<String> regions = new ArrayList<>();

        // Keeping all parent classes for now
        // This part could change (or another method be added)
        for (ProtectedRegion region : set) {
            regions.add(region.getId());
        }

        return regions;
    }
}
