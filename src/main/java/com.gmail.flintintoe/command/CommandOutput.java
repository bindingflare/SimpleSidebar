package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.ConfigFile;
import com.gmail.flintintoe.config.PluginConfig;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.Sidebar;
import com.gmail.flintintoe.timer.SidebarRunnable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandOutput {
    private PluginConfig config;
    private Sidebar sidebar;
    private Messenger messenger;
    private SidebarRunnable runnable;

    public CommandOutput(SimpleSidebar plugin) {
        config = plugin.getPluginConfig();
        sidebar = plugin.getSidebar();
        messenger = plugin.getMessenger();
        runnable = plugin.getsRunnable();
    }

    public void playerSidebarInfo(CommandSender sender, int sidebarIndex) {
        if (sidebarIndex != -1) {
            messenger.send(sender, "Sidebar details of " + sender.getName());
            messenger.send(sender, "  Name: " + sidebar.getSidebarName(sidebarIndex));
            messenger.send(sender, "  Index: " + (sidebarIndex + 1));
            messenger.send(sender, "  Aliases: " + Arrays.toString(sidebar.getSidebarAliases(sidebarIndex)));
        } else {
            messenger.sendError(sender, "You do not have a sidebar set");
        }
    }

    public void playerSidebarInfo(CommandSender sender, Player target, int sidebarIndex) {
        if (sidebarIndex != -1) {
            messenger.send(sender, target.getDisplayName() + "'s sidebar is " + sidebar.getSidebarName(sidebarIndex));
            messenger.send(sender, "  Index: " + (sidebarIndex + 1));
            messenger.send(sender, "  Aliases: " + Arrays.toString(sidebar.getSidebarAliases(sidebarIndex)));
        } else {
            messenger.sendError(sender, "You do not have a sidebar set");
        }
    }

    public void playerSetSidebar(CommandSender sender, int returnCode) {
        if (returnCode == -1) {
            messenger.send(sender, "Either the sidebar with the query name does not exist, or the number is a negative number");
        } else if (returnCode == -2) {
            messenger.send(sender, "You cannot set your sidebar to the AFK sidebar");
//            if (config.isAdminBypass()) {
//                messenger.send("Set sidebar of " +  + " to " + args[1] + " using admin bypass");
//                messenger.send(target, "Your sidebar has been changed to AFK sidebar " + args[1]);
//
//                sidebar.setAndUpdateSidebar(target, sidebar.getSidebarCount() - 1);
//            } else {
//                messenger.sendError("You do not have the permission to set " + args[0] + "'s sidebar to AFK sidebar" + args[1]);
//            }
        } else if (returnCode == -3) {
            messenger.send(sender, "Sidebar index is out of bounds");
        } else if (returnCode == -4) {
            messenger.sendError(sender, "Unexpected error");
            messenger.send(sender, "Please try again");
        } else {
            messenger.send(sender, "Set your sidebar to " + sidebar.getSidebarName(returnCode) + "(Index: " + (returnCode + 1) + ")");

            sidebar.setAndUpdateSidebar((Player) sender, returnCode);
            runnable.updateSidebarTime(((Player) sender).getDisplayName(), config.getAfkTimer());
            messenger.send(sender, "DEBUG: " + sender.getName());
        }
    }

    public void playerSetSidebar(CommandSender sender, Player target, int returnCode) {
        if (returnCode == -1) {
            messenger.send(sender, "Either the sidebar with the query name does not exist, or the number is a negative number");
        } else if (returnCode == -2) {
            messenger.send(sender, "You cannot set your sidebar to the AFK sidebar");
//            if (config.isAdminBypass()) {
//                messenger.send("Set sidebar of " +  + " to " + args[1] + " using admin bypass");
//                messenger.send(target, "Your sidebar has been changed to AFK sidebar " + args[1]);
//
//                sidebar.setAndUpdateSidebar(target, sidebar.getSidebarCount() - 1);
//            } else {
//                messenger.sendError("You do not have the permission to set " + args[0] + "'s sidebar to AFK sidebar" + args[1]);
//            }
        } else if (returnCode == -3) {
            messenger.send(sender, "Sidebar index is out of bounds");
        } else if (returnCode == -4) {
            messenger.sendError(sender, "Unexpected error");
            messenger.send(sender, "Please try again");
        } else {
            messenger.send(sender, "Set sidebar of " + target.getDisplayName() + " to " + sidebar.getSidebarName(returnCode));
            messenger.send(target, "Your sidebar has been changed to " + sidebar.getSidebarName(returnCode) + "(Index: " + (returnCode + 1) + ")");

            sidebar.setAndUpdateSidebar((Player) sender, returnCode);
            runnable.updateSidebarTime(((Player) sender).getDisplayName(), config.getAfkTimer());
        }
    }

    public void reloadPlugin(CommandSender sender) {
        long startTime = System.nanoTime();

        messenger.send(sender, "Reloading config...");
        config.loadFiles();
        config.loadConfig();

        if (!config.getBoolean(ConfigFile.CONFIG, "plugin_enabled")) {
            messenger.sendError(sender, "Cannot deactivate plugin through a config loadFiles");
        }

        messenger.send(sender, "Reloading sidebars");
        sidebar.loadSidebars();

        double timePassed = System.nanoTime() - startTime;
        int seconds = Double.valueOf(timePassed / 1000).intValue();
        int milliseconds = Double.valueOf(timePassed % 1000).intValue();

        messenger.send("Done! Took " + seconds + " seconds " + milliseconds + " milliseconds");
    }
}
