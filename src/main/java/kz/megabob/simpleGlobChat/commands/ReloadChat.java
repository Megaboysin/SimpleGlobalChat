package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadChat implements CommandExecutor {

    private final SimpleGlobChat plugin;

    public ReloadChat(SimpleGlobChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("simpleglobalchatplugin.reload")) {
            sender.sendMessage(ChatColor.RED + "§cУ вас нет прав для этого.");
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "§aКонфигурация успешно перезагружена.");
        return true;
    }
}
