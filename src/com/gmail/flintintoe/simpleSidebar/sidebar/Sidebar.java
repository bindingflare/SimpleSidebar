package com.gmail.flintintoe.simpleSidebar.sidebar;

import com.gmail.flintintoe.simpleSidebar.Configuration;
import com.gmail.flintintoe.simpleSidebar.ConfigurationFile;
import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class Sidebar {
    private String name;
    private String header;
    private String[] entries;

    public Sidebar(SimpleSidebar plugin, String name) {
        List<String> sidebarList = Configuration.getEntries(ConfigurationFile.sidebar, "sidebars");

        // arrayList size - 1 since last one does not need to be checked
        for (int i = 0; i < sidebarList.size() - 1; i++) {
            if (sidebarList.get(i).equals("%divider%")) {
                if (sidebarList.get(i + 1).equals(name)) {
                    // Get name of sidebar
                    name = sidebarList.get(i + 1);

                    // Find where sidebar entries end
                    int end = sidebarList.size();

                    for (int j = i + 2; j < sidebarList.size(); j++) {
                        if (sidebarList.get(j).equals("%divider"))
                            end = j;
                    }
                    // Initialise entries
                    entries = new String[end - (i + 2)];
                    // Add entries to entries
                    int count = 0;
                    for (int k = i + 2; k < end; k++) {
                        entries[count] = sidebarList.get(k);
                    }
                }
            }
        }

        // TODO Get header
    }

    public boolean setSidebar(Player player, String name) {
        ScoreboardManager sb = Bukkit.getServer().getScoreboardManager();

        Scoreboard scoreboard = sb.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");

        for (int i = entries.length - 1; i >= 0; i--) {
            objective.getScore(Placeholder.setPlaceholders(player, entries[i])).setScore(i);
        }

        return true;
    }
}
