package com.gmail.flintintoe.simplesidebar.sidebar;

import com.gmail.flintintoe.simplesidebar.message.MessageManager;
import com.gmail.flintintoe.simplesidebar.config.ConfigFile;
import com.gmail.flintintoe.simplesidebar.config.ConfigManager;
import com.gmail.flintintoe.simplesidebar.SimpleSidebar;
import com.gmail.flintintoe.simplesidebar.timer.CustomSidebarUpdater;
import com.gmail.flintintoe.simplesidebar.timer.GlobalSidebarUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

public class SidebarManager {
    private PlaceholderManager placeholderM;
    private ConfigManager configM;
    private MessageManager messageM;

    // BukkitRunnables
    private GlobalSidebarUpdater globalUpdater;
    private CustomSidebarUpdater customUpdater;

    //private static String[] names;
    // Names are now just the index of sidebar
    private String[] headers;
    private String[][] entries;

    private int sidebarCount = 0;

    public SidebarManager(SimpleSidebar plugin) {
        placeholderM = plugin.getPlaceholderManager();
        configM = plugin.getConfigManager();
        messageM = plugin.getMessageManager();

        // Setup sidebar
        if (!setupSidebar(plugin)) {
            messageM.sendToConsole("Fatal: Sidebar module has failed to finish setup process");
            messageM.sendToConsole("Info: Disabling plugin...");
            // Disable plugin
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        messageM.sendToConsole("Info: Sidebar module enabled");
    }

    private boolean setupSidebar(SimpleSidebar plugin) {
        // Use Efficient updater or the standard (Both not recommended)
        if (configM.updatePlaceholerSync) {
            globalUpdater = new GlobalSidebarUpdater(this);
            globalUpdater.runTaskTimer(plugin, 20L, configM.updateTimer * 20);
        }
        if (configM.updatePlaceholerSync){
            customUpdater = new CustomSidebarUpdater(this, configM.afkTimer, configM.afkPlaceholderUpdate);
            customUpdater.runTaskTimer(plugin, 20L, configM.updateTimer * 20);
        }

        List<String> sidebarList = configM.getStrings(ConfigFile.sidebars, "sidebars");
        List<Integer> sidebarStarts = new ArrayList<>();
        List<Integer> sidebarEnds = new ArrayList<>();

        if (sidebarList.get(sidebarList.size() - 1).equals("%divider%") || sidebarList.get(sidebarList.size() - 2).equals("%divider%")) {
            messageM.sendToConsole("Error: Cannot use %divider% in last 2 lines");
            return false;
        }
        // Find number of sidebars
        // Ignore last 2 lines of list
        // Keep note of reference points
        sidebarStarts.add(1);
        for (int i = 0; i < sidebarList.size() - 2; i++) {
            if (sidebarList.get(i).trim().equals("%divider%")) {
                if (i <= 1) {
                    messageM.sendToConsole("Error: Cannot use %divider in first 2 lines");
                    return false;
                }
                sidebarCount++;

                sidebarEnds.add(i - 1);
                sidebarStarts.add(i + 2);
            }
        }
        // One more sidebar than the number of %divide% lines
        sidebarCount++;
        sidebarEnds.add(sidebarList.size() - 1);
        // Declare entries, headers
        //names = new String[sidebarCount];
        entries = new String[sidebarCount][];
        headers = new String[sidebarCount];

        // Initialize entries
        for (int i = 0; i < sidebarCount; i++) {
            // Get a header every loop
            headers[i] = sidebarList.get(sidebarStarts.get(i) - 1);

            int start = sidebarStarts.get(i);
            int end = sidebarEnds.get(i);
            // Declare entries[i]
            entries[i] = new String[end - start + 1];

            // Initialize entries[i]
            int count = 0;
            for (int j = sidebarStarts.get(i); j <= sidebarEnds.get(i); j++) {
                entries[i][count] = sidebarList.get(j);
                count++;
            }
        }

        return true;
    }

    public boolean setSidebar(Player player, int sidebarIndex) {
        // Return false if sidebarIndex out of bounds
        if (sidebarIndex < 0 || sidebarIndex > sidebarCount - 1) {
            return false;
        }

        // Now ready to set sidebar
        ScoreboardManager sbM = Bukkit.getServer().getScoreboardManager();

        Scoreboard scoreboard = sbM.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("" + sidebarIndex, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int entryOrder = entries[sidebarIndex].length;
        int spaceCount = 1;

        // Set header
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', headers[sidebarIndex]));

        for (String entry : entries[sidebarIndex]) {
            entry = placeholderM.setPlaceholders(player, entry);

            // TODO Checking algorithm so that duplicates are not set score twice

            StringBuilder sb = new StringBuilder();

            if (entry.length() == 0 || entry.trim().length() == 0) {
                for (int i = 0; i < spaceCount; i++) {
                    sb.append(" ");
                }
                entry = sb.toString();

                spaceCount++;
            }
            // Add line to scoreboard
            if (entry.length() > 40) {
                objective.getScore(ChatColor.translateAlternateColorCodes('&', entry).substring(0, 39)).setScore(entryOrder);
            } else {
                objective.getScore(ChatColor.translateAlternateColorCodes('&', entry)).setScore(entryOrder);
            }
            entryOrder--;
        }

        // Set scoreboard
        player.setScoreboard(scoreboard);

        return true;
    }

    public void updateSidebar(Player player) {
        int sidebarIndex = getSidebarIndexOf(player);

        if (sidebarIndex == -1) {
            messageM.sendToConsole("Warning: Update sidebar was called when player " + player.getDisplayName() + " had no scoreboard set");
        }

        if (!setSidebar(player, sidebarIndex)) {
            messageM.sendToConsole("Error: Failed to update sidebar of " + player.getDisplayName());
        }
    }

    public void setAFKSidebar(Player player) {
        if (configM.afkTimer != 0) {
            // Set AFK sidebar
            int sidebarIndex = headers.length - 1;

            if (!setSidebar(player, sidebarIndex)) {
                messageM.sendToConsole("Error: An error has occured when setting AFK sidebar to " + player.getDisplayName());
            }
        } else {
            messageM.sendToConsole("Warning: AFK sidebar method called when AFK sidebar is disabled");
        }
    }

    public int getSidebarIndexOf(Player player) {
        String sidebarName;
        try {
            sidebarName = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName();
        } catch (Exception e) {
            //messageM.sendToConsole("Error:" + playerName + " has no sidebar set");
            return -1;
        }

        int sidebarIndex;
        try {
            sidebarIndex = Integer.parseInt(sidebarName);

        } catch (Exception e) {
            //messageM.sendToConsole("Failed to parse sidebar name of " + playerName + " into an integer");
            return -1;
        }

        return sidebarIndex;
    }

    public CustomSidebarUpdater getCustomUpdater() {
        return customUpdater;
    }

    public GlobalSidebarUpdater getGlobalUpdater() {
        return globalUpdater;
    }

    public int getSidebarCount() {
        return sidebarCount;
    }
}