package nl.sundeep.simplevanish.utils;

import org.bukkit.ChatColor;

public class Format {
    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
