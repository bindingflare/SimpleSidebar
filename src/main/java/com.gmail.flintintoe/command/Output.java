package com.gmail.flintintoe.command;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.Config;
import com.gmail.flintintoe.message.Messenger;
import com.gmail.flintintoe.sidebar.SidebarManager;
import com.gmail.flintintoe.sidebar.sidebars.Sidebars;
import com.gmail.flintintoe.sidebar.tracker.Tracker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Deals with the output of commands (Includes executing commands).
 *
 * @since v0.8.0_pre1
 */
public class Output {
    private Config configs;
    private SidebarManager sidebarM;
    private Messenger messenger;
    private Tracker tracker;

    public Output(SimpleSidebar plugin) {
        configs = plugin.getConfigManager();
        messenger = plugin.getMessenger();

        sidebarM = plugin.getSidebarManager();
        tracker = plugin.getSidebarManager().getTracker();
    }

    void ofInfo(CommandSender sender, Player target) {
        int sidebarIndex = tracker.getIndex(sender.getName());

        if (sidebarIndex != -1) {
            if (sender == target) {
                messenger.send(sender, 11);
            } else {
                messenger.send(sender, 12);
            }
        } else {
            messenger.send(sender, 5);
        }
    }

    // Todo redo scopes so that program does not continue when error
    void ofQuery(CommandSender sender, Player target, String query) {
        Player player = (Player) sender;

        int sidebarIndex = sidebarM.getSidebars().getSidebarIndexOf(query);

        // Check if query is an String positive
        if (sidebarIndex == -1) {
            for (char c : query.toCharArray()) {
                if (!Character.isDigit(c)) {
                    messenger.send(sender, 13);
                }
            }
        }
        // Try to set sidebar (For String query)
        else {
            // Check if setting AFK sb
            if (sidebarIndex == sidebarM.getSidebars().getSidebarCount() && !configs.isAllowAfkSet()) {
                if (sender == target) {
                    messenger.send(sender, 15);
                } else {
                    messenger.send(sender, 16);
                }
            }
            sidebarM.getSidebars().setSidebar(target, sidebarIndex);
        }

        // sidebarIndex will always be -1 at this point

        try {
            sidebarIndex = Integer.parseInt(query);
        } catch (Exception e) {
            e.printStackTrace();
            messenger.send(sender, 8);
        }

        // Set sidebar (For int query)
        if (sidebarIndex != -1) {
            if (sidebarIndex == sidebarM.getSidebars().getSidebarCount() - 1 && !configs.isAllowAfkSet()) {
                messenger.send(sender, 15);

            } else if (sidebarIndex < -1 || sidebarIndex >= sidebarM.getSidebars().getSidebarCount()) {
                messenger.send(sender, 14);
            } else {
                sidebarM.getSidebars().setSidebar(target, sidebarIndex);
                if (sender == target) {
                    messenger.send(sender, 2);
                } else {
                    messenger.send(sender, 3);
                    messenger.send(target, 4);
                }

            }
        } else {
            sidebarM.getSidebars().setEmptySidebar(target);
            // Message for set empty sidebar
        }
    }

    // Todo merge into one method
    void ofRemove(CommandSender sender) {
        messenger.send(sender, "Setting your sidebar to an empty sidebar");
        sidebarM.getSidebars().setEmptySidebar((Player) sender);
        tracker.set(sender.getName(), -1);
    }

    void ofRemove(CommandSender sender, Player target) {
        messenger.send(sender, "Setting the sidebar of " + target.getDisplayName() + " to an empty sidebar");
        messenger.send(target, "Your sidebar has been changed to an empty sidebar");
        sidebarM.getSidebars().setEmptySidebar(target);
        tracker.set(target.getDisplayName(), -1);
    }

    void reloadPlugin(CommandSender sender) {
        long startTime = System.nanoTime();

        messenger.send(sender, "Reloading configs...");
        configs.loadConfigFiles();
        configs.loadConfig();

        messenger.send(sender, "Reloading local placeholders");
        sidebarM.getPlaceholder().loadLocalPlaceholders();

        messenger.send(sender, "Reloading sidebars");
        sidebarM.getSidebars().load();

        messenger.send(sender, "Reloading messages...");
        messenger.loadMessages();

        long timePassed = System.nanoTime() - startTime;
        int seconds = Long.valueOf(timePassed).intValue() / 1000000000;
        int milliseconds = Long.valueOf(timePassed).intValue() / 1000000;

        messenger.send(sender, "Done! Took " + seconds + " seconds " + milliseconds + " milliseconds");
    }
}
