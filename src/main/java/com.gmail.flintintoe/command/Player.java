package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles the command "/sidebar".
 *
 * @since v0.8.0_pre1
 */
public class Player implements CommandExecutor {
    private Messenger messenger;
    private Output output;

    private Permission permission;

    public Player(SimpleSidebar plugin) {
        messenger = plugin.getMessenger();
        output = plugin.getCommandOutput();

        permission = plugin.getPermission();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {

            if (args.length == 0) {
                if (permission.has(sender, "simplesidebar.see")) {
                    output.ofInfo(sender, (org.bukkit.entity.Player) sender);
                } else {
                    messenger.send(sender, 0);
                }
                return true;

            } else if (args.length == 1) {
                if (args[0].equals("remove")) {
                    if (permission.has(sender, "simplesidebar.use")) {
                        output.ofRemove(sender);
                        return true;
                    } else {
                        messenger.send(sender, 0);
                        return true;
                    }
                }

                if (permission.has(sender, "simplesidebar.use")) {
                    output.ofQuery(sender, (org.bukkit.entity.Player) sender, args[0]);
                } else {
                    messenger.send(sender, 1);
                }
                return true;

            } else {
                messenger.send(sender, 3);
                return true;
            }
        } else {
            messenger.send(sender, 0);
            return true;
        }
    }
}
