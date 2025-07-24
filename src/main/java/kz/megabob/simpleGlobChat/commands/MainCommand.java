package kz.megabob.simpleGlobChat.commands;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import kz.megabob.simpleGlobChat.utils.LangManager;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MainCommand implements CommandExecutor {

    private final LangManager lang = SimpleGlobChat.getInstance().getLangManager();

    private final List<String> commandKeys = List.of(
            "msg",
            "r",
            "appeal",
            "ignore",
            "unignore",
            "spy",
            "adm-chat",
            "reload"
    );

    private final int COMMANDS_PER_PAGE = 5;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String langKey = lang.getDefaultLang();

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            int page = 1;
            if (args.length >= 2) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(lang.get(langKey, "Chat.Main.Help.InvalidPage")
                            .replace("%page%", args[1]));
                    return true;
                }
            }

            sendHelp(sender, langKey, page);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("simpleglobchat.reload")) {
                sender.sendMessage(lang.get(langKey, "Chat.General.NoRights"));
                return true;
            }
            SimpleGlobChat.getInstance().reloadPluginConfig();
            sender.sendMessage(lang.get(langKey, "Chat.Main.Help.Reloaded"));
            return true;
        }

        sender.sendMessage(lang.get(langKey, "Chat.Main.Help.UnknownCommand"));
        return true;
    }

    private void sendHelp(CommandSender sender, String langKey, int page) {
        int totalPages = (int) Math.ceil((double) commandKeys.size() / COMMANDS_PER_PAGE);
        if (page < 1 || page > totalPages) {
            sender.sendMessage(lang.get(langKey, "Chat.Main.Help.PageIndicator")
                    .replace("%current%", String.valueOf(page))
                    .replace("%total%", String.valueOf(totalPages)));
            return;
        }

        int start = (page - 1) * COMMANDS_PER_PAGE;
        int end = Math.min(start + COMMANDS_PER_PAGE, commandKeys.size());

        sender.sendMessage("§8§m------------------------------------");
        sender.sendMessage("§7[§bSimpleGlobalChat§7] §f" + lang.get(langKey, "Chat.Main.Help.Title")
                .replace("%current%", String.valueOf(page))
                .replace("%total%", String.valueOf(totalPages)));

        for (int i = start; i < end; i++) {
            String cmd = commandKeys.get(i);
            String usage = lang.get(langKey, "commands." + cmd + ".usage");
            String description = lang.get(langKey, "commands." + cmd + ".description");
            sender.sendMessage("§b" + usage + " §7– " + description);
        }

        if (sender instanceof Player player && totalPages > 1) {
            TextComponent nav = new TextComponent("§7[ ");

            if (page > 1) {
                TextComponent prev = new TextComponent("§b" + lang.get(langKey, "Chat.Main.Help.PreviousPage"));
                prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sgc help " + (page - 1)));
                prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(lang.get(langKey, "Chat.Main.Help.PageIndicator")
                                .replace("%current%", String.valueOf(page - 1))
                                .replace("%total%", String.valueOf(totalPages))).create()));
                nav.addExtra(prev);
                nav.addExtra(" ");
            }

            if (page < totalPages) {
                TextComponent next = new TextComponent("§b" + lang.get(langKey, "Chat.Main.Help.NextPage"));
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sgc help " + (page + 1)));
                next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(lang.get(langKey, "Chat.Main   .Help.PageIndicator")
                                .replace("%current%", String.valueOf(page + 1))
                                .replace("%total%", String.valueOf(totalPages))).create()));
                nav.addExtra(next);
            }

            nav.addExtra("§7 ]");
            player.spigot().sendMessage(nav);
        }

        sender.sendMessage("§8§m------------------------------------");
    }
}
