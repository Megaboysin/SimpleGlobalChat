package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.handlers.PrvtMessageHandler;
import kz.megabob.simpleGlobChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpyToggle implements CommandExecutor {

    private final PrvtMessageHandler handler;

    public SpyToggle(PrvtMessageHandler handler) {
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

        if (!player.hasPermission("simpleglobalchatplugin.spy")) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.NoPermission")
            );
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        boolean nowEnabled = handler.toggleSpy(player.getUniqueId());

        String messageKey = nowEnabled
                ? "Chat.Private.SpyMode.Enabled"
                : "Chat.Private.SpyMode.Disabled";

        String msg = HexColorUtil.translateHexColorCodes(
                SimpleGlobChat.getInstance().getLangManager().getDefault(messageKey)
        );
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return true;
    }
}
