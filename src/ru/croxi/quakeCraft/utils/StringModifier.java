package ru.croxi.quakeCraft.utils;

import org.bukkit.entity.Player;
import ru.croxi.quakeCraft.QuakeCraft;

public class StringModifier {

    public static String getModifiedString(String string) {
        return string
                .replace("%prefix%", QuakeCraft.getInstance().getConfig().getString("messages.prefix"))
                .replace("&", "§");
    }

    public static String getModifiedString(String string, Player player) {
        return getModifiedString(string).replace("%player%", player.getName());
    }

    public static String getModifiedString(String string, int time) {
        return getModifiedString(string).replace("%time%", String.valueOf(time));
    }

}
