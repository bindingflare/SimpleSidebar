package com.gmail.flintintoe.simpleSidebar.event;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import com.gmail.flintintoe.simpleSidebar.timer.SidebarUpdateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerEvent implements Listener {
    private SidebarManager sidebarM;
    private SidebarUpdateManager sbUpdateM;

    public PlayerEvent(SimpleSidebar plugin) {
        sidebarM = plugin.getSidebarManager();
        sbUpdateM = plugin.getSidebarUpdateManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Set sidebar of player
        sidebarM.setSidebar(player, 0);
        sbUpdateM.setPlayerCooldown(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            // Reset player AFK timer
            sbUpdateM.setPlayerCooldown(player);
        }
    }
}
