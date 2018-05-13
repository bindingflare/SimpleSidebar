package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.sidebars.Sidebars;
import com.gmail.flintintoe.sidebar.tracker.Tracker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Deals with the output of commands (Includes executing commands).
 *
 * @since v0.8.0_RC1
 */
public class CommandOutput {
    private Config configs;
    private Sidebars sidebars;
    private Messenger messenger;
    private Tracker tracker;

    public CommandOutput(SimpleSidebar plugin) {
        configs = plugin.getConfigManager();
        messenger = plugin.getMessenger();

        sidebars = plugin.getSidebarManager().getSidebars();
        tracker = plugin.getSidebarManager().getTracker();
    }

    public void ofInfo(CommandSender sender, Player target) {
        int sidebarIndex = tracker.getIndex(sender.getName());

        if (sidebarIndex != -1) {
            messenger.send(sender, target.getDisplayName() + "'s sidebar is " + sidebars.getSidebarName(sidebarIndex));
            messenger.send(sender, "  Index: " + sidebarIndex);
            messenger.send(sender, "  Aliases: " + Arrays.toString(sidebars.getSidebarAliases(sidebarIndex)));
        } else {
            messenger.sendError(sender, "You do not have a sidebar set");
        }
    }

    public void ofQuery(CommandSender sender, String query) {
        Player player = (Player) sender;

        int sidebarIndex = sidebars.querySidebarIndexOf(query);

        if (sidebarIndex == -1) {
            messenger.send(sender, "Either the sidebar with the query name does not exist, or the number is a negative number");
        } else if (sidebarIndex == -2) {
            messenger.send(sender, "You cannot set your sidebars to the AFK sidebar");
//            if (configs.isAdminBypass()) {
//                messenger.send("Set sidebars of " +  + " to " + args[1] + " using admin bypass");
//                messenger.send(target, "Your sidebars has been changed to AFK sidebars " + args[1]);
//
//                sidebars.setAndUpdateSidebar(target, sidebars.getSidebarCount() - 1);
//            } else {
//                messenger.sendError("You do not have the permission to set " + args[0] + "'s sidebars to AFK sidebars" + args[1]);
//            }
        } else if (sidebarIndex == -3) {
            messenger.send(sender, "Sidebar index is out of bounds");
        } else if (sidebarIndex == -4) {
            messenger.sendError(sender, "Unexpected error");
            messenger.send(sender, "Please try again");
        } else {
            messenger.send(player, "Your sidebar has been changed to " + sidebars.getSidebarName(sidebarIndex) + " " +
                    "(Index: " + sidebarIndex + ")");

            sidebars.setSidebar((Player) sender, sidebarIndex);
            tracker.set(sender.getName(), sidebarIndex);
        }
    }

    public void ofQuery(CommandSender sender, Player target, String query) {
        int sidebarIndex = sidebars.querySidebarIndexOf(query);

        if (sidebarIndex == -1) {
            messenger.send(sender, "Either the sidebar with the query name does not exist, or the number is a negative number");
        } else if (sidebarIndex == -2) {
            messenger.send(sender, "You cannot set your sidebar to the AFK sidebars");
//            if (configs.isAdminBypass()) {
//                messenger.send("Set sidebars of " +  + " to " + args[1] + " using admin bypass");
//                messenger.send(target, "Your sidebars has been changed to AFK sidebars " + args[1]);
//
//                sidebars.setAndUpdateSidebar(target, sidebars.getSidebarCount() - 1);
//            } else {
//                messenger.sendError("You do not have the permission to set " + args[0] + "'s sidebars to AFK sidebars" + args[1]);
//            }
        } else if (sidebarIndex == -3) {
            messenger.send(sender, "Sidebar index is out of bounds");
        } else if (sidebarIndex == -4) {
            messenger.sendError(sender, "Unexpected error");
            messenger.send(sender, "Please try again");
        } else {
            messenger.send(sender, "Set sidebar of " + target.getDisplayName() + " to " + sidebars.getSidebarName(sidebarIndex));
            messenger.send(target, "Your sidebar has been changed to " + sidebars.getSidebarName(sidebarIndex) + " (Index: " + sidebarIndex + ")");

            sidebars.setSidebar((Player) sender, sidebarIndex);
            tracker.set(sender.getName(), sidebarIndex);
        }
    }

    public void ofRemove(CommandSender sender) {
        messenger.send(sender, "Setting your sidebar to an empty sidebar");
        sidebars.setEmptySidebar((Player) sender);
        tracker.set(sender.getName(), -1);
    }

    public void ofRemove(CommandSender sender, Player target) {
        messenger.send(sender, "Setting the sidebar of " + target.getDisplayName() + " to an empty sidebar");
        messenger.send(target, "Your sidebar has been changed to an empty sidebar");
        sidebars.setEmptySidebar(target);
        tracker.set(target.getDisplayName(), -1);
    }

    public void reloadPlugin(CommandSender sender) {
        long startTime = System.nanoTime();

        messenger.send(sender, "Reloading configs...");
        configs.loadConfigFiles();
        configs.loadConfig();

        messenger.send(sender, "Reloading sidebars");
        sidebars.load();

        long timePassed = System.nanoTime() - startTime;
        int seconds = Long.valueOf(timePassed).intValue() / 1000000000;
        int milliseconds = Long.valueOf(timePassed).intValue() / 1000000;

        messenger.send(sender, "Done! Took " + seconds + " seconds " + milliseconds + " milliseconds");
    }
}
