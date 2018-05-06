package com.gmail.flintintoe.event;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.sidebar.Sidebar;
import com.gmail.flintintoe.timer.SidebarRunnable;
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
    private SidebarRunnable runnable;
    private Config config;

    public PlayerEvent(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        config = plugin.getPgConfig();
        runnable = plugin.getRunnable();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        runnable.set(player.getDisplayName(), -1, config.getAfkTimer());

        if (config.isSetOnLogin()) {
            sidebar.setAndUpdateSidebar(player, 0);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeave(PlayerQuitEvent event) {
        runnable.remove(event.getPlayer().getDisplayName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Reset player afkTimer when the player's coordinates change
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            sidebar.updateSidebar(player, true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMine(BlockBreakEvent event) {
        Player player = event.getPlayer();

        sidebar.updateSidebar(player, true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        sidebar.updateSidebar(player, true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        runnable.updateSidebarTime(player.getDisplayName(), config.getAfkTimer());

        //sidebar.updateSidebar(player, true);
    }
}
