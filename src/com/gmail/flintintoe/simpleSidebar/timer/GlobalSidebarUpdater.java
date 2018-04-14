package com.gmail.flintintoe.simpleSidebar.timer;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.gmail.flintintoe.simpleSidebar.sidebar.SidebarManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GlobalSidebarUpdater extends BukkitRunnable {
    SidebarManager sidebarM;

    public GlobalSidebarUpdater(SimpleSidebar plugin) {
        sidebarM = plugin.getSidebarManager();
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sidebarM.updateSidebar(player);
        }
    }
}
