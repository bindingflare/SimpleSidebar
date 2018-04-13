package com.gmail.flintintoe.simpleSidebar.config;

import com.gmail.flintintoe.simpleSidebar.MessageManager;
import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    private MessageManager messageM;

    private FileConfiguration config;

    private File sidebarFile;
    private File messageFile;
    private File configFile;

    // No need to save SimpleSidebar reference here
    public ConfigManager(SimpleSidebar plugin) {
        config = plugin.getConfig();
        messageM = plugin.getMessageManager();

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        sidebarFile = new File(plugin.getDataFolder(), "sidebar.yml");
        messageFile = new File(plugin.getDataFolder(), "messages.yml");
        configFile = new File(plugin.getDataFolder(), "config.yml");


        // Copy if file does not exist
        if (!sidebarFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of sidebar.yml");
                config.save(sidebarFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!messageFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of messages.yml");
                config.save(messageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!configFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of config.yml");
                config.save(configFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Load files for use
        try {
            config.load(sidebarFile);
            config.load(messageFile);
            config.load(configFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getEntry(ConfigFile configFile, String path) {
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

    public int getValue(ConfigFile configFile, String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(ConfigFile configFile, String path) {
        return config.getBoolean(path);
    }

    public List<String> getEntries(ConfigFile configFile, String path) {
        return config.getStringList(path);
    }
}
