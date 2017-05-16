package ua.Endertainment.QuartzDefenders.Utils;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class Language {

	public static String getString(String path) {
		String s = QuartzDefenders.getInstance().getConfigs().getLang().getString(path);
		if(s == null) return "Could not find string " + path;		
		return s; 
	}
	public static String getString(String path, Replacer... replacer) {
		String s = QuartzDefenders.getInstance().getConfigs().getLang().getString(path);
		if(s == null) return "Could not find string " + path;
		for(Replacer repl : replacer) {
			s = new Replacer(s, repl.what(), repl.to()).getReplaced();
		}		
		return s;
	}
	
}
