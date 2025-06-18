package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.handlers.AdminChatHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Appeal implements CommandExecutor {

    private final AdminChatHandler handler;

    public Appeal(AdminChatHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("Использование: /appeal <сообщение>");
            return true;
        }

        String message = String.join(" ", args);
        handler.sendAppeal(player, message);
        return true;
    }
}
