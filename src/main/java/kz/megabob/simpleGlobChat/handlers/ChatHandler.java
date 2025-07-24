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

import static kz.megabob.simpleGlobChat.handlers.AdminChatHandler.stripColorCodes;

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
        if (event.isCancelled()) return;
        Player sender = event.getPlayer();
        String message = event.getMessage();

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
            Bukkit.getConsoleSender().sendMessage(ChatColor.stripColor(formatted));

            String cleanPrefix = "";
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                String prefix = PlaceholderAPI.setPlaceholders(sender, "%luckperms_prefix%");
                if (prefix == null || prefix.equals("%luckperms_prefix%")) {
                    prefix = "";
                }
                cleanPrefix = stripColorCodes(prefix);
            }

            if (Bukkit.getPluginManager().getPlugin("DiscordSRV") != null) {
                TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("global");
                if (channel != null) {
                    channel.sendMessage(cleanPrefix + sender.getName() + " » " + cleanMessage).queue();
                }
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

        // Шпионы
        String spyFormatted = formatResolver.resolve("spy", sender, null, message);
        for (UUID spyUUID : spies) {
            Player spy = Bukkit.getPlayer(spyUUID);
            if (spy != null && spy.isOnline() && !spy.equals(sender)) {
                spy.sendMessage(ChatColor.translateAlternateColorCodes('&', spyFormatted));
            }
        }

        // Дублирование сообщения в консоль
        Bukkit.getConsoleSender().sendMessage(ChatColor.stripColor(formatted));
    }
}
