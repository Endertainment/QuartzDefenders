package ua.endertainment.quartzdefenders.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public abstract class LoggerUtil {

    private static String prefix = new ColorFormat("&b&lQuartz &3\u00BB &8: &f").format();
    private final static ConsoleCommandSender LOGGER = Bukkit.getConsoleSender();

    public static String getPrefix() {
        return prefix;
    }

    public static void logError(String message) {
        LOGGER.sendMessage(new ColorFormat(prefix + "&4 " + Language.getString("logger.error") + " &8: &4" + message).format());
    }

    public static void logInfo(String message) {
        LOGGER.sendMessage(new ColorFormat(prefix + "&7 " + Language.getString("logger.info") + " &8: &7" + message).format());
    }

    public static String gameMessage(String type, String message) {
        return new ColorFormat(getPrefix() + "&7" + type + " &8: &7" + message).format();
    }

}
