package com.gmail.flintintoe.simpleSidebar.economy;

import com.gmail.flintintoe.simpleSidebar.SimpleSidebar;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ServerEconomy {
    private static Economy economy;

    public static boolean setupEconomy(SimpleSidebar plugin) {
        // Check if Vault exists
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        // Check if an economy plugin exists
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }
        // Setup economy
        economy = rsp.getProvider();

        return economy != null;
    }

    public static double getBalance(Player player) {

        double balance = 0;

        // Check if the target has been on the server at least once
        // If not, send 0
        if (player.hasPlayedBefore()) {
            balance = economy.getBalance(player);
        }
        return balance;
    }
    // WARNING Possible error with new players when using these methods

    // Special return: Returns the new balance of the player
    public static double deductBalance(Player player, double amount) {
        double newBalance = getBalance(player) - amount;

        economy.withdrawPlayer(player, amount);

        return newBalance;
    }

    // Special return: Returns the new balance of the player
    public static double addBalance(Player player, double amount) {
        double newBalance = getBalance(player) + amount;

        economy.depositPlayer(player, amount);

        return newBalance;
    }
}