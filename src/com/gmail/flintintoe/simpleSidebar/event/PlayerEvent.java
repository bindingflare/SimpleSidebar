package com.gmail.flintintoe.simpleSidebar.event;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvent implements Listener {
    private SidebarManager sidebarM;
    private ConfigManager configM;

    public PlayerEvent(SimpleSidebar plugin) {
        sidebarM = plugin.getSidebarManager();
        configM = plugin.getConfigManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (configM.haveDefaultSb) {
            // Set sidebar of player
            sidebarM.setSidebar(player, 0);
            // Add player to update list
            sidebarM.getCustomUpdater().set(player.getDisplayName(), configM.duration);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getDisplayName();

        if (configM.duration != 0) {
            sidebarM.getCustomUpdater().remove(playerName);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove(PlayerMoveEvent event) {
        String playerName = event.getPlayer().getDisplayName();
        if (configM.duration != 0) {
            if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                sidebarM.getCustomUpdater().resetCooldown(playerName);
            }
        }
    }
}
