package kz.megabob.simpleGlobChat;

import kz.megabob.simpleGlobChat.commands.*;
import kz.megabob.simpleGlobChat.handlers.*;
import kz.megabob.simpleGlobChat.utils.*;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SimpleGlobChat extends JavaPlugin {
    public static SimpleGlobChat instance;
    private PrvtMessageHandler prvtMessageHandler;
    private AdminChatHandler adminChatHandler;
    private IgnoreHandler ignoreHandler;
    private ChatHandler chatHandler;
    private FormatResolver formatResolver;
    private LuckPerms luckPerms;
    private FileConfiguration config;
    private PlayerDeathHandler playerDeathHandler;
    private PlayerJoinHandler playerJoinHandler;
    private PlayerQuitHandler playerQuitHandler;
    private LangManager langManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveLangFile("lang/ru.yml");
        saveLangFile("lang/eng.yml");
        config = this.getConfig();
        boolean usePapi = config.getBoolean("use-placeholderapi");

        //Papi используется или нет
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI is found. Support enabled.");
        } else {
            getLogger().warning("PlaceholderAPI NOT found. Non-PAPI formats are used.");
            getLogger().warning("usePapi is set to false");
            usePapi = false;
        }

        //Передача данных в хендлеры
        formatResolver = new FormatResolver(this, usePapi);
        adminChatHandler = new AdminChatHandler(config, formatResolver);
        ignoreHandler = new IgnoreHandler();
        prvtMessageHandler = new PrvtMessageHandler(ignoreHandler, getPlugin(SimpleGlobChat.class), formatResolver);
        chatHandler = new ChatHandler(config, formatResolver, adminChatHandler, usePapi);
        playerDeathHandler = new PlayerDeathHandler(this);
        playerJoinHandler = new PlayerJoinHandler(this);
        playerQuitHandler = new PlayerQuitHandler(this);

        String lang = getConfig().getString("language", "eng");
        this.langManager = new LangManager(this, lang);

        //Регистрация комманд
        getCommand("msg").setExecutor(new PrvtMsg(prvtMessageHandler));
        getCommand("r").setExecutor(new ReplyMsg(prvtMessageHandler));
        getCommand("spy").setExecutor(new SpyToggle(prvtMessageHandler));
        getCommand("adm-chat").setExecutor(new AdmChatToggle(adminChatHandler));
        getCommand("appeal").setExecutor(new Appeal(adminChatHandler));
        getCommand("ignore").setExecutor(new IgnoreCommand(ignoreHandler));
        getCommand("unignore").setExecutor(new UnignoreCommand(ignoreHandler));
        getCommand("simpleglobchat").setExecutor(new MainCommand());
        getCommand("chathelp").setExecutor(new HelpCommand());

        // Регистрация ивентов
        getServer().getPluginManager().registerEvents(chatHandler, this);
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

    public static SimpleGlobChat getInstance() {
        return instance;
    }

    public void reloadPluginConfig() {
        reloadConfig();
        langManager.reloadLanguages();

        config = this.getConfig();

        String lang = getConfig().getString("language", "eng");
        boolean usePapi = config.getBoolean("use-placeholderapi");

        this.langManager = new LangManager(this, lang);
    }

    public LangManager getLangManager() {
        return langManager;
    }

    private void saveLangFile(String path) {
        File targetFile = new File(getDataFolder(), path);
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
            saveResource(path, false);
        }
    }
}
