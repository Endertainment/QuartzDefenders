package ua.Endertainment.QuartzDefenders.Stats;

import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.GamePlayer;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class StatsManager {
	
	public static int getInfo(Player p, String path) {
		return QuartzDefenders.getInstance().getConfigs().getStatsInfo().getInt(p.getUniqueId().toString() + "." + path);
	}
	
	public static void saveInfo(Player p, String path, Object arg) {
		QuartzDefenders.getInstance().getConfigs().getStatsInfo().set(p.getUniqueId().toString() + "." + path, arg);
		QuartzDefenders.getInstance().getConfigs().saveStatsInfo();
	}	
	
	public static int getStatsInfo(Player p, String path) {
		return getInfo(p, "stats." + path);
	}
	public static void saveStatsInfo(Player p, String path, Object arg) {
		saveInfo(p, "stats." + path, arg);
	}
	
	public static double getTeamKDR(GameTeam team) {
		double teamKD = 0;
		for(GamePlayer p : team.getPlayers()) {
			StatsPlayer sp = new StatsPlayer(p.getPlayer());
			teamKD += sp.getKdr();
		}
		teamKD = teamKD / team.getPlayers().size();
		return teamKD;
	}
	
	
}
