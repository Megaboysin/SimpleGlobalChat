package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.handlers.IgnoreHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnignoreCommand implements CommandExecutor {

    private final IgnoreHandler ignoreHandler;

    public UnignoreCommand(IgnoreHandler ignoreHandler) {
        this.ignoreHandler = ignoreHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Если комманду решила отправить консоль
        if (!(sender instanceof Player)) {
            sender.sendMessage("Команду может использовать только игрок.");
            return true;
        }

        //Если в комманде слишком мало characters
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(ChatColor.GRAY + "Использование: /unignore <ник игрока>");
            return true;
        }

        //Если игрока нет
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Игрок не найден.");
            return true;
        }

        //Что бы самого себя не начали игнорить
        if (player.equals(target)){
            player.sendMessage(ChatColor.RED + "Вы не можете использовать эту команду на себе.");
            return true;
        }

        //Перестать игнорировать челикса
        ignoreHandler.unignore(player, target);
        player.sendMessage(ChatColor.GOLD + "Вы перестали игнорировать " + target.getName() + ".");
        return true;
    }
}
