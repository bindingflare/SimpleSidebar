package com.gmail.flintintoe.sidebar.placeholder;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.sidebar.SidebarManager;
import com.google.common.base.Splitter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Deals with getting the values of different placeholders defined by this plugin and externally.
 *
 * @since v0.8.0_pre1
 */
public class Placeholder {
    private Config config;
    private SidebarManager manager;
    private Economy economy;

    private Stat stat;
    private Region region;

    // EMPTY will be a future placeholder
    private final String[] PLAYER_PLACEHOLDERS = {"player", "x", "y", "z", "balance", "timezone", "afktime",
            "afktimeleft", "datetime", "region", "stat", "mstat", "estat", };
    private final String[] TARGET_PLACEHOLDERS = {"balance", "x", "y", "z", "region"};
    private String[] LOCAL_PLACEHOLDERS;
    private String[][] LOCAL_PLACEHOLDER_VALUES;

    private final String TAG_DIVIDER = ".";

    public Placeholder(SimpleSidebar plugin) {
        config = plugin.getConfigManager();
        economy = plugin.getEconomy();
        manager = plugin.getSidebarManager();

        region = new Region(plugin.getwGPlugin());
        stat = new Stat(plugin);
    }

    public void loadLocalPlaceholders() {
        FileConfiguration configFile = config.getPlaceholderConfig();

        Set<String> tags = configFile.getKeys(false);

        LOCAL_PLACEHOLDERS = new String[tags.size()];
        LOCAL_PLACEHOLDER_VALUES = new String[tags.size()][];

        // For each tag...
        int tagCount = 0;
        for(String tag : tags) {
            LOCAL_PLACEHOLDERS[tagCount] = tag;

            ConfigurationSection section = configFile.getConfigurationSection(tag);

            // For single constant placeholder
            if (section.contains("value")) {
                LOCAL_PLACEHOLDER_VALUES[tagCount] = new String[1];
                LOCAL_PLACEHOLDER_VALUES[tagCount][0] = section.getString("value");
            }
            // For multiple constants placeholder
            else if (section.contains("values")) {
                List<String> values = section.getStringList("values");

                LOCAL_PLACEHOLDER_VALUES[tagCount] = new String[values.size()];

                int valueCount = 0;
                for (String value : values) {
                    valueCount++;
                    LOCAL_PLACEHOLDER_VALUES[tagCount][valueCount] = value;
                }
            }
        }

    }

    public String set(Player player, String tag) {
        String wordWithPh = "";

        String keyword = getKeyword(tag);
        List<String> args = getArgs(tag);

        if (args.size() == 0) {
            // player
            if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[0])) {
                wordWithPh = player.getDisplayName();
            }
            // x
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[1])) {
                wordWithPh += player.getLocation().getBlockX();
            }
            // y
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[2])) {
                wordWithPh += player.getLocation().getBlockX();
            }
            // z
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[3])) {
                wordWithPh += player.getLocation().getBlockX();
            }
            // balance
            else if (economy.isEnabled() && keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[4])) {
                wordWithPh += economy.getBalance(player);
            }
            // timezone
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[5])) {
                wordWithPh = ZonedDateTime.now().withFixedOffsetZone().getOffset().getId();
            }
            // afktime
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[6])) {
                wordWithPh += (-manager.getTracker().getCooldown(player.getDisplayName()));
            }
            // afktimeleft
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[7])) {
                wordWithPh += manager.getTracker().getCooldown(player.getDisplayName());
            }
        } else if (args.size() == 1) {
            // datetime
            if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[8])) {
                ZonedDateTime currentDateTime = ZonedDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern(args.get(0));

                wordWithPh = currentDateTime.format(format);
            }
            // region
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[9])) {
                List<String> regions = region.getRegionList(player);
                int i;

                try {
                    i = Integer.parseInt(args.get(0)) - 1;
                } catch (Exception e) {
                    i = -1;
                }

                if (!(i == -1 || i > regions.size() - 1)) {
                    wordWithPh = regions.get(i);
                }
            }
            // stat
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[10])) {
                wordWithPh += stat.getPlayerStat(player, args.get(0));
            }
        } else if (args.size() == 2) {
            // mstat
            if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[11])) {
                Material material = Material.getMaterial(args.get(1));
                wordWithPh += stat.getPlayerStat(player, args.get(0), material);
            }
            // estat
            else if (keyword.equalsIgnoreCase(PLAYER_PLACEHOLDERS[12])) {
                EntityType entityType = EntityType.valueOf(args.get(1));
                wordWithPh += stat.getPlayerStat(player, args.get(0), entityType);
            }
        }
        return wordWithPh;
    }

    public String setForTarget(String word) {
        String wordWithPh = "";

        String property = getKeyword(word);
        List<String> args = getArgs(word);

        // Get player
        Player target = Bukkit.getPlayer(args.get(0).substring(2));

        if (target == null) {
            return wordWithPh;
        }

        if (args.size() == 1) {
            // balance
            if (property.equalsIgnoreCase(TARGET_PLACEHOLDERS[0])) {
                wordWithPh = "" + economy.getBalance(target);
            }
            // x
            else if (property.equalsIgnoreCase(TARGET_PLACEHOLDERS[1])) {
                wordWithPh = "" + target.getLocation().getBlockX();
            }
            // y
            else if (property.equalsIgnoreCase(TARGET_PLACEHOLDERS[2])) {
                wordWithPh = "" + target.getLocation().getBlockX();
            }
            // z
            else if (property.equalsIgnoreCase(TARGET_PLACEHOLDERS[3])) {
                wordWithPh = "" + target.getLocation().getBlockX();
            }
        } else if (args.size() == 2) {
            // region
            if (property.equalsIgnoreCase(TARGET_PLACEHOLDERS[4])) {
                List<String> regions = region.getRegionList(target);
                int i;

                try {
                    i = Integer.parseInt(args.get(1)) - 1;
                } catch (Exception e) {
                    i = -1;
                }

                if (!(i == -1 || i > regions.size() - 1)) {
                    wordWithPh = regions.get(i);
                }
            }
        }

        return wordWithPh;
    }

    public String setLocal(Player player, String tag) {
        String wordWithPh = "";

        for (int i = 0; i < LOCAL_PLACEHOLDERS.length; i++) {
            if (LOCAL_PLACEHOLDERS[i].equals(tag)) {
                if (LOCAL_PLACEHOLDER_VALUES[i].length == 1) {
                    wordWithPh = LOCAL_PLACEHOLDER_VALUES[i][0];
                } else {
                    Random random = new Random();

                    int index = random.nextInt(LOCAL_PLACEHOLDER_VALUES[i].length);
                    wordWithPh = LOCAL_PLACEHOLDER_VALUES[i][index];
                }
            }
        }
        return wordWithPh;
    }

    public String setRemote(Player player, String tag) {
        return PlaceholderAPI.setPlaceholders(player, tag);

    }

    private String getKeyword(String tag) {
        String property = tag;

        if (tag.contains(TAG_DIVIDER)) {
            property = tag.substring(0, tag.indexOf(TAG_DIVIDER));
        }

        return property;
    }

    public List<String> getArgs(String tag) {
        Iterable<String> argsIterable = Splitter.on(TAG_DIVIDER).split(tag);
        List<String> args = new ArrayList<>();

        for (String arg : argsIterable) {
            args.add(arg);
        }
        // Remove property
        args.remove(0);

        return args;
    }

//    private boolean isPh(String word) {
//        return word.indexOf(0) == '%';
//    }

    public boolean isLocalKeyword(String tag) {
        String keyword = getKeyword(tag);

        for (String localKeyword:LOCAL_PLACEHOLDERS) {
            if (localKeyword.equals(keyword)) {
                return true;
            }
        }

        return false;
    }

    public boolean isKeyword(String tag) {
        String keyword = getKeyword(tag);

        for (String playerPh : PLAYER_PLACEHOLDERS) {
            // Compare the keyword of word with available keywords
            if (keyword.equalsIgnoreCase(playerPh)) {
                return true;
            }
        }

        return false;
    }
}
