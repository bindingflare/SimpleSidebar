package com.gmail.flintintoe.sidebar;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.event.PlayerEvent;
import com.gmail.flintintoe.sidebar.placeholder.Placeholder;
import com.gmail.flintintoe.sidebar.sidebars.Sidebars;
import com.gmail.flintintoe.sidebar.tracker.Tracker;

/**
 * Manages the sidebars of the Spigot server with the help of the class PlayerEvent.
 *
 * @since v0.8.0_RC1
 * @see   PlayerEvent
 */
public class SidebarManager {
    private SimpleSidebar plugin;

    private Sidebars sidebars;
    private Tracker tracker;
    private Placeholder placeholder;

    public SidebarManager(SimpleSidebar plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        placeholder = new Placeholder(plugin);

        sidebars = new Sidebars(plugin);
        sidebars.load();

        tracker = new Tracker(plugin);
        tracker.runTaskTimer(plugin, 20L, 2L);
    }

    public Tracker getTracker() {
        return tracker;
    }

    public Sidebars getSidebars() {
        return sidebars;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }
}
