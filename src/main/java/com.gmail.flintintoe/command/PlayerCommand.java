package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.message.Messenger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the command "/sidebar".
 *
 * @since v0.8.0_RC1
 */
public class PlayerCommand implements CommandExecutor {
    private Messenger messenger;
    private CommandOutput output;

    private Permission permission;

    public PlayerCommand(SimpleSidebar plugin) {
        messenger = plugin.getMessenger();
        output = plugin.getCommandOutput();

        permission = plugin.getPermission();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {

            if (args.length == 0) {
                if (permission.has(sender, "simplesidebar.see")) {
                    output.ofInfo(sender, (Player) sender);
                } else {
                    messenger.sendError(sender, "simplesidebar.see permission required");
                }
                return true;

            } else if (args.length == 1) {
                if (args[0].equals("remove")) {
                    if (permission.has(sender, "simplesidebar.use")) {
                        output.ofRemove(sender);
                        return true;
                    } else {
                        messenger.sendError(sender, "simplesidebar.see permission required");
                        return true;
                    }
                }

                if (permission.has(sender, "simplesidebar.use")) {
                    output.ofQuery(sender, args[0]);
                } else {
                    messenger.sendError(sender, "simplesidebar.see permission required");
                }
                return true;

            } else {
                messenger.sendError(sender, "Too many arguments");
                return true;
            }
        } else {
            messenger.sendError(sender, "This command is only for players");
            return true;
        }
    }
}
