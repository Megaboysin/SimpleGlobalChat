package kz.megabob.simpleGlobChat.handlers;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.utils.FormatResolver;
import kz.megabob.simpleGlobChat.utils.HexColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import java.util.*;

public class AdminChatHandler {

    private final Set<UUID> adminChatUsers = new HashSet<>();
    private final FormatResolver formatResolver;
    private final FileConfiguration config;

    public AdminChatHandler(FileConfiguration config, FormatResolver formatResolver) {
        this.config = config;
        this.formatResolver = formatResolver;
    }

    public static String stripColorCodes(String input) {
        return input.replaceAll("(?i)§[0-9A-FK-ORX]", "") // Удаляет §-коды
                .replaceAll("(?i)&[0-9A-FK-ORX]", "") // Удаляет &-коды
                .replaceAll("(?i)&#[0-9A-F]{6}", "");  // Удаляет hex цвета
    }

    //Вход/выход в/из Админ мода
    public boolean toggleAdminMode(UUID uuid) {
        if (adminChatUsers.contains(uuid)) {
            adminChatUsers.remove(uuid);
            return false;
        } else {
            adminChatUsers.add(uuid);
            return true;
        }
    }

    //Проверка находиться ли игрок в админ моде
    public boolean isInAdminMode(UUID uuid) {
        return adminChatUsers.contains(uuid);
    }

    //Отправка сообщения в адм чат
    public void sendAdminMessage(Player sender, String message) {
        String formatted = formatResolver.resolve("admin", sender, null, message);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("simpleglobchatplugin.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
            }
        }

        Bukkit.getConsoleSender().sendMessage(formatted);

        String cleanPrefix = "";
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            String prefix = PlaceholderAPI.setPlaceholders(sender, "%luckperms_prefix%");
            if (prefix == null || prefix.equals("%luckperms_prefix%")) {
                prefix = "";
            }
            cleanPrefix = stripColorCodes(prefix);
        }

        if (Bukkit.getPluginManager().getPlugin("DiscordSRV") != null) {
            TextChannel discordChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("admin");
            if (discordChannel != null) {
                discordChannel.sendMessage("[ADMIN] " + cleanPrefix + sender.getName() + " » " + message).queue();
            }
        }
    }

    // Отправка обращения
    public void sendAppeal(Player sender, String message) {
        String formatted = formatResolver.resolve("appeal", sender, null, message);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("simpleglobchatplugin.adminchat")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
            } else if (Objects.equals(p, sender)) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
            }
        }

        String msg = HexColorUtil.translateHexColorCodes(
                SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.Appeal.Sent")
        );
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        Bukkit.getConsoleSender().sendMessage(formatted);
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
            TextChannel discordChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("appeal");
            if (discordChannel != null) {
                discordChannel.sendMessage("[APPEAL] " + cleanPrefix + sender.getName() + " » " + message).queue();
            }
        }
    }

}