package com.gmail.flintintoe.simpleSidebar.event;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerEvent implements Listener {
    private SimpleSidebar plugin;

    public PlayerEvent (SimpleSidebar plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //TODO Add sidebar to player
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove (PlayerMoveEvent event) {

    }
}
