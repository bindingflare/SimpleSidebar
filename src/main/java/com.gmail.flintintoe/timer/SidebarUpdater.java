package com.gmail.flintintoe.timer;

import com.gmail.flintintoe.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SidebarUpdater extends BukkitRunnable {
    private SidebarManager sidebarM;

    public SidebarUpdater(SidebarManager sidebarM) {
        this.sidebarM = sidebarM;
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sidebarM.updateSidebar(player);
        }
    }
}
