package com.gmail.flintintoe.simpleSidebar.sidebar;

import com.gmail.flintintoe.simpleSidebar.config.ConfigFile;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class SidebarManager {
    private PlaceholderManager placeholderM;
    private ConfigManager configM;
    //private static String[] names;
    // Names are now just the index of sidebar
    private String[] headers;
    private String[][] entries;

    private int sidebarCount = 0;

    public SidebarManager(SimpleSidebar plugin) {
        placeholderM = plugin.getPlaceholderManager();
        configM = plugin.getConfigManager();

        List<String> sidebarList = configM.getEntries(ConfigFile.sidebars, "sidebars");

        // Find number of sidebars
        // Ignore last line of list
        for (int i = 0; i < sidebarList.size() - 1; i++) {
            if (sidebarList.get(i).equals("%divider%")) {
                sidebarCount++;
            }
        }

        // Initialize names, headers
        //names = new String[sidebarCount];
        headers = new String[sidebarCount];

        // Extract entries of each sidebar
        // arrayList size - 1 since last one does not need to be checked
        int count = 0;

        for (int i = 0; i < sidebarList.size() - 1; i++) {
            if (sidebarList.get(i).equals("%divider%")) {
                // Get name of sidebar
                //names[count] = sidebarList.get(i + 1);
                headers[count] = sidebarList.get(i + 1);

                // Find where sidebar entries end
                int end = sidebarList.size();

                for (int j = i + 3; j < sidebarList.size(); j++) {
                    if (sidebarList.get(j).equals("%divider"))
                        end = j;
                }
                // Add entries
                int entryCount = 0;

                for (int k = i + 3; k < end; k++) {
                    entries[count][entryCount] = sidebarList.get(k);
                    entryCount++;
                }

                count++;
            }
        }
    }

    public boolean setSidebar(Player player, int sidebarIndex) {
        // Return false if sidebarIndex out of bounds
        if (sidebarIndex < 0 || sidebarIndex > sidebarCount - 1) {
            return false;
        }

        // Now ready to set sidebar
        ScoreboardManager sb = Bukkit.getServer().getScoreboardManager();

        Scoreboard scoreboard = sb.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("" + sidebarIndex, "dummy");

        int entryCount = 0;
        int spaceCount = 1;

        for (String entry : entries[sidebarIndex]) {
            entry = placeholderM.setPlaceholders(player, entry);

            // TODO Checking algorithm so that duplicates are not set score twice

            if (entry.length() == 0 || entry.trim().length() == 0) {
                entry = "";

                for (int i = 0; i < spaceCount; i++) {
                    entry += " ";
                }
                spaceCount++;

            } else {
                objective.getScore(entry).setScore(entryCount);
            }
            entryCount++;
        }

        // Set scoreboard
        player.setScoreboard(scoreboard);

        return true;
    }

    @Deprecated
    public boolean setSidebar(Player player, String header) {
        int sidebarNum = -1;

        // Query sidebar
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(header)) {
                sidebarNum = i;
            }
        }

        // Return false if sidebar not found
        if (sidebarNum == -1) {
            return false;
        }

        // Now ready to set sidebar
        ScoreboardManager sb = Bukkit.getServer().getScoreboardManager();

        Scoreboard scoreboard = sb.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("" + sidebarNum, "dummy");

        int entryCount = 0;
        int spaceCount = 1;

        for (String entry : entries[sidebarNum]) {
            entry = placeholderM.setPlaceholders(player, entry);

            // TODO Checking algorithm so that duplicates are not set score twice

            if (entry.length() == 0 || entry.trim().length() == 0) {
                entry = "";

                for (int i = 0; i < spaceCount; i++) {
                    entry += " ";
                }
                spaceCount++;

            } else {
                objective.getScore(entry).setScore(entryCount);
            }
            entryCount++;
        }

        // Set scoreboard
        player.setScoreboard(scoreboard);

        return true;
    }

    // TODO Update sidebar function
    public boolean updateSidebar(Player player) {
        return true;
    }

    // TODO Set AFK sidebar function
    public boolean setAFKSidebar(Player player) {
        return true;
    }
}
