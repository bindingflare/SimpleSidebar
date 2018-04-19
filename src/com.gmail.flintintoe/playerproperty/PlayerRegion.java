package com.gmail.flintintoe.playerproperty;

import com.gmail.flintintoe.SimpleSidebar;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class PlayerRegion {
    private SimpleSidebar plugin;

    private WorldGuardPlugin wGPlugin;

    public PlayerRegion(SimpleSidebar plugin) {
        this.plugin = plugin;
    }

    public boolean setupWorldGuard() {
        Plugin wGPlugin = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(wGPlugin instanceof WorldGuardPlugin)) {
            return false;
        }

        this.wGPlugin = (WorldGuardPlugin) wGPlugin;
        return true;
    }

    public String[] getRegionList(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();

       Set<ProtectedRegion> set = wGPlugin.getRegionManager(world).getApplicableRegions(location).getRegions();

        var regions = new String[set.size()];

        // Keeping all parent classes for now
        // This part could change (or another method be added)
        var count = 0;
        for (var region : set) {
            regions[count] = region.getId();
            count++;
        }

        return regions;
    }
}
