package ua.Endertainment.QuartzDefenders.Utils;

import org.bukkit.Bukkit;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class Language {

	public static String getString(String path) {
		String s = QuartzDefenders.getInstance().getConfigs().getLang().getString(path);
		if(s == null)  {
                    Bukkit.getLogger().warning("Could not find string " + path);
                    return " ";
                }		
		return s; 
	}
        
	public static String getString(String path, Replacer... replacer) {
		String s = QuartzDefenders.getInstance().getConfigs().getLang().getString(path);
		if(s == null)  {
                    Bukkit.getLogger().warning("Could not find string " + path);
                    return " ";
                }
		for(Replacer repl : replacer) {
			s = new Replacer(s, repl.what(), repl.to()).getReplaced();
		}		
		return s;
	}
	
}
