package com.gmail.flintintoe.placeholder;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.message.MessageManager;
import com.gmail.flintintoe.playerproperty.PlayerEconomy;
import com.gmail.flintintoe.playerproperty.PlayerRegion;
import com.gmail.flintintoe.playerproperty.PlayerStatistic;
import com.gmail.flintintoe.timer.CustomSidebarUpdater;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

public class Placeholder {
    private Config config;
    private MessageManager messenger;

    private PlayerStatistic statistic;
    private PlayerEconomy economy;
    private PlayerRegion region;

    private CustomSidebarUpdater customUpdater;

    public Placeholder(SimpleSidebar plugin) {
        config = plugin.getConfigMan();
        messenger = plugin.getMessenger();

        statistic = plugin.getPlStatistic();
        economy = plugin.getPlEconomy();
        region = plugin.getPlRegion();
    }

    public void setupPholder(SimpleSidebar plugin) {
        customUpdater = plugin.getSidebar().getCustomUpdater();
    }

    public String setPlPholder(Player player, String word) {
        String wordWithPh = "";

        if (isPh(word)) {
            String property = getProperty(word);
            List<String> args = getArgs(word);

            if (args.size() == 0) {
                // Player name
                if (property.equalsIgnoreCase("player")) {
                    wordWithPh = player.getDisplayName();
                }
                // Player location
                if (property.equalsIgnoreCase("%x%")) {
                    wordWithPh += player.getLocation().getBlockX();
                }
                if (property.equalsIgnoreCase("%y%")) {
                    wordWithPh += player.getLocation().getBlockX();
                }
                if (property.equalsIgnoreCase("%z%")) {
                    wordWithPh += player.getLocation().getBlockX();
                }
            } else if (args.size() == 1) {
                // Date and time
                if (property.equalsIgnoreCase("date")) {
                    ZoneId currentZone = ZoneId.systemDefault();
                    ZonedDateTime currentDateTime = ZonedDateTime.now(currentZone);

                    DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
                    wordWithPh = formatterBuilder.appendPattern(args.get(1)).toFormatter().format(currentDateTime);


                }
                if (property.equalsIgnoreCase("time")) {
                    ZoneId currentZone = ZoneId.systemDefault();
                    ZonedDateTime currentDateTime = ZonedDateTime.now(currentZone);

                    DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
                    wordWithPh = formatterBuilder.appendPattern(args.get(0)).toFormatter().format(currentDateTime);
                }
                // Economy
                if (config.isEconomyEnabled) {
                    if (property.equalsIgnoreCase("balance")) {
                        wordWithPh += economy.getBalance(player);
                    }
                }
                // Region
                if (config.isRegionEnabled) {
                    // Region
                    if (property.equalsIgnoreCase("region")) {
                        List<String> regions = region.getRegionList(player);

                        int i = 0;

                        try {
                            i = Integer.parseInt(args.get(1));
                        } catch (Exception e) {
                            messenger.sendToConsole("Error: Region placeholder, 1st argument is not an integer");
                        }

                        if (i > regions.size() - 1) {
                            wordWithPh = "";
                        } else {
                            wordWithPh = regions.get(i);
                        }
                    }
                }
                // Afk duration
                if (config.afkTimer != 0) {
                    if (property.equalsIgnoreCase("afktime")) {
                        customUpdater.getAfkTime(player.getDisplayName());
                    }

                    if (property.equalsIgnoreCase("afktimeleft")) {
                        customUpdater.getTime(player.getDisplayName());
                    }

                }
            } else if (args.size() == 2) {
                // Player statistics
                if (property.equals("mstat")){
                    String materialName = args.get(1);

                    Material material = Material.getMaterial(materialName);

                    wordWithPh += statistic.getPlayerStat(player, args.get(0), material);
                }

                if (property.equals("estat")) {
                    String entityName = args.get(1);

                    EntityType entityType = EntityType.valueOf(entityName);

                    wordWithPh += statistic.getPlayerStat(player, args.get(0), entityType);
                }
            }
        } else {
            return "{ERROR}";
        }

        return wordWithPh;
    }

    public String setTaPholder(String word) {
        String wordWithPh = "'";

        if (isPh(word)) {
            String property = getProperty(word);
            List<String> args = getArgs(word);

            Player target = Bukkit.getPlayer(args.get(0));

            if (target == null) {
                return "{ERROR}";
            }

            if (args.size() == 1) {
                // Economy
                while (property.equalsIgnoreCase("balance")) {
                    wordWithPh = "" + economy.getBalance(target);
                }
                // Other player's location
                if (property.equalsIgnoreCase("%x%")) {
                    wordWithPh = "" + target.getLocation().getBlockX();
                }
                if (property.equalsIgnoreCase("%y%")) {
                    wordWithPh = "" + target.getLocation().getBlockX();
                }
                if (property.equalsIgnoreCase("%z%")) {
                    wordWithPh = "" + target.getLocation().getBlockX();
                }
            }
            // Other player's region
            if (args.size() == 2) {
                // Region
                if (config.isRegionEnabled) {
                    if (property.equalsIgnoreCase("region")) {
                        wordWithPh = region.getRegionList(target).get(Integer.parseInt(args.get(1)));
                    }
                }
            }
        } else {
            return "{ERROR}";
        }

        return wordWithPh;
    }

    private String getProperty(String tag) {
        String property;

        if (!tag.contains("_")) {
            property = tag.substring(1, tag.length() - 1);
        } else {
            property = tag.substring(1, tag.indexOf("_"));
        }

        return property;
    }

    private List<String> getArgs(String tag) {
        List<String> args = new ArrayList<>();

        String argsString = tag + "_";

        while (argsString.contains("_")) {
            // Add argument to argsString
            String arg = argsString.substring(0, argsString.indexOf("_"));
            args.add(arg);
            argsString.replace(arg, "");
        }

        return args;
    }

    private boolean isPh(String word) {
        if (word.indexOf(0) == '%' && word.indexOf(word.length() - 1) == '%') {
            return true;
        }

        return false;
    }
}
