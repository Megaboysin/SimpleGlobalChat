package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.handlers.PrvtMessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyMsg implements CommandExecutor {

    private final PrvtMessageHandler handler;

    public ReplyMsg(PrvtMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("Использование: /r <сообщение>");
            return true;
        }

        var targetUUID = handler.getLastMessaged(player.getUniqueId());
        if (targetUUID == null) {
            player.sendMessage(ChatColor.RED + "Нет игрока для ответа.");
            return true;
        }

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Игрок оффлайн.");
            return true;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (String arg : args) {
            messageBuilder.append(arg).append(" ");
        }

        String message = messageBuilder.toString().trim();
        handler.sendPrivateMessage(player, target, message);
        return true;
    }
}