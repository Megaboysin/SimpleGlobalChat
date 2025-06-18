package kz.megabob.simpleGlobChat.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TechChatListener implements Listener {
    // Пока что бесполезная фигня
    // Пока что бесполезная фигня
    // Пока что бесполезная фигня
    private final TechChatHandler handler;

    public TechChatListener(TechChatHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();

        if (msg.contains("[TECH]")) {
            event.setCancelled(true);
            handler.sendTechMessage(msg.replace("[TECH]", "").trim());
        }
    }
}
