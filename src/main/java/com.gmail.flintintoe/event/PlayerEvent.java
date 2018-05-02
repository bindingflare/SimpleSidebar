package com.gmail.flintintoe.event;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvent implements Listener {
    private Sidebar sidebar;
    private Config config;

    public PlayerEvent(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        config = plugin.getPgConfig();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (config.isSetOnLogin()) {
            sidebar.setSidebar(player, 0);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeave(PlayerQuitEvent event) {
        // If customUpdater is active, remove player name to its update list
        if (config.getAfkTimer() != 0) {
            sidebar.getCustomUpdater().remove(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // If customUpdater is active, reset player afkTimer when the player moves 1 block
        if (config.getAfkTimer() != 0 && (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
            sidebar.getCustomUpdater().resetCooldown(player);
        }

        if (config.isUpdatePhAsync()) {
            sidebar.updateSidebar(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMine(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (config.getAfkTimer() != 0) {
            sidebar.getCustomUpdater().resetCooldown(player);
        }

        if (config.isUpdatePhAsync()) {
            sidebar.updateSidebar(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (config.getAfkTimer() != 0) {
            sidebar.getCustomUpdater().resetCooldown(player);
        }

        if (config.isUpdatePhAsync()) {
            sidebar.updateSidebar(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (config.getAfkTimer() != 0) {
            sidebar.getCustomUpdater().resetCooldown(player);
        }

        if (config.isUpdatePhAsync()) {
            sidebar.updateSidebar(player);
        }
    }
}
