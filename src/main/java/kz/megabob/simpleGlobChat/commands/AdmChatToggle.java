package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.handlers.AdminChatHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdmChatToggle implements CommandExecutor {

    private final AdminChatHandler handler;

    public AdmChatToggle(AdminChatHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (!player.hasPermission("simpleglobalchatplugin.adminchat")) {
            player.sendMessage("§cУ вас нет прав.");
            return true;
        }

        boolean nowEnabled = handler.toggleAdminMode(player.getUniqueId());
        player.sendMessage("§6Режим админ-чата " + (nowEnabled ? "включен." : "выключен."));
        return true;
    }
}
