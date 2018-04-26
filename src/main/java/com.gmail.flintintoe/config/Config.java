package com.gmail.flintintoe.config;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {
    private Messenger messageM;

    private FileConfiguration sidebarConfig;
    private FileConfiguration messageConfig;
    private FileConfiguration configConfig;

    // Automatic settings
    public boolean isEconomyEnabled = false;
    public boolean isRegionEnabled = false;

    // Settings
    private boolean enablePlugin;
    private boolean setOnLogin;

    private int afkTimer;

    private boolean allowAfkSet;
    private boolean afkPhUpdate;

    private boolean updatePhAsync;
    private boolean updatePhSync;

    private int updateTimer;

    // No need to save SimpleSidebar reference here
    public Config(SimpleSidebar plugin) {
        messageM = plugin.getMessenger();
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


    private void loadConfig() {
        enablePlugin = getBoolean(ConfigFile.config, "plugin_enabled");
        setOnLogin = getBoolean(ConfigFile.config, "set_on_login");

        afkTimer = getValue(ConfigFile.config, "afk_timer");

        allowAfkSet = getBoolean(ConfigFile.config, "allow_change_afk");
        afkPhUpdate = getBoolean(ConfigFile.config, "afk_placeholder_update");

        updatePhAsync = getBoolean(ConfigFile.config, "update_placeholder_async");
        updatePhSync = getBoolean(ConfigFile.config, "update_placeholder_sync");

        updateTimer = getValue(ConfigFile.config, "sidebar_update_timer");
    }

    private void checkConfig() {
        // TODO Check config versions
    }

    public boolean paramExists(ConfigFile configFile, String path) {
        boolean exists = false;

        if (configFile == ConfigFile.sidebars) {
            exists = sidebarConfig.isConfigurationSection(path);
        } else if (configFile == ConfigFile.messages) {
            exists = messageConfig.isConfigurationSection(path);
        } else if (configFile == ConfigFile.config) {
            exists = configConfig.isConfigurationSection(path);
        }

        return exists;
    }

    private int getValue(ConfigFile configFile, String path) {
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

    public boolean isEnablePlugin() {
        return enablePlugin;
    }

    public void setEnablePlugin(boolean enablePlugin) {
        this.enablePlugin = enablePlugin;
    }

    public boolean isSetOnLogin() {
        return setOnLogin;
    }

    public void setSetOnLogin(boolean setOnLogin) {
        this.setOnLogin = setOnLogin;
    }

    public int getAfkTimer() {
        return afkTimer;
    }

    public void setAfkTimer(int afkTimer) {
        this.afkTimer = afkTimer;
    }

    public boolean isAllowAfkSet() {
        return allowAfkSet;
    }

    public void setAllowAfkSet(boolean allowAfkSet) {
        this.allowAfkSet = allowAfkSet;
    }

    public boolean isAfkPhUpdate() {
        return afkPhUpdate;
    }

    public void setAfkPhUpdate(boolean afkPhUpdate) {
        this.afkPhUpdate = afkPhUpdate;
    }

    public boolean isUpdatePhAsync() {
        return updatePhAsync;
    }

    public void setUpdatePhAsync(boolean updatePhAsync) {
        this.updatePhAsync = updatePhAsync;
    }

    public boolean isUpdatePhSync() {
        return updatePhSync;
    }

    public void setUpdatePhSync(boolean updatePhSync) {
        this.updatePhSync = updatePhSync;
    }

    public int getUpdateTimer() {
        return updateTimer;
    }

    public void setUpdateTimer(int updateTimer) {
        this.updateTimer = updateTimer;
    }
}
