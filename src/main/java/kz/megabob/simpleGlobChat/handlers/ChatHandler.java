package kz.megabob.simpleGlobChat.handlers;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.utils.FormatResolver;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatHandler implements Listener {

    private final FileConfiguration config;
    private final FormatResolver formatResolver;
    private final AdminChatHandler adminChatHandler;
    private final Set<UUID> spies = new HashSet<>();

    public ChatHandler(FileConfiguration config, FormatResolver formatResolver, AdminChatHandler adminChatHandler, boolean usePapi) {
        this.config = config;
        this.formatResolver = formatResolver;
        this.adminChatHandler = adminChatHandler;
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();
        boolean usePapi = config.getBoolean("use-placeholderapi", true);

        // Если игрок в админ моде, то соодщение отменяет свою отправку здесь, поэтому отправляется только в адмчат
        if (adminChatHandler.isInAdminMode(sender.getUniqueId())) {
            event.setCancelled(true);
            adminChatHandler.sendAdminMessage(sender, message);
            return;
        }

        // Глобальный чат (! в начале)
        if (message.startsWith("!")) {
            event.setCancelled(true);
            String cleanMessage = message.substring(1).trim();
            String formatted = formatResolver.resolve("global", sender, null, message.substring(1).trim());
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
            }
            Bukkit.getConsoleSender().sendMessage(formatted);

            TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("global");
            String prefix = PlaceholderAPI.setPlaceholders(sender, "%luckperms_prefix%");
            if (prefix == null || prefix.equals("%luckperms_prefix%")) {
                prefix = "";
            }
            String cleanPrefix = AdminChatHandler.stripColorCodes(prefix);
            if (channel != null) {
                channel.sendMessage(cleanPrefix + sender.getName() + " » " + cleanMessage).queue();
            }
            return;
        }

        // Локальный чат
        event.setCancelled(true);
        int radius = config.getInt("chat.local.radius", 50);
        String formatted = formatResolver.resolve("local", sender, null, message);
        for (Player p : sender.getWorld().getPlayers()) {
            if (p.getLocation().distance(sender.getLocation()) <= radius) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
            }
        }

        //Для шпионов (Пока что не работает)
        for (UUID spyUUID : spies) {
            Player spy = Bukkit.getPlayer(spyUUID);
            if (spy != null && spy.isOnline() && !spy.equals(sender)) {
                String formattedspy = formatResolver.resolve("spy", sender, null, message);
                spy.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
                spy.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedspy));
            }
        }

        // Шпионы
        String spyFormatted = formatResolver.resolve("spy", sender, null, message);
        for (UUID spyUUID : spies) {
            Player spy = Bukkit.getPlayer(spyUUID);
            if (spy != null && spy.isOnline() && !spy.equals(sender)) {
                spy.sendMessage(ChatColor.translateAlternateColorCodes('&', spyFormatted));
            }
        }

        // Дублирование сообщения в консоль
        Bukkit.getConsoleSender().sendMessage(formatted);
    }
}
