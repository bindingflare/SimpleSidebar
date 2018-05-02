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

    // Automatic settings
    private boolean isEconomyEnabled = false;
    private boolean isRegionEnabled = false;

    // Settings
    private boolean setOnLogin;

    private int afkTimer;

    private boolean allowAfkSet;
    private boolean afkPhUpdate;

    private boolean updatePhAsync;
    private boolean updatePhSync;

    private int updateTimer;

    public Config(SimpleSidebar plugin) {
        messenger = plugin.getMessenger();
    }

    public boolean setupConfig(SimpleSidebar plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // Load files
        File sidebarFile  = new File(plugin.getDataFolder(), "sidebars.yml");
        File messageFile = new File(plugin.getDataFolder(), "messages.yml");
        File configFile= new File(plugin.getDataFolder(), "config.yml");

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

        afkTimer = getValue(ConfigFile.CONFIG, "afk_timer");

        allowAfkSet = getBoolean(ConfigFile.CONFIG, "allow_change_afk");
        afkPhUpdate = getBoolean(ConfigFile.CONFIG, "afk_placeholder_update");

        updatePhAsync = getBoolean(ConfigFile.CONFIG, "update_placeholder_async");
        updatePhSync = getBoolean(ConfigFile.CONFIG, "update_placeholder_sync");

        updateTimer = getValue(ConfigFile.CONFIG, "sidebar_update_timer");
    }

    public void checkConfig(SimpleSidebar plugin) {
        // Using rudimentary CONFIG checking for now
        if (configConfig.getDouble("config_version") != 1.2) {
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

    private boolean getBoolean(ConfigFile configFile, String path) {
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

    public boolean isSetOnLogin() {
        return setOnLogin;
    }

//    public void setSetOnLogin(boolean setOnLogin) {
//        this.setOnLogin = setOnLogin;
//    }

    public int getAfkTimer() {
        return afkTimer;
    }

//    public void setAfkTimer(int afkTimer) {
//        this.afkTimer = afkTimer;
//    }

    public boolean isAllowAfkSet() {
        return allowAfkSet;
    }

//    public void setAllowAfkSet(boolean allowAfkSet) {
//        this.allowAfkSet = allowAfkSet;
//    }

    public boolean isAfkPhUpdate() {
        return afkPhUpdate;
    }

//    public void setAfkPhUpdate(boolean afkPhUpdate) {
//        this.afkPhUpdate = afkPhUpdate;
//    }

    public boolean isUpdatePhAsync() {
        return updatePhAsync;
    }

//    public void setUpdatePhAsync(boolean updatePhAsync) {
//        this.updatePhAsync = updatePhAsync;
//    }

    public boolean isUpdatePhSync() {
        return updatePhSync;
    }

//    public void setUpdatePhSync(boolean updatePhSync) {
//        this.updatePhSync = updatePhSync;
//    }

    public int getUpdateTimer() {
        return updateTimer;
    }

//    public void setUpdateTimer(int updateTimer) {
//        this.updateTimer = updateTimer;
//    }
}
