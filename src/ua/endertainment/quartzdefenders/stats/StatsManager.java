package ua.endertainment.quartzdefenders.stats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.QuartzDefenders;

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
		gp.sendMessage("&8» &fTop kills: &b" + QuartzDefenders.getInstance().getTopManager().getPlayerKillsPosition(p));
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
	public static void sendStatsInfo(CommandSender sender, UUID id) {
		StatsPlayer sp = new StatsPlayer(id);
		sender.sendMessage("&8-------------------- &6Stats &8--------------------");
		sender.sendMessage("&8» &fName: &b" + Bukkit.getOfflinePlayer(id));
		//sender.sendMessage("&8» &fTop kills: &b" + QuartzDefenders.getInstance().getTopManager().getPlayerKillsPosition(p));
		sender.sendMessage("&8» &fLevel: &b" + sp.getLevel() + "&f(&b" + sp.getPoints() + "&f/&b" + sp.getNextLevelPoints() + "&f)");
		sender.sendMessage("&8» &fCoins: &b" + sp.getCoins());
		sender.sendMessage("&8» &fGames: &b" + sp.getPlayedGames());
		sender.sendMessage("&8» &fWins: &b" + sp.getWins());
		sender.sendMessage("&8» &fKills: &b" + sp.getKills());
		sender.sendMessage("&8» &fDeaths: &b" + sp.getDeaths());
		sender.sendMessage("&8» &fBroken quartz: &b" + sp.getBrokenQuartz());
		sender.sendMessage("&8» &fBroken ores: &b" + sp.getBrokenOres());
		sender.sendMessage("&8» &fPlaced blocks: &b" + sp.getPlacedBlocks());
	}
	
}
