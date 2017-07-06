package ua.Endertainment.QuartzDefenders.Stats;

import java.util.UUID;

import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.GamePlayer;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class StatsManager {
	
	public static int getInfo(UUID id, String path) {
		return QuartzDefenders.getInstance().getConfigs().getStatsInfo().getInt(id.toString() + "." + path);
	}
	
	public static void saveInfo(UUID id, String path, Object arg) {
		QuartzDefenders.getInstance().getConfigs().getStatsInfo().set(id.toString() + "." + path, arg);
		QuartzDefenders.getInstance().getConfigs().saveStatsInfo();
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
	
	public static void sendStatsInfo(Player p, UUID id) {
		GamePlayer gp = QuartzDefenders.getInstance().getGamePlayer(p);
		StatsPlayer sp = new StatsPlayer(id);
		gp.sendMessage("&8-------------------- &6Stats &8--------------------");
		gp.sendMessage("&8» &fName: &b" + p.getName());
		gp.sendMessage("&8» &fTop kills: &b" + QuartzDefenders.getInstance().getTopManager().getPlayerPositionK(p));
		//gp.sendMessage("&8» &fTop kills: &b" + QuartzDefenders.getInstance().getTopManager().getPlayerPositionK(p));
		gp.sendMessage("&8» &fLevel: &b" + sp.getLevel() + "&f(&b" + sp.getPoints() + "&f/&b" + sp.getNextLevelPoints() + "&f)");
		gp.sendMessage("&8» &fCoins: &b" + sp.getCoins());
		gp.sendMessage("&8» &fGames: &b" + sp.getPlayedGames());
		gp.sendMessage("&8» &fWins: &b" + sp.getWins());
		gp.sendMessage("&8» &fKills: &b" + sp.getKills());
		gp.sendMessage("&8» &fDeaths: &b" + sp.getDeaths());
		gp.sendMessage("&8» &fBroken quartz: &b" + sp.getBrokenQuartz());
		gp.sendMessage("&8» &fBroken ores: &b" + sp.getBrokenOres());
		gp.sendMessage("&8» &fPlaced blocks: &b" + sp.getPlacedBlocks());
	}
	
}
