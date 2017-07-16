package ua.Endertainment.QuartzDefenders.Utils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public abstract class Language {

    public static void reload() {
        QuartzDefenders.getInstance().getConfigs().reloadLang();
    }

    public static YamlConfiguration getDefaultLanguage() {
        Reader reader = new InputStreamReader(QuartzDefenders.getInstance().getResource("messages_en.yml"));
        return YamlConfiguration.loadConfiguration(reader);
    }

    public static String getRawString(String path) {
        String s = QuartzDefenders.getInstance().getConfigs().getLang().getString(path);
        if (s == null) {
            s = getDefaultLanguage().getString(path);
            if (s == null) {
                Bukkit.getLogger().log(Level.WARNING, "Could not find string {0}", path);
            }
            return " ";
        }
        return s;
    }

    public static String getString(String path) {
        return new ColorFormat(getRawString(path)).format();
    }

    public static String getString(String path, Replacer... replacer) {
        return new ColorFormat(getRawString(path, replacer)).format();
    }

    public static String getRawString(String path, Replacer... replacer) {
        String s = QuartzDefenders.getInstance().getConfigs().getLang().getString(path);
        if (s == null) {
            s = getDefaultLanguage().getString(path);
            if (s == null) {
                Bukkit.getLogger().log(Level.WARNING, "Could not find string {0}", path);
            }
            return " ";
        }
        
        Bukkit.broadcastMessage(s);
        
        for (Replacer repl : replacer) {
            repl.addString(s);
            Bukkit.broadcastMessage(s);
            s = repl.getReplaced();
            Bukkit.broadcastMessage(s);
        }
        return s;
    }

}
