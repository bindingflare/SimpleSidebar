package com.gmail.flintintoe.sidebar;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.placeholder.Placeholder;
import com.gmail.flintintoe.timer.CustomSidebarUpdater;
import com.gmail.flintintoe.timer.SidebarUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
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

        int sidebarNum = 1;
        while (config.paramExists(ConfigFile.sidebars, "sidebars." + sidebarNum)) {
            names.add(config.getString(ConfigFile.sidebars, "sidebars." + sidebarNum + ".name"));
            headers.add(config.getString(ConfigFile.sidebars, "sidebars." + sidebarNum + ".header"));
            sidebarNum++;
            aliases.add(config.getStrings(ConfigFile.sidebars, "sidebars." + sidebarNum + ".aliases"));
        }
        sidebarCount = sidebarNum - 1;

        this.names = new String[sidebarCount];
        this.headers = new String[sidebarCount];
        this.aliases = new String[sidebarCount][];
        sidebars = new String[sidebarCount][][];

        // For each sidebar...
        for (int i = 0; i < sidebarCount; i++) {
            this.names[i] = names.get(i);
            this.headers[i] = headers.get(i);
            this.aliases[i] = new String[aliases.size()];

            // For each alias...
            for (int j = 0; j < aliases.size(); j++) {
                this.aliases[i][j] = aliases.get(i).get(j);
            }

            List<String> entries = config.getStrings(ConfigFile.sidebars, "sidebars." + sidebarNum + ".entries");

            // Sidebar entries limit: 15
            int entriesSize = entries.size();
            if (entries.size() > 15) {
                entriesSize = 15;
            }

            // For each line...
            for (int j = 0; j < entriesSize; j++) {
                sidebars[i] = new String[entriesSize][];

                String entry = entries.get(j);
                List<Integer> splitters = new ArrayList<>();
                int partCount = 1;

                // Getting places to split String
                while (entry.contains("%")) {
                    splitters.add(entry.indexOf("%"));
                    entry = entry.replace("%", "");

                    partCount++;
                }

                // If last character of String is "%" remove that index in splitters
                if (splitters.get(splitters.size() - 1) == entry.length()) {
                    splitters.remove(splitters.size());
                } else {
                    partCount++;
                }

                sidebars[i][j] = new String[partCount];
                int lastIndex = 0;
                int k;

                // Adding parts to the sidebars Array
                for (k = 0; k < splitters.size(); k++) {
                    String part = entry.substring(lastIndex, splitters.get(k) - 1);

                    // TODO Use different characters that call smaller setPh methods to further improve performance
                    if (placeholder.isKeyword(part)) {
                        part = "%" + part;
                    }

                    sidebars[i][j][k] = part;
                    lastIndex = splitters.get(k);
                }
                sidebars[i][j][k] = entry.substring(lastIndex);
            }
        }
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

        for (int i = 0; i < entries.length; i++) {
            StringBuilder entry = new StringBuilder();

            for (int j = 0; j < entries[i].length; j++) {
                String part = entries[i][j];

                if (part.indexOf("%") == 0) {
                    // Send word without the '%'
                    part = placeholder.setPh(player, part.substring(1));
                    part = placeholder.setTargetPh(part);
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
