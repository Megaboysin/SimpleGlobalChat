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

public class ReplyMsg implements CommandExecutor {

    private final PrvtMessageHandler handler;

    public ReplyMsg(PrvtMessageHandler handler) {
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

        if (args.length < 1) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.EmptyCommand")
            );
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        var targetUUID = handler.getLastMessaged(player.getUniqueId());
        if (targetUUID == null) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.Private.NoReply")
            );
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null || !target.isOnline()) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.Private.Offline")
            );
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        String message = String.join(" ", args);
        handler.sendPrivateMessage(player, target, message);
        return true;
    }
}
