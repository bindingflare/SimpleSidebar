package com.gmail.flintintoe.placeholder;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.playerproperty.PlayerEconomy;
import com.gmail.flintintoe.playerproperty.PlayerRegion;
import com.gmail.flintintoe.playerproperty.PlayerStatistic;
import com.gmail.flintintoe.timer.CustomSidebarUpdater;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

public class Placeholder {
    private Config config;

    private PlayerStatistic statistic;
    private PlayerEconomy economy;
    private PlayerRegion region;

    private CustomSidebarUpdater customUpdater;

    private final String[] playerPhs = {"player", "x", "y", "z", "balance", "date", "time", "region", "afktime", "afktimeleft", "stat", "mstat", "estat"};
    private final String[] targetPhs = {"balance", "x", "y", "z", "region"};

    public Placeholder(SimpleSidebar plugin) {
        config = plugin.getPgConfig();

        statistic = plugin.getPlStatistic();
        economy = plugin.getPlEconomy();
        region = plugin.getPlRegion();
    }

    public String setPh(Player player, String tag) {
        String wordWithPh = "";

        if (tag.length() > 0) {
            String keyword = getKeyword(tag);
            String[] args = getArgs(tag);

            if (args.length == 0) {
                // player
                if (keyword.equalsIgnoreCase(playerPhs[0])) {
                    wordWithPh = player.getDisplayName();
                }
                // x
                if (keyword.equalsIgnoreCase(playerPhs[1])) {
                    wordWithPh += player.getLocation().getBlockX();
                }
                // y
                if (keyword.equalsIgnoreCase(playerPhs[2])) {
                    wordWithPh += player.getLocation().getBlockX();
                }
                // z
                if (keyword.equalsIgnoreCase(playerPhs[3])) {
                    wordWithPh += player.getLocation().getBlockX();
                }
                // balance
                if (config.isEconomyEnabled()) {
                    if (keyword.equalsIgnoreCase(playerPhs[4])) {
                        wordWithPh += economy.getBalance(player);
                    }
                }
            } else if (args.length == 1) {
                // date
                if (keyword.equalsIgnoreCase(playerPhs[5])) {
                    ZoneId currentZone = ZoneId.systemDefault();
                    ZonedDateTime currentDateTime = ZonedDateTime.now(currentZone);

                    DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
                    wordWithPh = formatterBuilder.appendPattern(args[0]).toFormatter().format(currentDateTime);
                }
                // time
                if (keyword.equalsIgnoreCase(playerPhs[6])) {
                    ZoneId currentZone = ZoneId.systemDefault();
                    ZonedDateTime currentDateTime = ZonedDateTime.now(currentZone);

                    DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
                    wordWithPh = formatterBuilder.appendPattern(args[0]).toFormatter().format(currentDateTime);
                }
                // region
                if (config.isRegionEnabled()) {
                    if (keyword.equalsIgnoreCase(playerPhs[7])) {
                        List<String> regions = region.getRegionList(player);

                        int i = Integer.parseInt(args[0]);

                        if (i > regions.size() - 1) {
                            wordWithPh = "";
                        } else {
                            wordWithPh = regions.get(i);
                        }
                    }
                }
                // AFK
                if (customUpdater != null) {
                    // afktime
                    if (keyword.equalsIgnoreCase(playerPhs[8])) {
                        customUpdater.getAfkTime(player.getDisplayName());
                    }
                    //afktimeleft
                    if (keyword.equalsIgnoreCase(playerPhs[9])) {
                        customUpdater.getTime(player.getDisplayName());
                    }
                }
                // Player statistics
                if (keyword.equalsIgnoreCase(playerPhs[10])) {
                    wordWithPh += statistic.getPlayerStat(player, args[0]);
                }
            } else if (args.length == 2) {
                // Player statistics
                if (keyword.equalsIgnoreCase(playerPhs[11])) {
                    String materialName = args[1];

                    Material material = Material.getMaterial(materialName);

                    wordWithPh += statistic.getPlayerStat(player, args[0], material);
                }
                if (keyword.equalsIgnoreCase(playerPhs[12])) {
                    String entityName = args[1];

                    EntityType entityType = EntityType.valueOf(entityName);

                    wordWithPh += statistic.getPlayerStat(player, args[0], entityType);
                }
            }
        }
        return wordWithPh;
    }

    public String setTargetPh(String word) {
        String wordWithPh = "'";

        String property = getKeyword(word);
        String[] args = getArgs(word);

        // Get player
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            return "{ERROR}";
        }

        if (args.length == 1) {
            // balance
            while (property.equalsIgnoreCase(targetPhs[0])) {
                wordWithPh = "" + economy.getBalance(target);
            }
            // x
            if (property.equalsIgnoreCase(targetPhs[1])) {
                wordWithPh = "" + target.getLocation().getBlockX();
            }
            // y
            if (property.equalsIgnoreCase(targetPhs[2])) {
                wordWithPh = "" + target.getLocation().getBlockX();
            }
            // z
            if (property.equalsIgnoreCase(targetPhs[3])) {
                wordWithPh = "" + target.getLocation().getBlockX();
            }
        }
        // Other player's region
        if (args.length == 2) {
            // region
            if (config.isRegionEnabled()) {
                if (property.equalsIgnoreCase(targetPhs[4])) {
                    wordWithPh = region.getRegionList(target).get(Integer.parseInt(args[1]));
                }
            }
        }

        return wordWithPh;
    }

    private String getKeyword(String tag) {
        String property;

        if (!tag.contains("_")) {
            property = tag.substring(1, tag.length() - 1);
        } else {
            property = tag.substring(1, tag.indexOf("_") - 1);
        }

        return property;
    }

    private String[] getArgs(String tag) {
        // Get arguments
        Iterable<String> argsIterable = Splitter.on('_').split(tag);

        int argCount = Iterables.size(argsIterable);
        String[] args = new String[argCount - 1];

        // Convert arguments to Array
        int count = 0;
        for (String arg : argsIterable) {
            if (count != 0) {
                args[count - 1] = arg;
            }
            count++;
        }
        return args;
    }

//    private boolean isPh(String word) {
//        return word.indexOf(0) == '%';
//    }

    public boolean isKeyword(String word) {
        String keyword = getKeyword(word);

        for (String playerPh : playerPhs) {
            // Compare the keyword of word with available keywords
            if (keyword.equalsIgnoreCase(playerPh)) {
                return true;
            }
        }

        return false;
    }

    public void setCustomUpd(SimpleSidebar plugin) {
        customUpdater = plugin.getSidebar().getCustomUpdater();
    }
}
