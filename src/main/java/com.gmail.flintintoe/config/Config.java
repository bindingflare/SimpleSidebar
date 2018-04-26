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
        File sidebarFile;
        File messageFile;
        File configFile;

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
                messenger.sendToConsole("Creating a copy of SIDEBARS.yml");
                sidebarFile.getParentFile().mkdirs();
                plugin.saveResource("sidebars.yml", false);

            } catch (Exception e) {
                return false;
            }
        }
        if (!messageFile.exists()) {
            try {
                messenger.sendToConsole("Creating a copy of MESSAGES.yml");
                messageFile.getParentFile().mkdirs();
                plugin.saveResource("messages.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!configFile.exists()) {
            try {
                messenger.sendToConsole("Creating a copy of CONFIG.yml");
                configFile.getParentFile().mkdirs();
                plugin.saveResource("config.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
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
            messenger.sendToConsole("You are currently using an old CONFIG.yml");
        }
        if (sidebarConfig.getDouble("sidebars_version") != 1.1) {
            messenger.sendToConsole("You are currently using an old SIDEBARS.yml");
        }
        if (messageConfig.getDouble("messages_version") != 1.0) {
            messenger.sendToConsole("You are currently using an old MESSAGES.yml");
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
