package kz.megabob.simpleGlobChat.utils;

import kz.megabob.simpleGlobChat.SimpleGlobChat;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FormatResolver {

    private final SimpleGlobChat plugin;
    private final boolean usePapi;

    public FormatResolver(SimpleGlobChat plugin, boolean usePapi) {
        this.plugin = plugin;
        this.usePapi = usePapi;
    }


    // Changing format of messages
    public String resolve(String channel, Player sender, Player receiver, String message) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("chat." + channel.toLowerCase());

        // Safe if there is no section
        if (section == null) {
            return "§c[ERROR] Invalid format section: chat." + channel;
        }

        // Safe if there is no format
        if (!section.contains("msgformat")) {
            String format = section.getString("msgformat");
            if (format == null) {
                Bukkit.getLogger().warning("[FormatResolver] Format field missing in section: chat." + channel);
                return "§c[ERROR] Format not defined for channel: chat." + channel;
            }
        }


        // Loading configuration and second choices
        String format = section.getString("msgformat", "{player}: {message}");
        Bukkit.getLogger().info("Loaded format for channel " + channel + ": " + format);
        String prefix = section.getString("prefix", "");
        String nameColor = section.getString("name-color", "");
        String receivercolor = section.getString("receiver-color", "");
        String messageColor = section.getString("message-color", "");
        String placeholderPrefix = section.getString("placeholder-prefix", "");
        String receiverprefix = section.getString("receiver-prefix", "");
        String worldName = sender.getWorld().getName();
        String worldColor;


        // color of a world
        switch (worldName.toLowerCase()) {
            case "world":
                worldColor = "§2";
                break;
            case "world_nether":
                worldColor = "§4";
                break;
            case "world_the_end":
                worldColor = "§5";
                break;
            default:
                worldColor = "§3";
        }

        // name of a world
        String worldPrefix = "";
        ConfigurationSection worldPrefixSection = section.getConfigurationSection("world-prefix");
        if (worldPrefixSection != null && worldPrefixSection.getBoolean("enabled")) {
            String worldFormat = worldPrefixSection.getString("format", "§8[{world}] ");
            worldPrefix = worldFormat.replace("{world}", sender.getWorld().getName());
        }

        String playerName = sender.getName();
        String receiverName = (receiver != null) ? receiver.getName() : "";

        // PlaceholderAPI
        if (usePapi) {
            Bukkit.getLogger().info("Using PAPI ");
            format = PlaceholderAPI.setPlaceholders(sender, format);
            prefix = PlaceholderAPI.setPlaceholders(sender, prefix);
            receiverprefix = PlaceholderAPI.setPlaceholders(receiver, receiverprefix);
            placeholderPrefix = PlaceholderAPI.setPlaceholders(sender, placeholderPrefix);
        }

        // Final Replacement
        return format
                .replace("{world-color}", worldColor)
                .replace("{world-prefix}", worldPrefix)
                .replace("{prefix}", prefix)
                .replace("{placeholder-prefix}", placeholderPrefix)
                .replace("{name-color}", nameColor)
                .replace("{message-color}", messageColor)
                .replace("{player}", playerName)
                .replace("{receiver-prefix}", receiverprefix)
                .replace("{receiver-color}", receivercolor)
                .replace("{receiver}", receiverName)
                .replace("{sender}", playerName)
                .replace("{message}", message);

    }
}
