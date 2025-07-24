package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.handlers.AdminChatHandler;
import kz.megabob.simpleGlobChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
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
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.OnlyPlayers")
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

        String message = String.join(" ", args);
        handler.sendAppeal(player, message);
        return true;
    }
}
