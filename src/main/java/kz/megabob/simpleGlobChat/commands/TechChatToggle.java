package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.handlers.TechChatHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TechChatToggle implements CommandExecutor {

    private final TechChatHandler handler;

    public TechChatToggle(TechChatHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (!player.hasPermission("simpleglobalchatplugin.techchat")) {
            player.sendMessage("У вас нет доступа к тех. чату.");
            return true;
        }

        boolean nowEnabled = handler.toggle(player.getUniqueId());
        player.sendMessage("§bТех. чат " + (nowEnabled ? "включен." : "выключен."));
        return true;
    }
}
