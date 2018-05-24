package com.gmail.flintintoe.sidebar.sidebars;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.sidebar.SidebarManager;
import com.gmail.flintintoe.sidebar.placeholder.Placeholder;
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

/**
 * Deals with loading, setting sidebars and getting sidebar information.
 *
 * @since v0.8.0_pre1
 */
public class Sidebars {
    private Config config;
    private Placeholder ph;

    private String[] names;
    private String[] headers;
    private String[][] aliases; // {Sidebar index} -> {Aliases}
    private String[][][] sidebars; // {Sidebar index} -> {Entry numbers} -> {Entry parts}
    private int sidebarCount = 0;

    private final char PLAYER_TAG_SYMBOL = '࿄'; // U+0FC4 - Tibetan Symbol Dril Bu
    private final char TARGET_TAG_SYMBOL = '࿅'; // U+0FC5 - Tibetan Symbol Rdo Rje
    private final char LOCAL_TAG_SYMBOL = '࿇'; // U+0FC7 - Tibetan Symbol Rdo Rje Rgya Gram
    private final char REMOTE_TAG_SYMBOL = '࿈'; // U+0FC8 - Tibetan Symbol Phur Pa

    public Sidebars(SimpleSidebar plugin) {
        ph = plugin.getSidebarManager().getPlaceholder();

        config = plugin.getConfigManager();

        // Load sidebars
        load();
    }

    public void load() {
        final char TAG_SYMBOL = '%';
        final char ALT_TAG_SYMBOL = '࿉'; // U+0FC9 - Tibetan Symbol Nor Bu

        List<String> names = new ArrayList<>();
        List<String> headers = new ArrayList<>();
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

        // Initialize the arrays using the sidebar count
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

            // Sidebars entries limit: 15
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
                        entries.set(j, entry);
                        // Check for duplicates again
                        k = 0;
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
                        if (ph.isLocalKeyword(part)) {
                            sidebars[i][j][count] = LOCAL_TAG_SYMBOL + part;
                        } else if (ph.isKeyword(part)) {
                            List<String> args = ph.getArgs(part);

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
                        else {
                            sidebars[i][j][count] = REMOTE_TAG_SYMBOL + "%" + part + "%";
                        }
                    }
                    count++;
                }
            }
        }
    }

    public void setEmptySidebar(Player player) {
        String playerName = player.getDisplayName();

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        sb.registerNewObjective("_", "dummy");

        player.setScoreboard(sb);
    }

    public void setSidebar(Player player, int sidebarIndex) {
        String playerName = player.getDisplayName();
        Scoreboard sb = player.getScoreboard();

        String modifier = "";

        // If objective already exists, add an objective with a "_";
        if (sb.getObjective("_") != null) {
            modifier = "_";
        }

        Objective objective = sb.registerNewObjective("_" + modifier, "dummy");
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', headers[sidebarIndex]));

        String[][] entries = sidebars[sidebarIndex];

        int spaceCount = 0;
        int entryScore = entries.length;

        // For each line...
        for (String[] entryParts : entries) {
            StringBuilder strB = new StringBuilder();

            // For each part of line...
            for (int i = 0; i < entryParts.length; i++) {
                // Append based on whether the part is a ph part or not
                if (i % 2 == 1) {
                    String part = entryParts[i];

                    if (part.length() != 0) {
                        if (part.charAt(0) == PLAYER_TAG_SYMBOL) {
                            strB.append(ph.set(player, part.substring(1)));
                        } else if (part.charAt(0) == TARGET_TAG_SYMBOL) {
                            strB.append(ph.setForTarget(part.substring(1)));
                        } else if (part.charAt(0) == REMOTE_TAG_SYMBOL) {
                            strB.append(ph.setRemote(player, part.substring(1)));
                        } else if (part.charAt(0) == LOCAL_TAG_SYMBOL) {
                            strB.append(ph.setLocal(player, part.substring(1)));
                        }
                    }
                } else {
                    strB.append(entryParts[i]);
                }
            }

            // Separate handler for empty entries
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

            objective.getScore(ChatColor.translateAlternateColorCodes('&', output)).setScore(entryScore);
            entryScore--;
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Remove the old objective for existing player
        if (modifier.equals("_")) {
            sb.getObjective("_").unregister();
        } else {
            sb.getObjective("__").unregister();
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

    public int getSidebarIndexOf(String sidebarName) {
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

    public int getSidebarCount() {
        return sidebarCount;
    }
}
