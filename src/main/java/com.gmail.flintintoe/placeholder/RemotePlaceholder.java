package com.gmail.flintintoe.placeholder;

import com.gmail.flintintoe.SimpleSidebar;
import com.gmail.flintintoe.config.PluginConfig;
import com.gmail.flintintoe.message.Messenger;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class RemotePlaceholder {
    private Messenger messenger;

    private List<String> remotePlaceholders;

    private boolean isPlaceholderAPIEnabled;

    // TODO local placeholders

    public RemotePlaceholder (SimpleSidebar plugin) {
        messenger = plugin.getMessenger();
    }

    public void setup(SimpleSidebar plugin) {
        // Placeholder API
        if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return;
        }
        messenger.send("PlaceholderAPI found! You can now use placeholders from Placeholder API");

        isPlaceholderAPIEnabled = true;
    }

//    public void load() {
//
//    }

    public String setRemotePlaceholder (Player player, String tag) {
        String wordWithPh = "";
        if (isPlaceholderAPIEnabled) {
            wordWithPh = PlaceholderAPI.setPlaceholders(player, tag);
        }

        return wordWithPh;
    }
}
