package com.gmail.flintintoe.config;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class PluginConfig {
    private Messenger messenger;

    private FileConfiguration sidebarConfig;
    private FileConfiguration messageConfig;
    private FileConfiguration configConfig;
    private FileConfiguration placeholderConfig;

    private File sidebarFile;
    private File messageFile;
    private File configFile;
    private File placeholderFile;

    // Settings
    private boolean setOnLogin;
    private boolean adminBypass;

    private int afkTimer;
    private int updateTimer;

    private boolean allowAfkSet;
    private boolean afkPhUpdate;

    public PluginConfig(SimpleSidebar plugin) {
        messenger = plugin.getMessenger();
    }

    public boolean setup(SimpleSidebar plugin) {
        File pluginFolder = plugin.getDataFolder();

        if (!(pluginFolder.exists() && pluginFolder.mkdirs())) {
            messenger.sendFatalError("Failed to create plugin folder!");
        }
        // Load files
        sidebarFile = new File(pluginFolder, "sidebars.yml");
        messageFile = new File(pluginFolder, "messages.yml");
        configFile = new File(pluginFolder, "config.yml");
        placeholderFile = new File(pluginFolder, "placeholders.yml");

        // Load FileConfigs
        sidebarConfig = new YamlConfiguration();
        messageConfig = new YamlConfiguration();
        configConfig = new YamlConfiguration();
        placeholderConfig = new YamlConfiguration();

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
        loadFiles();
        loadConfig();
        return getBoolean(ConfigFile.CONFIG, "plugin_enabled");
    }

    public void loadFiles() {
        try {
            sidebarConfig.load(sidebarFile);
            messageConfig.load(messageFile);
            configConfig.load(configFile);
            placeholderConfig.load(placeholderFile);
        } catch (Exception e) {
        }
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
            messenger.sendError("Cannot set updateSidebarIndex timer to less than 1 second. Using default value 1 second instead");
            updateTimer = 1;
        }
    }

    public void checkConfig(SimpleSidebar plugin) {
        // Using rudimentary CONFIG checking for now
        if (configConfig.getDouble("config_version") != 1.3) {
            messenger.send("Old config.yml detected");
        }
        if (sidebarConfig.getDouble("sidebars_version") != 1.1) {
            messenger.send("Old sidebars.yml detected");
        }
        if (messageConfig.getDouble("messages_version") != 1.0) {
            messenger.send("Old messages.yml detected");
        }
        if (placeholderConfig.getDouble("placeholders_version") != 1.0) {
            messenger.send("Old messages.yml detected");
        }
    }

    private void saveConfig(SimpleSidebar plugin, String fileName, File file) {
        try {
            messenger.send("Creating a copy of " + file.getName());
            plugin.saveResource(file.getName(), false);
        } catch (Exception e) {
            messenger.send("Error: Error while saving " + fileName + ".yml");
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
        } else if (configFile == ConfigFile.PLACEHOLDERS) {
            return configConfig.isConfigurationSection(path);
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
        } else if (configFile == ConfigFile.PLACEHOLDERS) {
            return configConfig.isList(path);
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
        } else if (configFile == ConfigFile.PLACEHOLDERS) {
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
        } else if (configFile == ConfigFile.PLACEHOLDERS) {
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
        } else if (configFile == ConfigFile.PLACEHOLDERS) {
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
        } else if (configFile == ConfigFile.PLACEHOLDERS) {
            return configConfig.getStringList(path);
        }

        return null;
    }

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
