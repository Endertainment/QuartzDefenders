package ua.Endertainment.QuartzDefenders.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;

public class ScoreboardLobby {

	private QuartzDefenders plugin;
	private Scoreboard scoreboard;
	private Objective objective;
	private DisplaySlot slot = DisplaySlot.SIDEBAR;
	
	private int index = 0;
	
	private String title = new ColorFormat("&3«&b&l Quartz Defenders &3»").format();
	
	private Player p;
	
	public ScoreboardLobby(QuartzDefenders plugin, Player p) {
		this.plugin = plugin;
		this.p = p;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective("LobbyBar", "MEOW");
		
		objective.setDisplayName(title);
		objective.setDisplaySlot(slot);
		
		scoreBuilder();
	}
	
	public void setScoreboard() {
		p.setScoreboard(scoreboard);
	}
	
	private void scoreBuilder() {
		StatsPlayer sp = new StatsPlayer(p);
		addString("&a ");
		addString("&f Players online: &b" + Bukkit.getOnlinePlayers().size());
		addString("&f Active games: &b" + plugin.getGames().size());
		addString("&1 ");
		addString("&f&m------------------");
		addString("&3 ");
		addString("&f Wins: &b" + sp.getWins());
		addString("&3 &3");
		addString("&f Coins: &b" + sp.getCoins());
		addString("&f Level: &b" + sp.getLevel());
		addString("&3 &3 ");
		addString("&f Top position: &b" + plugin.getTopManager().getPlayerPositionK(p));
		addString("&2 ");
		addString("&f&m------------------&5 ");
		addString("&3«&b&l GovnoCoder.net &3»");		
	}
	
	private void addString(String s) {
		Score score = objective.getScore(new ColorFormat(s).format());
		score.setScore(index);
		index--;
	}
	
	
}
