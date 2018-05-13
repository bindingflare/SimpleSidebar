package com.gmail.flintintoe;

import com.gmail.flintintoe.command.AdminCommand;
import com.gmail.flintintoe.command.CommandOutput;
import com.gmail.flintintoe.command.PlayerCommand;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.event.PlayerEvent;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.SidebarManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of this plugin.
 *
 * MIT License
 *
 * Copyright (c) 2018 Flarin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author xFlarinFlint (Flarin)
 * @version v0.8.0_RC1
 */
public class SimpleSidebar extends JavaPlugin {

    private Messenger messenger;
    private Config config;
    private CommandOutput output;

    private Economy economy;
    private Permission permission;
    private WorldGuardPlugin wGPlugin;

    private SidebarManager sbManager;

    /**
     * Method called by the Spigot server to initialize this plugin.
     *
     * @see    JavaPlugin
     */
    @Override
    public void onEnable() {
        messenger = new Messenger();

        config = new Config(this);
        config.setup();

        Dependency dependency = new Dependency(this);

        // Disable plugin if dependency Vault not found
        if (!dependency.checkVault()) {
            messenger.sendError("This plugin requires Vault");
            messenger.sendError("Use the link: https://www.spigotmc.org/resources/vault.34315/");
            forceDisable();
        }

        // Get dependencies
        economy = dependency.getEconomy();
        permission = dependency.getPermission();
        wGPlugin = dependency.getWorldGuard();

        sbManager = new SidebarManager(this);
        sbManager.setup();

        // Commands (Including command output manager)
        output = new CommandOutput(this);
        getCommand("sidebar").setExecutor(new PlayerCommand(this));
        getCommand("adminsidebar").setExecutor((new AdminCommand(this)));

        // Events
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
    }

    /**
     * Forcefully disables the plugin.
     */
    void forceDisable() {
        messenger.send("Force disabling plugin...");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    /**
     * Method called when the plugin is being disabled.
     *
     * @see    JavaPlugin
     */
    @Override
    public void onDisable() {
        getSidebarManager().getTracker().cancel();
    }

    /**
     * Gets an instance of Config.
     *
     * @return An instance of Config
     * @see    Config
     */
    public Config getConfigManager() {
        return config;
    }

    /**
     * Gets an instance of Messenger.
     *
     * @return Messenger
     * @see    Messenger
     */
    public Messenger getMessenger() {
        return messenger;
    }

    /**
     * Gets an instance of SidebarManager.
     *
     * @return An instance of SidebarManager
     * @see    SidebarManager
     */
    public SidebarManager getSidebarManager() {
        return sbManager;
    }

    /**
     * Gets an instance of CommandOutput.
     *
     * @return An instance of CommandOutput
     * @see    CommandOutput
     */
    public CommandOutput getCommandOutput() {
        return output;
    }

    /**
     * Gets an instance of Messenget.
     *
     * @return An instance of Economy
     * @see    Dependency
     * @see    Economy
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Gets an instance of Permission.
     *
     * @return An instance of Permission
     * @see    Dependency
     * @see    Permission
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Gets an instance of WorldGuardPlugin.
     *
     * @return An instance of WorldGuardPlugin
     * @see    Dependency
     * @see    WorldGuardPlugin
     */
    public WorldGuardPlugin getwGPlugin() {
        return wGPlugin;
    }
}