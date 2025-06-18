package kz.megabob.simpleGlobChat.handlers;

import java.util.Objects;
import kz.megabob.simpleGlobChat.SimpleGlobChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitHandler implements Listener {
    private final SimpleGlobChat plugin;

    public PlayerQuitHandler(SimpleGlobChat plugin) {
        this.plugin = plugin;
    }

    //Просто проверяет вышел ли игрок итгд
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Objects.equals(plugin.getConfig().getString("events.custom-quit.toggle"), "true")) {
            String message_quit = plugin.getConfig().getString("events.custom-quit.message");
            event.setQuitMessage(message_quit.replace("{player}", player.getDisplayName()));
        }
    }
}
