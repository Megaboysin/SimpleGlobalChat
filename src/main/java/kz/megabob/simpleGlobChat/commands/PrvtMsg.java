package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.handlers.PrvtMessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrvtMsg implements CommandExecutor {

    private final PrvtMessageHandler handler;

    public PrvtMsg(PrvtMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return true;
        }

        Player senderplayer = (Player) sender;

        if (args.length < 2) {
            senderplayer.sendMessage("Использование: /msg <игрок> <сообщение>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            senderplayer.sendMessage(ChatColor.RED + "Игрок не найден или оффлайн.");
            return true;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");

        }

        String message = messageBuilder.toString().trim();
        handler.sendPrivateMessage(senderplayer, target, message);
        return true;
    }
}