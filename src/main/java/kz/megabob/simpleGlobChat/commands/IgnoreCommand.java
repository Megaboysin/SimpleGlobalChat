package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.handlers.IgnoreHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCommand implements CommandExecutor {

    private final IgnoreHandler ignoreHandler;

    public IgnoreCommand(IgnoreHandler ignoreHandler) {
        this.ignoreHandler = ignoreHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Команду может использовать только игрок.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("Использование: /ignore <ник игрока>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Игрок не найден.");
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(ChatColor.RED + "Нельзя игнорировать самого себя.");
            return true;
        }

        ignoreHandler.ignore(player, target);
        player.sendMessage(ChatColor.GOLD + "Вы начали игнорировать " + target.getName() + ".");
        return true;
    }
}
