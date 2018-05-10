package com.gmail.flintintoe.playerproperty;

import com.gmail.flintintoe.SimpleSidebar;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerEconomy {
    private Economy economy;

    private boolean isEnabled = false;

    public boolean setup(SimpleSidebar plugin) {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        // Check if an economy plugin exists
        if (rsp == null) {
            return false;
        } else {
            economy = rsp.getProvider();
        }

        isEnabled = true;
        return true;
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    // WARNING Possible error with new players when using these methods

//    // Special return: Returns the new balance of the player
//    public double deductBalance(Player player, double amount) {
//        double newBalance = getBalance(player) - amount;
//
//        economy.withdrawPlayer(player, amount);
//
//        return newBalance;
//    }
//
//    // TODO Check return of depositPlayer when error happens
//    // Special return: Returns the new balance of the player
//    public double addBalance(Player player, double amount) {
//        double newBalance = getBalance(player) + amount;
//
//        economy.depositPlayer(player, amount);
//
//        return newBalance;
//    }
}