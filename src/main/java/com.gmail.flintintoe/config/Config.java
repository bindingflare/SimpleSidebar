package com.gmail.flintintoe.config;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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
                messenger.sendToConsole("Creating a copy of sidebars.yml");
                sidebarFile.getParentFile().mkdirs();
                plugin.saveResource("src/sidebars.yml", false);

            } catch (Exception e) {
                return false;
            }
        }
        if (!messageFile.exists()) {
            try {
                messenger.sendToConsole("Creating a copy of messages.yml");
                messageFile.getParentFile().mkdirs();
                plugin.saveResource("src/messages.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!configFile.exists()) {
            try {
                messenger.sendToConsole("Creating a copy of config.yml");
                configFile.getParentFile().mkdirs();
                plugin.saveResource("src/config.yml", false);
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

        return getBoolean(ConfigFile.config, "plugin_enabled");
    }


    public void loadConfig() {
        setOnLogin = getBoolean(ConfigFile.config, "set_on_login");

        afkTimer = getValue(ConfigFile.config, "afk_timer");

        allowAfkSet = getBoolean(ConfigFile.config, "allow_change_afk");
        afkPhUpdate = getBoolean(ConfigFile.config, "afk_placeholder_update");

        updatePhAsync = getBoolean(ConfigFile.config, "update_placeholder_async");
        updatePhSync = getBoolean(ConfigFile.config, "update_placeholder_sync");

        updateTimer = getValue(ConfigFile.config, "sidebar_update_timer");
    }

    public void checkConfig(SimpleSidebar plugin) {
        // Using rudimentary config checking for now
        if (!getString(ConfigFile.config, "config_version").equals("1.2.0")) {
            messenger.sendToConsole("You are currently using an old config.yml");
            messenger.sendToConsole("Creating a copy of the new config.yml under the name newconfig.yml");
            try {
                configConfig.save(new File(plugin.getDataFolder(), "src/newconfig.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!getString(ConfigFile.sidebars, "sidebars_version").equals("1.1.0")) {
            messenger.sendToConsole("You are currently using an old sidebars.yml");
            messenger.sendToConsole("Creating a copy of the new config.yml under the name newsidebarsyml");
            try {
                configConfig.save(new File(plugin.getDataFolder(), "src/newsidebars.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!getString(ConfigFile.messages, "messages_version").equals("1.0.0")) {
            messenger.sendToConsole("You are currently using an old messages.yml");
            messenger.sendToConsole("Creating a copy of the new config.yml under the name newmessages.yml");
            try {
                configConfig.save(new File(plugin.getDataFolder(), "src/newmessages.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private boolean getBoolean(ConfigFile configFile, String path) {
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
