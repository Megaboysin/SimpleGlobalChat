package kz.megabob.simpleGlobChat.handlers;

import java.util.Objects;
import kz.megabob.simpleGlobChat.SimpleGlobChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathHandler implements Listener {

    private final SimpleGlobChat plugin;

    public PlayerDeathHandler(SimpleGlobChat plugin) {
        this.plugin = plugin;
    }

    //Просто проверяет сдохпадох ли игрок итгд
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (Objects.equals(plugin.getConfig().getString("events.custom-death.toggle"), "true")) {
            Player player = event.getEntity();
            String message_template = plugin.getConfig().getString("events.custom-death.message");
            String message_death = event.getDeathMessage().toString();
            event.setDeathMessage(message_template.replace("{death_message}", message_death).replace("{player}", player.getDisplayName()));
        }
    }
}
