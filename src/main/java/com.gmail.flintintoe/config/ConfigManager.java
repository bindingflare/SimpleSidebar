package com.gmail.flintintoe.config;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.MessageManager;
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

    // Automatic settings
    public boolean isEconomyEnabled = true;
    public boolean isRegionEnabled = true;

    // TODO Change these into getters and setters in the future
    public boolean enablePlugin;
    public boolean setOnLogin;

    public int afkTimer;

    public boolean allowChangeAfk;
    public boolean afkPlaceholderUpdate;

    public boolean updatePlaceholderAsync;
    public boolean updatePlaceholerSync;

    public int updateTimer;

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
        File sidebarFile;
        File messageFile;
        File configFile;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        // Load Files
        sidebarFile = new File(plugin.getDataFolder(), "src/sidebars.yml");
        messageFile = new File(plugin.getDataFolder(), "src/messages.yml");
        configFile = new File(plugin.getDataFolder(), "src/config.yml");
        // Load FileConfigs
        sidebarConfig = new YamlConfiguration();
        messageConfig = new YamlConfiguration();
        configConfig = new YamlConfiguration();
        // Copy if file does not exist
        if (!sidebarFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of sidebars.yml");
                sidebarFile.getParentFile().mkdirs();
                plugin.saveResource("src/sidebars.yml", false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!messageFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of messages.yml");
                messageFile.getParentFile().mkdirs();
                plugin.saveResource("src/messages.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!configFile.exists()) {
            try {
                messageM.sendToConsole("Creating a copy of config.yml");
                configFile.getParentFile().mkdirs();
                plugin.saveResource("src/config.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Load files for use
        try {
            sidebarConfig.load(sidebarFile);
            messageConfig.load(messageFile);
            configConfig.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPluginSettings() {
        enablePlugin = getBoolean(ConfigFile.config, "plugin_enabled");
        setOnLogin = getBoolean(ConfigFile.config, "set_on_login");

        afkTimer = getValue(ConfigFile.config, "afk_timer");

        allowChangeAfk = getBoolean(ConfigFile.config, "allow_change_afk");
        afkPlaceholderUpdate = getBoolean(ConfigFile.config, "afk_placeholder_update");

        updatePlaceholderAsync = getBoolean(ConfigFile.config, "update_placeholder_async");
        updatePlaceholerSync = getBoolean(ConfigFile.config, "update_placeholder_sync");

        updateTimer = getValue(ConfigFile.config, "sidebar_update_timer");
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
