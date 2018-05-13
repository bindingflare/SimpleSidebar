package com.gmail.flintintoe.config;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {
    private SimpleSidebar plugin;
    private Messenger messenger;

    private FileConfiguration sidebarConfig;
    private FileConfiguration messageConfig;
    private FileConfiguration configConfig;
    private FileConfiguration placeholderConfig;

    private File pluginFolder;

    // Settings
    private boolean setOnLogin;
    private boolean adminBypass;

    private int afkTimer;
    private int updateTimer;

    private boolean allowAfkSet;
    private boolean afkPhUpdate;

    /**
     * Manages the configuration files used by this plugin.
     *
     * @since v0.8.0_RC1
     */
    public Config(SimpleSidebar plugin) {
        this.plugin = plugin;
        messenger = plugin.getMessenger();
    }

    public void setup() {
        pluginFolder = plugin.getDataFolder();

        // Check if pluginFolder exists
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
            messenger.send("Creating plugin folder...");
        }

        // Initialize file configs
        sidebarConfig = new YamlConfiguration();
        messageConfig = new YamlConfiguration();
        configConfig = new YamlConfiguration();
        placeholderConfig = new YamlConfiguration();

        // Load file configs
        loadConfigFiles();

        // Check config versions
        checkConfigFiles();

        // Get settings from config.yml
        loadConfig();
    }

    public void loadConfigFiles() {
        File sidebarFile = new File(pluginFolder, "sidebars.yml");
        File messageFile = new File(pluginFolder, "messages.yml");
        File configFile = new File(pluginFolder, "config.yml");
        File placeholderFile = new File(pluginFolder, "placeholders.yml");

        // Check if file exists
        if (!sidebarFile.exists()) {
            saveConfigFile(sidebarFile);
        }
        if (!messageFile.exists()) {
            saveConfigFile(messageFile);
        }
        if (!configFile.exists()) {
            saveConfigFile(configFile);
        }
        if (!placeholderFile.exists()) {
            saveConfigFile(placeholderFile);
        }

        // Load files
        try {
            sidebarConfig.load(sidebarFile);
            messageConfig.load(messageFile);
            configConfig.load(configFile);
            placeholderConfig.load(placeholderFile);
        } catch (Exception e) {
            messenger.sendError("Failed to load configuration files");
        }
    }

    public void loadConfig() {
        setOnLogin = getBoolean(ConfigFile.CONFIG, "set_on_login");
        adminBypass = getBoolean(ConfigFile.CONFIG, "admin_bypass");

        afkTimer = getValue(ConfigFile.CONFIG, "afk_timer");

        allowAfkSet = getBoolean(ConfigFile.CONFIG, "allow_change_afk");
        afkPhUpdate = getBoolean(ConfigFile.CONFIG, "update_sidebar_sync_afk");

        updateTimer = getValue(ConfigFile.CONFIG, "update_timer");
        // Check if set timer is out of bounds
        if (updateTimer <= 0) {
            messenger.sendError("Cannot set set timer to less than 1 second. Using default value 1 second instead");
            updateTimer = 1;
        }
    }

    private void checkConfigFiles() {
        // Using rudimentary CONFIG checking for now
        if (configConfig.getDouble("config_version") != 1.4) {
            messenger.send("Old config.yml detected");
        }
        if (sidebarConfig.getDouble("sidebars_version") != 1.1) {
            messenger.send("Old sidebars.yml detected");
        }
        if (messageConfig.getDouble("messages_version") != 1.0) {
            messenger.send("Old messages.yml detected");
        }
        if (placeholderConfig.getDouble("placeholders_version") != 1.0) {
            messenger.send("Old placeholders.yml detected");
        }
    }

    private void saveConfigFile(File file) {
        try {
            messenger.send("Creating a copy of " + file.getName());
            plugin.saveResource(file.getName(), false);
        } catch (Exception e) {
            messenger.send("Error: Error while saving " + file.getName());
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

    private boolean getBoolean(ConfigFile configFile, String path) {
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
