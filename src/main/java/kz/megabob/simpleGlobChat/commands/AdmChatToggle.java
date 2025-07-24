package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.handlers.AdminChatHandler;
import kz.megabob.simpleGlobChat.utils.HexColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdmChatToggle implements CommandExecutor {

    private final AdminChatHandler handler;

    public AdmChatToggle(AdminChatHandler handler) {
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

        if (!player.hasPermission("simpleglobalchatplugin.adminchat")) {
            String msg = HexColorUtil.translateHexColorCodes(
                    SimpleGlobChat.getInstance().getLangManager().getDefault("Chat.General.NoRights")
            );
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        boolean nowEnabled = handler.toggleAdminMode(player.getUniqueId());
        String messageKey = nowEnabled
                ? "Chat.Admchat.Toggle.Enabled"
                : "Chat.Admchat.Toggle.Disabled";
        String msg = HexColorUtil.translateHexColorCodes(
                SimpleGlobChat.getInstance().getLangManager().getDefault(messageKey)
        );
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return true;
    }
}
