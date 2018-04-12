package com.gmail.flintintoe.simpleSidebar.event;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import com.gmail.flintintoe.simpleSidebar.timer.CooldownTimer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerEvent implements Listener {
    private SimpleSidebar plugin;
    private SidebarManager sidebarM;

    public PlayerEvent(SimpleSidebar plugin) {
        this.plugin = plugin;
        sidebarM = plugin.getSidebarManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Set sidebar of player
        sidebarM.setSidebar(player, 0);
        CooldownTimer timer = new CooldownTimer(player, 20);
        timer.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove(PlayerMoveEvent event) {
        // TODO Reset cooldown timer
        // CooldownTimer.resetPlayer(event.getPlayer());
    }
}
