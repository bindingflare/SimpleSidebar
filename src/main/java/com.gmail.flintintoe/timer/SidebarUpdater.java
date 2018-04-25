package com.gmail.flintintoe.timer;

import com.gmail.flintintoe.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SidebarUpdater extends BukkitRunnable {
    private Sidebar sidebar;

    public SidebarUpdater(Sidebar sidebar) {
        this.sidebar = sidebar;
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sidebar.updateSidebar(player);
        }
    }
}
