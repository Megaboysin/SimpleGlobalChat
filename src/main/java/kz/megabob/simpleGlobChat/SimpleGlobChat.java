package kz.megabob.simpleGlobChat;

import kz.megabob.simpleGlobChat.commands.*;
import kz.megabob.simpleGlobChat.handlers.*;
import kz.megabob.simpleGlobChat.utils.FormatResolver;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleGlobChat extends JavaPlugin {
    public static SimpleGlobChat instance;
    private PrvtMessageHandler prvtMessageHandler;
    private AdminChatHandler adminChatHandler;
    private TechChatHandler techChatHandler;
    private IgnoreHandler ignoreHandler;
    private ChatHandler chatHandler;
    private FormatResolver formatResolver;
    private LuckPerms luckPerms;
    private FileConfiguration config;
    private PlayerDeathHandler playerDeathHandler;
    private PlayerJoinHandler playerJoinHandler;
    private PlayerQuitHandler playerQuitHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = this.getConfig();
        boolean usePapi = config.getBoolean("use-placeholderapi");

        //Papi используется или нет
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI найден. Поддержка включена.");
        } else {
            getLogger().warning("PlaceholderAPI НЕ найден. Форматы без PAPI.");
        }

        //Передача данных в хендлеры
        formatResolver = new FormatResolver(this, usePapi);
        adminChatHandler = new AdminChatHandler(config, formatResolver);
        techChatHandler = new TechChatHandler();
        ignoreHandler = new IgnoreHandler();
        prvtMessageHandler = new PrvtMessageHandler(ignoreHandler, getPlugin(SimpleGlobChat.class), formatResolver);
        chatHandler = new ChatHandler(config, formatResolver, adminChatHandler, usePapi);
        playerDeathHandler = new PlayerDeathHandler(this);
        playerJoinHandler = new PlayerJoinHandler(this);
        playerQuitHandler = new PlayerQuitHandler(this);

        //Регистрация комманд
        getCommand("msg").setExecutor(new PrvtMsg(prvtMessageHandler));
        getCommand("r").setExecutor(new ReplyMsg(prvtMessageHandler));
        getCommand("spy").setExecutor(new SpyToggle(prvtMessageHandler));
        getCommand("sgc reload").setExecutor(new ReloadChat(this));
        getCommand("adm-chat").setExecutor(new AdmChatToggle(adminChatHandler));
        getCommand("appeal").setExecutor(new Appeal(adminChatHandler));
        getCommand("tech-chat").setExecutor(new TechChatToggle(techChatHandler));
        getCommand("ignore").setExecutor(new IgnoreCommand(ignoreHandler));
        getCommand("unignore").setExecutor(new UnignoreCommand(ignoreHandler));

        // Регистрация ивентов
        getServer().getPluginManager().registerEvents(chatHandler, this);
        getServer().getPluginManager().registerEvents(new TechChatListener(techChatHandler), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathHandler(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitHandler(this), this);

        getServer().getLogger().info("§2[SGC] Plugin is ready");
    }

    // Выключение плагина
    @Override
    public void onDisable() {
        getServer().getLogger().info("§4[SGC] Plugin was shutdown!");

    }
}
