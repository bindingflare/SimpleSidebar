package com.gmail.flintintoe.event;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvent implements Listener {
    private Sidebar sidebar;
    private Config config;

    public PlayerEvent(SimpleSidebar plugin) {
        sidebar = plugin.getSidebar();
        config = plugin.getConfigMan();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (config.setOnLogin) {
            sidebar.setSidebar(player, 0);
            // If customUpdater is active, add player name to its update list
            if (config.afkTimer != 0) {
                sidebar.getCustomUpdater().set(player.getDisplayName());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getDisplayName();

        // If customUpdater is active, remove player name to its update list
        if (config.afkTimer != 0) {
            sidebar.getCustomUpdater().remove(playerName);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove(PlayerMoveEvent event) {
        String playerName = event.getPlayer().getDisplayName();

        // If customUpdater is active, reset player afkTimer when the player moves 1 block
        if (config.afkTimer != 0 && (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
            sidebar.getCustomUpdater().resetCooldown(playerName);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getDisplayName();

        if (config.afkTimer != 0) {
            sidebar.getCustomUpdater().resetCooldown(playerName);
        }
    }

//    TODO Player mine and place block (If possible)
//    @EventHandler(priority = EventPriority.HIGH)
//    public void playerMine(PLayer)
}
