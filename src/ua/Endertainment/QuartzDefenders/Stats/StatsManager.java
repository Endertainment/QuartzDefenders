package ua.Endertainment.QuartzDefenders.Stats;

import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class StatsManager {
	
	public static int getInfo(Player p, String path) {
		return QuartzDefenders.getInstance().getConfigs().getStatsInfo().getInt(p.getUniqueId().toString() + "." + path);
	}
	
	public static void saveInfo(Player p, String path, Object arg) {
		QuartzDefenders.getInstance().getConfigs().getStatsInfo().set(p.getUniqueId().toString() + "." + path, arg);
		QuartzDefenders.getInstance().getConfigs().saveStatsInfo();
	}
	
	
	
	
}
