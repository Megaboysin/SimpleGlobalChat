package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.handlers.PrvtMessageHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpyToggle implements CommandExecutor {

    private final PrvtMessageHandler handler;

    public SpyToggle(PrvtMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("simpleglobalchatplugin.spy")) {
            player.sendMessage(ChatColor.RED + "У вас нет прав на шпионский режим.");
            return true;
        }

        boolean nowEnabled = handler.toggleSpy(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "Шпионский режим " + (nowEnabled ? "включен." : "выключен."));
        return true;
    }
}