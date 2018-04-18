package com.gmail.flintintoe.simplesidebar.event;

import com.gmail.flintintoe.simplesidebar.SimpleSidebar;
import com.gmail.flintintoe.simplesidebar.config.ConfigManager;
import com.gmail.flintintoe.simplesidebar.sidebar.SidebarManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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

        if (configM.setOnLogin) {
            sidebarM.setSidebar(player, 0);
            // If customUpdater is active, add player name to its update list
            if (configM.afkTimer != 0) {
                sidebarM.getCustomUpdater().set(player.getDisplayName());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getDisplayName();

        // If customUpdater is active, remove player name to its update list
        if (configM.afkTimer != 0) {
            sidebarM.getCustomUpdater().remove(playerName);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerMove(PlayerMoveEvent event) {
        String playerName = event.getPlayer().getDisplayName();

        // If customUpdater is active, reset player afkTimer when the player moves 1 block
        if (configM.afkTimer != 0 && (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
            sidebarM.getCustomUpdater().resetCooldown(playerName);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getDisplayName();

        if (configM.afkTimer != 0) {
            sidebarM.getCustomUpdater().resetCooldown(playerName);
        }
    }

//    TODO Player mine and place block (If possible)
//    @EventHandler(priority = EventPriority.HIGH)
//    public void playerMine(PLayer)
}
