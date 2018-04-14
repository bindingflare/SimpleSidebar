package com.gmail.flintintoe.simpleSidebar.sidebar;

import com.gmail.flintintoe.simpleSidebar.MessageManager;
import com.gmail.flintintoe.simpleSidebar.config.ConfigFile;
import com.gmail.flintintoe.simpleSidebar.config.ConfigManager;
import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class SidebarManager {
    private PlaceholderManager placeholderM;
    private ConfigManager configM;
    private MessageManager messageM;
    //private static String[] names;
    // Names are now just the index of sidebar
    private String[] headers;
    private String[][] entries;

    private int sidebarCount = 0;

    public SidebarManager(SimpleSidebar plugin) {
        placeholderM = plugin.getPlaceholderManager();
        configM = plugin.getConfigManager();
        messageM = plugin.getMessageManager();

        List<String> sidebarList = configM.getEntries(ConfigFile.sidebars, "sidebars");

        // Find number of sidebars
        // Ignore last line of list
        for (int i = 0; i < sidebarList.size() - 1; i++) {
            if (sidebarList.get(i).equals("%divider%")) {
                sidebarCount++;
            }
        }

        // Initialize entries, headers
        //names = new String[sidebarCount];
        entries = new String[sidebarCount][];
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
                // Initialize 2nd dimension of entries
                entries[count] = new String[end - i];
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
        if (player.isOnline()) {
            // Return false if sidebarIndex out of bounds
            if (sidebarIndex < 0 || sidebarIndex > sidebarCount - 1) {
                return false;
            }

            // Now ready to set sidebar
            ScoreboardManager sbM = Bukkit.getServer().getScoreboardManager();

            Scoreboard scoreboard = sbM.getNewScoreboard();
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

        return false;
    }

    @Deprecated
    public boolean setSidebar(Player player, String header) {
        if (player.isOnline()) {
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
            ScoreboardManager sbM = Bukkit.getServer().getScoreboardManager();

            Scoreboard scoreboard = sbM.getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("" + sidebarNum, "dummy");

            int entryCount = 0;
            int spaceCount = 1;

            for (String entry : entries[sidebarNum]) {
                entry = placeholderM.setPlaceholders(player, entry);

                // TODO Checking algorithm so that duplicates are not set score twice

                if (entry.length() == 0 || entry.trim().length() == 0) {
                    // Create spaces
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < spaceCount; i++) {
                        sb.append(" ");
                    }
                    entry = sb.toString();
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

        return false;
    }

    public boolean updateSidebar(Player player) {
        if (player.isOnline()) {
            // Set sidebar
            String name = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName();

            int sidebarIndex;

            try {
                sidebarIndex = Integer.parseInt(name);
            } catch (Exception e) {
                messageM.sendToConsole("Unexpected error updating a sidebar. Name could not be parsed to int");
                return false;
            }

            setSidebar(player, sidebarIndex);
            return true;
        }

        return false;
    }

    public boolean setAFKSidebar(Player player) {
        if (player.isOnline()) {
            // Set AFK sidebar
            int sidebarIndex = headers.length - 1;

            setSidebar(player, sidebarIndex);

            return true;
        }

        return false;
    }
}
