package kz.megabob.simpleGlobChat.handlers;

import java.util.Objects;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {

    private final SimpleGlobChat plugin;

    public PlayerJoinHandler(SimpleGlobChat plugin) {
        this.plugin = plugin;
    }

    //Просто проверяет зашел ли игрок итгд
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Objects.equals(plugin.getConfig().getString("events.custom-join.toggle"), "true")) {
            String message_join = plugin.getConfig().getString("events.custom-join.message");
            event.setJoinMessage(message_join.replace("{player}", player.getDisplayName()));
        }
    }
}
