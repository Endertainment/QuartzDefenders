package ua.Endertainment.QuartzDefenders.Utils;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class Language {

	public static void reload() {
		QuartzDefenders.getInstance().getConfigs().reloadLang();
	}
	
	public static String getRawString(String path) {
		String s = QuartzDefenders.getInstance().getConfigs().getLang().getString(path);
		if(s == null)  {
                    Bukkit.getLogger().log(Level.WARNING, "Could not find string {0}", path);
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
		if(s == null)  {
                    Bukkit.getLogger().log(Level.WARNING, "Could not find string {0}", path);
                    return " ";
                }
		for(Replacer repl : replacer) {
			s = new Replacer(s, repl.what(), repl.to()).getReplaced();
		}		
		return s;
	}
	
}
