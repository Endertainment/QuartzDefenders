package ua.endertainment.quartzdefenders.Utils;

import java.util.ArrayList;
import java.util.List;

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
	
	private List<String> formatedList;
	
	public ColorFormat(List<String> list) {
		formatedList = new ArrayList<>();
		for(String s : list) {
			formatedList.add(ChatColor.translateAlternateColorCodes('&', s));
		}
	}
	public List<String> getFormatedList() {
		return formatedList;
	}
}
