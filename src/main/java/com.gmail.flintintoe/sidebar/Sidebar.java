package com.gmail.flintintoe.sidebar;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.placeholder.Placeholder;
import com.gmail.flintintoe.timer.CustomSidebarUpdater;
import com.gmail.flintintoe.timer.SidebarUpdater;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sidebar {
    private Placeholder placeholder;
    private Config config;

    // BukkitRunnables
    private SidebarUpdater globalUpdater;
    private CustomSidebarUpdater customUpdater;

    // {Sidebar number} -> {Entry number} -> {Entry part}
    private String[] names;
    private String[] headers;
    private String[][] aliases;
    private String[][][] sidebars;
    private int sidebarCount = 0;

    public Sidebar(SimpleSidebar plugin) {
        placeholder = plugin.getPlaceholder();
        config = plugin.getPgConfig();
    }

    public void setupUpdater(SimpleSidebar plugin) {
        // Use custom updater or the standard
        if (config.isUpdatePhSync() && config.getAfkTimer() != 0) {
            globalUpdater = new SidebarUpdater(this);
            globalUpdater.runTaskTimer(plugin, 20L, config.getUpdateTimer() * 20);
        } else {
            customUpdater = new CustomSidebarUpdater(this, config.getAfkTimer(), config.isAfkPhUpdate());
            customUpdater.runTaskTimer(plugin, 20L, config.getUpdateTimer() * 20);
        }
    }

    public void loadSidebars() {
        List<String> names = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        // List inside a list = list squared lol
        List<List<String>> aliases = new ArrayList<>();

        // Get names, headers, aliases
        // Also, get sidebar count
        int sidebarNum = 1;
        while (config.sectionExists(ConfigFile.SIDEBARS, "sidebars." + sidebarNum)) {
            StringBuilder path = new StringBuilder();
            path.append("sidebars.").append(sidebarNum);

            names.add(config.getString(ConfigFile.SIDEBARS, path.toString() + ".name"));
            headers.add(config.getString(ConfigFile.SIDEBARS, path.toString() + ".header"));

            sidebarNum++;

            if (config.listExists(ConfigFile.SIDEBARS, path.toString() + ".aliases")) {
                aliases.add(config.getStrings(ConfigFile.SIDEBARS, path.toString() + ".aliases"));
            } else {
                // Add empty list if no path
                List<String> emptyList = Arrays.asList("");
                aliases.add(emptyList);
            }
        }
        sidebarCount = sidebarNum - 1;

        // Initialize class Arrays
        this.names = new String[sidebarCount];
        this.headers = new String[sidebarCount];
        this.aliases = new String[sidebarCount][];

        // Partly initialize sidebars Array
        sidebars = new String[sidebarCount][][];

        // For each sidebar...
        for (int i = 0; i < sidebarCount; i++) {
            this.names[i] = names.get(i);
            this.headers[i] = headers.get(i);
            this.aliases[i] = new String[aliases.get(i).size()];

            // For each alias...
            for (int j = 0; j < aliases.get(i).size(); j++) {
                this.aliases[i][j] = aliases.get(i).get(j);
            }

            // Get entries
            List<String> entries = config.getStrings(ConfigFile.SIDEBARS, "sidebars." + (i + 1) + ".entries");

            // Sidebar entries limit: 15
            int entriesSize = entries.size();
            if (entries.size() > 15) {
                entriesSize = 15;
            }

            sidebars[i] = new String[entriesSize][];

            // For each line...
            for (int j = 0; j < entriesSize; j++) {
                String entry = entries.get(j);
                Iterable<String> parts = Splitter.on('%').split(entry);

                // Get number of parts
                int partCount = Iterables.size(parts);
                sidebars[i][j] = new String[partCount];

                // For each part of line...
                int count = 0;
                for (String part : parts) {
                    // Alternate between tags and text
                    if (count % 2 == 0) {
                        sidebars[i][j][count] = part;
                    } else {
                        sidebars[i][j][count] = "%" + part;
                    }
                    count++;
                }
            }
        }

        int i = 0;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean setSidebar(Player player, String sidebarName) {
        // Search sidebarName
        for (int i = 0; i < aliases.length; i++) {
            if (names[i].equalsIgnoreCase(sidebarName)) {
                setSidebar(player, i);
                return true;
            }

            for (int j = 0; j < aliases[i].length; j++) {
                if (aliases[i][j].equalsIgnoreCase(sidebarName)) {
                    setSidebar(player, i);
                    return true;
                }
            }
        }
        return false;
    }

    public void setSidebar(Player player, int sidebarIndex) {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective sbObj = sb.registerNewObjective(sidebarIndex + "", "dummy");

        sbObj.setDisplayName(headers[sidebarIndex]);
        sbObj.setDisplaySlot(DisplaySlot.SIDEBAR);

        String[][] entries = sidebars[sidebarIndex];

        // For each line...
        for (int i = 0; i < entries.length; i++) {
            StringBuilder entry = new StringBuilder();

            // For each part of line...
            for (int j = 0; j < entries[i].length; j++) {
                String part = entries[i][j];

                if (part.charAt(0) == '%') {
                    // Send word without the '%'
                    part = placeholder.setPh(player, part.substring(1));
                    part = placeholder.setTargetPh(part.substring(1));
                }

                entry.append(part);
            }

            sbObj.getScore(entry.toString()).setScore(i);
        }
    }

    public void setAFKSidebar(Player player) {
        if (config.isAllowAfkSet() && config.getAfkTimer() != 0) {
            setSidebar(player, sidebars.length - 1);
        }
    }

    public int getSidebarIndexOf(Player player) {
        int sidebarIndex;

        try {
            sidebarIndex = Integer.parseInt(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName());
        } catch (Exception e) {
            sidebarIndex = -1;
        }
        return sidebarIndex;
    }

    public CustomSidebarUpdater getCustomUpdater() {
        return customUpdater;
    }

    public SidebarUpdater getGlobalUpdater() {
        return globalUpdater;
    }

    public void updateSidebar(Player player) {
        setSidebar(player, getSidebarIndexOf(player));
    }

    public int getSidebarCount() {
        return sidebarCount;
    }
}
