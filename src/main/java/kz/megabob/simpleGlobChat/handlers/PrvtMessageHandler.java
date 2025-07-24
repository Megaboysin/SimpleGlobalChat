package kz.megabob.simpleGlobChat.handlers;

import kz.megabob.simpleGlobChat.utils.HexColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.utils.FormatResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PrvtMessageHandler {
    private FormatResolver formatResolver;
    private final HashMap<UUID, UUID> lastMessageTargets = new HashMap<>();
    private final HashSet<UUID> spies = new HashSet<>();
    private IgnoreHandler ignoreHandler;
    private final SimpleGlobChat plugin;

    public PrvtMessageHandler(IgnoreHandler ignoreHandler, SimpleGlobChat plugin, FormatResolver formatResolver) {
        this.ignoreHandler = ignoreHandler;
        this.plugin = plugin;
        this.formatResolver = formatResolver;
    }

    public void sendPrivateMessage(Player sender, Player receiver, String message) {

        // Проверка игнора
        if (ignoreHandler.isIgnoring(receiver.getUniqueId(), sender.getUniqueId())) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.Private.Ignoring")
            );
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return;
        }
        // Отправка форматированного приватного меседж
        String formatted = formatResolver.resolve("private", sender, receiver, message);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
        Bukkit.getConsoleSender().sendMessage(formatted);
        receiver.playSound(receiver.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);

        //Отправка приватного сообщения игркоам в режиме шпиона
        for (UUID spyUUID : spies) {
            Player spy = Bukkit.getPlayer(spyUUID);
            if (spy != null && spy.isOnline() && !spy.equals(sender) && !spy.equals(receiver)) {
                String formattedspy = formatResolver.resolve("spy", sender, receiver, message);
                spy.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedspy));
            }
        }

        // Штука для reply
        lastMessageTargets.put(sender.getUniqueId(), receiver.getUniqueId());
        lastMessageTargets.put(receiver.getUniqueId(), sender.getUniqueId());
    }

    // Штука для reply
    public UUID getLastMessaged(UUID uuid) {
        return lastMessageTargets.get(uuid);
    }

    //Вход/выход в/из режим/-а Шпиона
    public boolean toggleSpy(UUID uuid) {
        if (spies.contains(uuid)) {
            spies.remove(uuid);
            return false;
        } else {
            spies.add(uuid);
            return true;
        }
    }
}