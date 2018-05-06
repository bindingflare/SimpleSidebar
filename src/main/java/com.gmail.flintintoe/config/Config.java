package com.gmail.flintintoe.config;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {
    private Messenger messenger;

    private FileConfiguration sidebarConfig;
    private FileConfiguration messageConfig;
    private FileConfiguration configConfig;

    private File dataFolder;

    // Automatic settings
    private boolean isEconomyEnabled = false;
    private boolean isRegionEnabled = false;

    // Settings
    private boolean setOnLogin;
    private boolean adminBypass;

    private int afkTimer;
    private int updateTimer;

    private boolean allowAfkSet;
    private boolean afkPhUpdate;

    public Config(SimpleSidebar plugin) {
        messenger = plugin.getMessenger();
    }

    public boolean setupConfig(SimpleSidebar plugin) {
        if (!plugin.getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            plugin.getDataFolder().mkdirs();
        }

        dataFolder = plugin.getDataFolder();

        // Load files
        File sidebarFile  = new File(dataFolder, "sidebars.yml");
        File messageFile = new File(dataFolder, "messages.yml");
        File configFile= new File(dataFolder, "config.yml");

        // Load FileConfigs
        sidebarConfig = new YamlConfiguration();
        messageConfig = new YamlConfiguration();
        configConfig = new YamlConfiguration();

        // Copy if file does not exist
        if (!sidebarFile.exists()) {
            saveConfig(plugin, "sidebar", sidebarFile);
        }
        if (!messageFile.exists()) {
            saveConfig(plugin, "messages", messageFile);
        }
        if (!configFile.exists()) {
            saveConfig(plugin, "config", configFile);
        }
        // Load files for use
        try {
            sidebarConfig.load(sidebarFile);
            messageConfig.load(messageFile);
            configConfig.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return getBoolean(ConfigFile.CONFIG, "plugin_enabled");
    }

    public void loadConfig() {
        setOnLogin = getBoolean(ConfigFile.CONFIG, "set_on_login");
        adminBypass = getBoolean(ConfigFile.CONFIG, "admin_bypass");

        afkTimer = getValue(ConfigFile.CONFIG, "afk_timer");

        allowAfkSet = getBoolean(ConfigFile.CONFIG, "allow_change_afk");
        afkPhUpdate = getBoolean(ConfigFile.CONFIG, "update_sidebar_sync_afk");

        updateTimer = getValue(ConfigFile.CONFIG, "update_timer");
        // Check if updateSidebarIndex timer is out of bounds
        if (updateTimer <= 0) {
            messenger.sendErrorMessage("Cannot set updateSidebarIndex timer to less than 1 second. Using default value 1 second instead");
            updateTimer = 1;
        }
    }

    public void reloadConfig() {
        try {
            sidebarConfig.load(new File(dataFolder, "sidebars.yml"));
            messageConfig.load(new File(dataFolder, "messages.yml"));
            configConfig.load(new File(dataFolder, "config.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadConfig();
    }

    public void checkConfig(SimpleSidebar plugin) {
        // Using rudimentary CONFIG checking for now
        if (configConfig.getDouble("config_version") != 1.3) {
            messenger.sendToConsole("Old config.yml detected");
        }
        if (sidebarConfig.getDouble("sidebars_version") != 1.1) {
            messenger.sendToConsole("Old sidebars.yml detected");
        }
        if (messageConfig.getDouble("messages_version") != 1.0) {
            messenger.sendToConsole("Old messages.yml detected");
        }
    }

    private void saveConfig(SimpleSidebar plugin, String fileName, File file) {
        try {
            messenger.sendToConsole("Creating a copy of " + file.getName());
            plugin.saveResource(file.getName(), false);
        } catch (Exception e) {
            messenger.sendToConsole("Error: Error while saving " + fileName + ".yml");
        }
    }

    public boolean sectionExists(ConfigFile configFile, String path) {
        boolean exists = false;

        if (configFile == ConfigFile.SIDEBARS) {
            exists = sidebarConfig.isConfigurationSection(path);
        } else if (configFile == ConfigFile.MESSAGES) {
            exists = messageConfig.isConfigurationSection(path);
        } else if (configFile == ConfigFile.CONFIG) {
            exists = configConfig.isConfigurationSection(path);
        }

        return exists;
    }

    public boolean listExists(ConfigFile configFile, String path) {
        boolean exists = false;

        if (configFile == ConfigFile.SIDEBARS) {
            exists = sidebarConfig.isList(path);
        } else if (configFile == ConfigFile.MESSAGES) {
            exists = messageConfig.isList(path);
        } else if (configFile == ConfigFile.CONFIG) {
            exists = configConfig.isList(path);
        }

        return exists;
    }

    private int getValue(ConfigFile configFile, String path) {
        if (configFile == ConfigFile.SIDEBARS) {
            return sidebarConfig.getInt(path);
        } else if (configFile == ConfigFile.MESSAGES) {
            return messageConfig.getInt(path);
        } else if (configFile == ConfigFile.CONFIG) {
            return configConfig.getInt(path);
        }

        return -1;
    }

    public boolean getBoolean(ConfigFile configFile, String path) {
        if (configFile == ConfigFile.SIDEBARS) {
            return sidebarConfig.getBoolean(path);
        } else if (configFile == ConfigFile.MESSAGES) {
            return messageConfig.getBoolean(path);
        } else if (configFile == ConfigFile.CONFIG) {
            return configConfig.getBoolean(path);
        }

        return false;
    }

    public String getString(ConfigFile configFile, String path) {
        if (configFile == ConfigFile.SIDEBARS) {
            return sidebarConfig.getString(path);
        } else if (configFile == ConfigFile.MESSAGES) {
            return messageConfig.getString(path);
        } else if (configFile == ConfigFile.CONFIG) {
            return configConfig.getString(path);
        }

        return null;
    }

    public List<String> getStrings(ConfigFile configFile, String path) {
        if (configFile == ConfigFile.SIDEBARS) {
            return sidebarConfig.getStringList(path);
        } else if (configFile == ConfigFile.MESSAGES) {
            return messageConfig.getStringList(path);
        } else if (configFile == ConfigFile.CONFIG) {
            return configConfig.getStringList(path);
        }

        return null;
    }

    // Getters and setters
    public boolean isEconomyEnabled() {
        return isEconomyEnabled;
    }

    public void setEconomyEnabled(boolean economyEnabled) {
        isEconomyEnabled = economyEnabled;
    }

    public boolean isRegionEnabled() {
        return isRegionEnabled;
    }

    public void setRegionEnabled(boolean regionEnabled) {
        isRegionEnabled = regionEnabled;
    }

    // Getters only
    public boolean isSetOnLogin() {
        return setOnLogin;
    }

    public boolean isAdminBypass() {
        return adminBypass;
    }

    public int getAfkTimer() {
        return afkTimer;
    }

    public boolean isAllowAfkSet() {
        return allowAfkSet;
    }

    public boolean isAfkPhUpdate() {
        return afkPhUpdate;
    }

    public int getUpdateTimer() {
        return updateTimer;
    }
}
