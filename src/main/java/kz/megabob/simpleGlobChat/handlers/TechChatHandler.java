package kz.megabob.simpleGlobChat.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class TechChatHandler {
    // Пока что бесполезная фигня
    // Пока что бесполезная фигня
    // Пока что бесполезная фигня
    private final HashSet<UUID> watching = new HashSet<>();

    public boolean toggle(UUID uuid) {
        if (watching.contains(uuid)) {
            watching.remove(uuid);
            return false;
        } else {
            watching.add(uuid);
            return true;
        }
    }

    public boolean isWatching(UUID uuid) {
        return watching.contains(uuid);
    }

    public void sendTechMessage(String rawMessage) {
        String formatted = ChatColor.DARK_AQUA + "[TECH] " + ChatColor.GRAY + rawMessage;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("simpleglobalchatplugin.techchat") && isWatching(p.getUniqueId())) {
                p.sendMessage(formatted);
            }
        }

        Bukkit.getConsoleSender().sendMessage("[TECH] " + rawMessage);
    }
}
