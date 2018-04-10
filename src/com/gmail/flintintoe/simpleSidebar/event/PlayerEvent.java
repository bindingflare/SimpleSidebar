package com.gmail.flintintoe.simpleSidebar.event;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvent implements Listener {
    private SimpleSidebar plugin;

    public PlayerEvent (SimpleSidebar plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin (PlayerJoinEvent event) {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove (PlayerEvent event) {

    }
}
