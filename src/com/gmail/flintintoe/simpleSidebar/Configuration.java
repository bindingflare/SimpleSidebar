package com.gmail.flintintoe.simpleSidebar;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration {
    private static FileConfiguration config;

    private static File sidebarFile;
    private static File messageFile;

    public static void setupConfig(SimpleSidebar plugin) {
        config = plugin.getConfig();

        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        sidebarFile = new File(plugin.getDataFolder(), "sidebar.yaml");
        messageFile = new File(plugin.getDataFolder(), "messages.yaml");

        // Copy if file does not exist
        if(!sidebarFile.exists()) {
            try {
                plugin.sendToConsole("Creating a copy of sidebar.yaml");
                config.save(sidebarFile);
            } catch (Exception e ) {
                e.printStackTrace();
            }
        }
        if(!messageFile.exists()) {
            try {
                plugin.sendToConsole("Creating a copy of messages.yaml");
                config.save(messageFile);
            } catch (Exception e ) {
                e.printStackTrace();
            }
        }

        // Load files for use
        try {
            config.load(sidebarFile);
            config.load(messageFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String getEntry(ConfigurationFile configFile, String path) {
        return config.getString(path);

//        if (configFile == ConfigurationFile.config) {
//            return config.getString(path);
//        }
//        else if (configFile == ConfigurationFile.sidebar) {
//
//        }
//        else if (configFile == ConfigurationFile.messages) {
//
//        }
    }

    public static List<String> getEntries (ConfigurationFile configFile, String path) {
        return config.getStringList(path);
    }
}
