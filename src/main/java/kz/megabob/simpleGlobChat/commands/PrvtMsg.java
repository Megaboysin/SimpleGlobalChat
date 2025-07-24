package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.handlers.PrvtMessageHandler;
import kz.megabob.simpleGlobChat.utils.HexColorUtil;
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
        if (!(sender instanceof Player player)) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.NotPlayer")
            );
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        if (args.length < 2) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.EmptyCommand")
            );
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.PlayerOffline")
            );
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        if (player.equals(target)) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.Private.Self")
            );
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }

        String message = messageBuilder.toString().trim();
        handler.sendPrivateMessage(player, target, message);
        return true;
    }
}
