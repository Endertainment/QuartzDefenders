package ua.Endertainment.QuartzDefenders.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;

public class ColorFormat {

	private String normal = null;
	private String formated = null;
	
	public ColorFormat(String s) {
		normal = s;
		formated = ChatColor.translateAlternateColorCodes('&', s);
	}	
	public String format() {
		return formated;
	}
	public String normal() {
		return normal;
	}
	
	private ArrayList<String> formatedList;
	
	public ColorFormat(ArrayList<String> list) {
		formatedList = new ArrayList<String>();
		for(String s : list) {
			formatedList.add(ChatColor.translateAlternateColorCodes('&', s));
		}
	}
	public ArrayList<String> getFormatedList() {
		return formatedList;
	}
}
