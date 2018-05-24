package com.gmail.flintintoe;

import com.gmail.flintintoe.message.Messenger;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Manages the dependencies of this plugin.
 *
 * @since v0.8.0_pre1
 */
class Dependency {
    private SimpleSidebar plugin;

    private Messenger messenger;

    Dependency(SimpleSidebar plugin) {
        this.plugin = plugin;
        messenger = plugin.getMessenger();
    }

    boolean checkVault() {
        return plugin.getServer().getPluginManager().getPlugin("Vault") != null;
    }

    WorldGuardPlugin getWorldGuard() {
        Plugin wGPlugin = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard might not be loaded
        if (!(wGPlugin instanceof WorldGuardPlugin)) {
            messenger.send("WorldGuard and WorldEdit dependency not found. Region features will be disabled");
            return null;
        }

        return (WorldGuardPlugin) wGPlugin;
    }

    Economy getEconomy() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        Economy economy = rsp.getProvider();

        if (economy == null) {
            messenger.sendError("Could not find an economy plugin!");
            plugin.forceDisable();
        }

        return economy;
    }

    Permission getPermission() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        Permission permission = rsp.getProvider();

        if (permission == null) {
            messenger.sendError("Could not find a permissions plugin!");
            plugin.forceDisable();
        }

        return permission;
    }
}
