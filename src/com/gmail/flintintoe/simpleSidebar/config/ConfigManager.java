package com.gmail.flintintoe.simpleSidebar.config;

import com.gmail.flintintoe.simpleSidebar.message.MessageManager;
import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import com.sk89q.worldedit.event.platform.ConfigurationLoadEvent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    private MessageManager messageM;

    private FileConfiguration sidebarConfig;
    private FileConfiguration messageConfig;
    private FileConfiguration configConfig;

    private File sidebarFile;
    private File messageFile;
    private File configFile;

    // No need to save SimpleSidebar reference here
    public ConfigManager(SimpleSidebar plugin) {
        messageM = plugin.getMessageManager();

        // Setup config
        setupConfig(plugin);
        // Get plugin settings
        getPluginSettings();

        // TODO Update/ add settings when config file version updates
    }

    private void setupConfig(SimpleSidebar plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        // Load Files
        sidebarFile = new File(plugin.getDataFolder(), "sidebars.yml");
        messageFile = new File(plugin.getDataFolder(), "messages.yml");
        configFile = new File(plugin.getDataFolder(), "config.yml");
        // Load FileConfigs
        sidebarConfig = new YamlConfiguration();
        messageConfig = new YamlConfiguration();
        configConfig = new YamlConfiguration();
        // Copy if file does not exist
        if (!sidebarFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of sidebars.yml");
                sidebarFile.getParentFile().mkdirs();
                plugin.saveResource("sidebars.yml", false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!messageFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of messages.yml");
                messageFile.getParentFile().mkdirs();
                plugin.saveResource("messages.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!configFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of config.yml");
                configFile.getParentFile().mkdirs();
                plugin.saveResource("config.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Load files for use
        try {
            sidebarConfig.load(sidebarFile);
            messageConfig.load(messageFile);
            configConfig.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    // Automatic settings
    // TODO Use these booleans in case of lack of dependencies
    public boolean isEconomyEnabled;
    public boolean isRegionEnabled;
    public boolean isSidebarEnabled;

    public boolean containsAFKSbPlaceholders;

    public boolean updateWhenNeeded;
    public boolean updateWhenMovement;
    public boolean updateWhenMining;
    public boolean updateWhenPlacing;

    // Manual settings
    public boolean haveDefaultSb;
    public boolean haveAFKSb;

    public int duration;

    private void getPluginSettings() {
        haveDefaultSb = getBoolean(ConfigFile.config, "first_sidebar_is_default");
        haveAFKSb = getBoolean(ConfigFile.config, "last_sidebar_is_afk");

        duration = getValue(ConfigFile.config, "AFK_timer");

        updateWhenNeeded = getBoolean(ConfigFile.config, "update_sidebar_when_placeholder_update");
        updateWhenMovement = getBoolean(ConfigFile.config, "sidebar_update_when_player_movement");
        updateWhenMining = getBoolean(ConfigFile.config,"sidebar_update_when_player_mine");
        updateWhenPlacing = getBoolean(ConfigFile.config,"sidebar_update_when_block_place");
    }

    public String getString(ConfigFile configFile, String path) {
        if (configFile == ConfigFile.sidebars) {
            return sidebarConfig.getString(path);
        } else if (configFile == ConfigFile.messages) {
            return messageConfig.getString(path);
        } else if (configFile == ConfigFile.config) {
            return configConfig.getString(path);
        }

        return null;
    }

    public int getValue(ConfigFile configFile, String path) {
        if (configFile == ConfigFile.sidebars) {
            return sidebarConfig.getInt(path);
        } else if (configFile == ConfigFile.messages) {
            return messageConfig.getInt(path);
        } else if (configFile == ConfigFile.config) {
            return configConfig.getInt(path);
        }

        return -1;
    }

    public boolean getBoolean(ConfigFile configFile, String path) {
        if (configFile == ConfigFile.sidebars) {
            return sidebarConfig.getBoolean(path);
        } else if (configFile == ConfigFile.messages) {
            return messageConfig.getBoolean(path);
        } else if (configFile == ConfigFile.config) {
            return configConfig.getBoolean(path);
        }

        return false;
    }

    public List<String> getStrings(ConfigFile configFile, String path) {
        if (configFile == ConfigFile.sidebars) {
            return sidebarConfig.getStringList(path);
        } else if (configFile == ConfigFile.messages) {
            return messageConfig.getStringList(path);
        } else if (configFile == ConfigFile.config) {
            return configConfig.getStringList(path);
        }

        return null;
    }
}
