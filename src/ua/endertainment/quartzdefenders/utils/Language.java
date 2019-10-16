package ua.endertainment.quartzdefenders.utils;

import java.io.InputStreamReader;
import java.io.Reader;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringEscapeUtils;
import ua.endertainment.quartzdefenders.QuartzDefenders;

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
                LoggerUtil.error("Could not find string: "+ path);
                return ChatColor.DARK_RED+path; //return red path, not empty string
            }

        }
        return StringEscapeUtils.unescapeJava(s);
    }

    public static String getString(String path, Object... replace) {
        String s = getString(path);
        int i = 0;
        for (Object st : replace) {
            s = s.replace("{" + i + "}", st.toString());
            i++;
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
        String s = getRawString(path);
        for (Replacer repl : replacer) {
            repl.addString(s);
            s = repl.getReplaced();
        }
        return s;
    }

}
