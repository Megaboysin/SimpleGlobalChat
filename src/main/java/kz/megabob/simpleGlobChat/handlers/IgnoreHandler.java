package kz.megabob.simpleGlobChat.handlers;

import org.bukkit.entity.Player;

import java.util.*;

public class IgnoreHandler {
    // Кто кого игнорирует: ключ — UUID игрока, значение — множество UUID игнорируемых
    private final Map<UUID, Set<UUID>> ignores = new HashMap<>();

    // Игнорировать игрока
    public void ignore(Player player, Player toIgnore) {
        ignores.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(toIgnore.getUniqueId());
    }

    // Перестать игнорить
    public void unignore(Player player, Player toUnignore) {
        Set<UUID> set = ignores.get(player.getUniqueId());
        if (set != null) {
            set.remove(toUnignore.getUniqueId());
            if (set.isEmpty()) {
                ignores.remove(player.getUniqueId());
            }
        }
    }

    // Проверить, игнорирует ли player1 player2
    public boolean isIgnoring(UUID player1, UUID player2) {
        Set<UUID> set = ignores.get(player1);
        return set != null && set.contains(player2);
    }
}
