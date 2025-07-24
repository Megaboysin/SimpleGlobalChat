package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.handlers.IgnoreHandler;
import kz.megabob.simpleGlobChat.utils.HexColorUtil;
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
        if (!(sender instanceof Player player)) {
            String msg = HexColorUtil.translateHexColorCodes(SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.NotPlayer"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        if (args.length != 1) {
            String msg = HexColorUtil.translateHexColorCodes(SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.EmptyCommand"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            String msg = HexColorUtil.translateHexColorCodes(SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.PlayerOffline"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        if (player.equals(target)) {
            String msg = HexColorUtil.translateHexColorCodes(SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.Ignore.Self"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        ignoreHandler.unignore(player, target);
        String rawMessage = SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.Ignore.Unignored");
        String colored = HexColorUtil.translateHexColorCodes(rawMessage);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', colored).replace("%player%", target.getName()));
        return true;
    }
}
