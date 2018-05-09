package com.gmail.flintintoe.sidebar;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.PluginConfig;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.placeholder.Placeholder;
import com.gmail.flintintoe.timer.SidebarRunnable;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class Sidebar {
    private Placeholder placeholder;
    private PluginConfig config;
    private SidebarRunnable runnable;

    // {Sidebar number} -> {Entry number} -> {Entry part}
    private String[] names;
    private String[] headers;
    private String[][] aliases;
    private String[][][] sidebars;
    private int sidebarCount = 0;

    // Using special UTF-8 tags:
    // U+0FC4 - Tibetan Symbol Dril Bu
    // U+0FC5 - Tibetan Symbol Rdo Rje
    // U+0FC7 - Tibetan Symbol Rdo Rje Rgya Gram
    private final char PLAYER_TAG_SYMBOL = '࿄';
    private final char TARGET_TAG_SYMBOL = '࿅';

    // Keeping final chars organized here
    @SuppressWarnings("FieldCanBeLocal")
    private final char TAG_SYMBOL = '%';
    @SuppressWarnings("FieldCanBeLocal")
    private final char ALT_TAG_SYMBOL = '࿇';

    public Sidebar(SimpleSidebar plugin) {
        placeholder = plugin.getPlaceholder();
        config = plugin.getPluginConfig();
        runnable = plugin.getRunnable();
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
                List<String> emptyList = new ArrayList<>();
                aliases.add(emptyList);
            }
        }
        sidebarCount = sidebarNum - 1;

        // Initialize class String Arrays
        this.names = new String[sidebarCount];
        this.headers = new String[sidebarCount];

        this.aliases = new String[sidebarCount][];
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

            // For each entry...
            for (int j = 0; j < entriesSize; j++) {
                String entry = entries.get(j);

                // Change masked % into another character
                String entryCopy = entry;
                List<Integer> indexes = new ArrayList<>();
                while (entryCopy.contains(Character.toString(TAG_SYMBOL))) {
                    int index = entryCopy.indexOf(TAG_SYMBOL);
                    if ((char) (index - 1) == '\\') {
                        indexes.add(index);
                    } else {
                        entryCopy = entryCopy.replace(Character.toString(TAG_SYMBOL), " ");
                    }
                }

                char[] characters = entry.toCharArray();
                for (int index : indexes) {
                    characters[index] = ALT_TAG_SYMBOL;
                }
                entry = String.copyValueOf(characters);

                // Duplicate checker
                for (int k = 0; k < j; k++) {
                    if (entries.get(k).equals(entry)) {
                        entry += " ";
                    }
                }

                // Get parts of entry
                Iterable<String> parts = Splitter.on(TAG_SYMBOL).split(entry);

                // Get number of parts
                int partCount = Iterables.size(parts);
                sidebars[i][j] = new String[partCount];

                // For each part of entry...
                int count = 0;

                for (String part : parts) {
                    // Revert entries back into tag symbols
                    part = part.replaceAll(Character.toString(ALT_TAG_SYMBOL), Character.toString(TAG_SYMBOL));

                    // Alternate between tags and text
                    if (count % 2 == 0) {
                        sidebars[i][j][count] = part;
                    } else {
                        List<String> args = placeholder.getArgs(part);

                        // Prevent errors in the following code
                        if (args.size() == 0) {
                            args.add("  ");
                        } else if (args.get(0).length() < 2) {
                            args.set(0, args.get(0) + "  ");
                        }

                        if (args.get(0).substring(0, 2).equalsIgnoreCase("p/")) {
                            sidebars[i][j][count] = TARGET_TAG_SYMBOL + part;
                        } else {
                            sidebars[i][j][count] = PLAYER_TAG_SYMBOL + part;
                        }
                    }
                    count++;
                }
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean setAndUpdateSidebar(Player player, String sidebarName) {
        // Search sidebarName
        for (int i = 0; i < aliases.length; i++) {
            if (names[i].equalsIgnoreCase(sidebarName)) {
                setAndUpdateSidebar(player, i);
                return true;
            }

            for (int j = 0; j < aliases[i].length; j++) {
                if (aliases[i][j].equalsIgnoreCase(sidebarName)) {
                    setAndUpdateSidebar(player, i);
                    return true;
                }
            }
        }
        return false;
    }

    public void setSidebar(Player player, int sidebarIndex) {
        String playerName = player.getDisplayName();

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective sbObj = sb.registerNewObjective(sidebarIndex + "", "dummy");

        sbObj.setDisplayName(ChatColor.translateAlternateColorCodes('&', headers[sidebarIndex]));
        sbObj.setDisplaySlot(DisplaySlot.SIDEBAR);

        String[][] entries = sidebars[sidebarIndex];

        int spaceCount = 0;
        int entryScore = entries.length;

        // For each line...
        for (String[] entry : entries) {
            StringBuilder strB = new StringBuilder();

            // For each part of line...
            for (String part : entry) {
                if (part.length() != 0) {
                    if (part.charAt(0) == PLAYER_TAG_SYMBOL) {
                        part = placeholder.setPlaceholder(player, part.substring(1));
                    } else if (part.charAt(0) == TARGET_TAG_SYMBOL) {
                        part = placeholder.setTargetPlaceholder(part.substring(1));
                    }
                }
                strB.append(part);
            }

            if (strB.toString().trim().length() == 0) {
                spaceCount++;

                StringBuilder emtpyEntry = new StringBuilder();
                for (int j = 0; j < spaceCount; j++) {
                    emtpyEntry.append(" ");
                }

                strB = emtpyEntry;
            }

            String output = strB.toString();
            // Remove overflow characters (if any)
            if (output.length() > 40) {
                output = output.substring(0, 40);
            }

            sbObj.getScore(ChatColor.translateAlternateColorCodes('&', output)).setScore(entryScore);
            entryScore--;
        }

        player.setScoreboard(sb);
    }

    public void setAndUpdateSidebar(Player player, int sidebarIndex) {
        runnable.updateSidebarIndex(player.getDisplayName(), sidebarIndex);

        setSidebar(player, sidebarIndex);
    }

    public void updateSidebar(Player player, boolean updateAfkTime) {
        if (updateAfkTime) {
            runnable.updateSidebarTime(player.getDisplayName(), config.getAfkTimer());
        }

        if (getSidebarIndexOf(player) != -1) {
            setSidebar(player, getSidebarIndexOf(player));
        }
    }

    public String getSidebarName(int sidebarIndex) {
        if (sidebarIndex < 0 && sidebarIndex >= sidebarCount) {
            return null;
        }

        return names[sidebarIndex];
    }

    public String[] getSidebarAliases(int sidebarIndex) {
        if (sidebarIndex < 0 && sidebarIndex >= sidebarCount) {
            return null;
        }

        return aliases[sidebarIndex].clone();
    }

    private int getSidebarIndexOf(String sidebarName) {
        for (int i = 0; i < aliases.length; i++) {
            if (names[i].equalsIgnoreCase(sidebarName)) {
                return i;
            }

            for (int j = 0; j < aliases[i].length; j++) {
                if (aliases[i][j].equalsIgnoreCase(sidebarName)) {
                    return i;
                }
            }
        }
        return -1;
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

    // Return values
    // 0 - No errors
    // 1 - Query not found, Integer negative
    // 2 - Cannot set to AFK sidebar
    // 3 - Index out of bounds
    // 4 - Unexpected error
    //
    // NOTE:
    // Also registers the player to the SidebarRunnable automatically
    public int querySidebarIndexOf(String query) {
        // Check query
        int sidebarIndex = getSidebarIndexOf(query);

        // Check if query is an String positive
        if (sidebarIndex == -1) {
            for (char c : query.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return -1;
                }
            }
        }
        // Try to set sidebar (For String query)
        else {
            // Check if setting AFK sb
            if (sidebarIndex == getSidebarCount() - 1 && !config.isAllowAfkSet()) {
                return -2;
            }
            return 0;
        }

        // sidebarIndex will always be -1 at this point

        try {
            sidebarIndex = Integer.parseInt(query) - 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -4;
        }

        // Set sidebar (For int query)
        if (sidebarIndex != -1) {
            if (sidebarIndex == getSidebarCount() - 1 && !config.isAllowAfkSet()) {
                return -2;
            } else if (sidebarIndex < -1 || sidebarIndex >= getSidebarCount()) {
                return -3;
            } else {
                return 0;
            }
        }
        return -3;
    }

    public int getSidebarCount() {
        return sidebarCount;
    }
}
