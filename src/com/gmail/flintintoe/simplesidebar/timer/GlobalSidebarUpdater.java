package com.gmail.flintintoe.simplesidebar.timer;

import com.gmail.flintintoe.simplesidebar.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GlobalSidebarUpdater extends BukkitRunnable {
    private SidebarManager sidebarM;

    public GlobalSidebarUpdater(SidebarManager sidebarM) {
        this.sidebarM = sidebarM;
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sidebarM.updateSidebar(player);
        }
    }
}
