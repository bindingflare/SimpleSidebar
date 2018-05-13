package com.gmail.flintintoe.event;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.sidebar.sidebars.Sidebars;
import com.gmail.flintintoe.sidebar.tracker.Tracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

/**
 * Handles the command "/adminsidebar".
 *
 * @since v0.8.0_RC1
 */
public class PlayerEvent implements Listener {
    private Sidebars sidebars;
    private Tracker tracker;
    private Config config;

    public PlayerEvent(SimpleSidebar plugin) {
        sidebars = plugin.getSidebarManager().getSidebars();
        config = plugin.getConfigManager();
        tracker = plugin.getSidebarManager().getTracker();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent event) {
        sidebars.setEmptySidebar(event.getPlayer());
        tracker.add(event.getPlayer().getDisplayName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeave(PlayerQuitEvent event) {
        tracker.remove(event.getPlayer().getDisplayName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove(PlayerMoveEvent event) {
        // Reset player afkTimer when the player's coordinates change
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            tracker.update(event.getPlayer().getDisplayName());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMine(BlockBreakEvent event) {
        tracker.update(event.getPlayer().getDisplayName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerPlace(BlockPlaceEvent event) {
        tracker.update(event.getPlayer().getDisplayName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerChat(AsyncPlayerChatEvent event) {
        tracker.update(event.getPlayer().getDisplayName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerCommand(PlayerCommandPreprocessEvent event) {
        tracker.update(event.getPlayer().getDisplayName());
    }
}
