package com.gmail.flintintoe.playerproperty;

import com.gmail.flintintoe.SimpleSidebar;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerEconomy {
    private Economy economy;

    public boolean setupEconomy(SimpleSidebar plugin) {

        // Check if Vault exists
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            // Check if an economy plugin exists
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

            if (rsp == null) {
                return false;
            } else {
                // Setup economy
                economy = rsp.getProvider();
            }
        }
        plugin.getPluginConfig().setEconomyEnabled(true);
        return true;
    }

    public double getBalance(Player player) {

        double balance = 0D;

        // Check if the target has been on the server at least once
        // If not, send 0
        if (player.hasPlayedBefore()) {
            balance = economy.getBalance(player);
        }
        return balance;
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