package com.gmail.flintintoe.playerproperty;

import com.gmail.flintintoe.SimpleSidebar;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerPermission {
    private Permission permission;

    private boolean isEnabled = false;

    public boolean setup(SimpleSidebar plugin) {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);

        // Check if a permissions plugin exists
        if (rsp == null) {
            return false;
        } else {
            permission = rsp.getProvider();
        }

        isEnabled = true;
        return true;
    }

    public Permission getPermission() {
        return permission;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
