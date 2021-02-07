package ua.endertainment.quartzdefenders.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;

public class LobbySidebar {

	private QuartzDefenders plugin;
	private Scoreboard scoreboard;
	private Objective objective;
	private DisplaySlot slot = DisplaySlot.SIDEBAR;
	
	private int index = 15;
	
	private String title = new ColorFormat("&3«&b&l Quartz Defenders &3»").format();
	
	private Player p;
	
	public LobbySidebar(QuartzDefenders plugin, Player p) {
		this.plugin = plugin;
		this.p = p;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective("LobbyBar", "MEOW", title);
		
		objective.setDisplayName(title);
		objective.setDisplaySlot(slot);

		scoreBuilder();
	}
	
	public void setScoreboard() {
		if(p.getWorld() == plugin.getLobby().getWorld()) p.setScoreboard(scoreboard);
	}
	
	private void scoreBuilder() {
		StatsPlayer sp = new StatsPlayer(p);
		List<String> l = Language.getRawStringList("scoreboard.lobby");
		addString(new Replacer("{0}", p.getDisplayName()).addString(l.get(0)).getReplaced());
		addString(new Replacer("{0}", Bukkit.getOnlinePlayers().size()).addString(l.get(1)).getReplaced());
		addString(new Replacer("{0}", plugin.getGames().size()).addString(l.get(2)).getReplaced());
		addString(l.get(3));
		addString(l.get(4));
		addString(l.get(5));
		addString(new Replacer("{0}", sp.getWins()).addString(l.get(6)).getReplaced());
		addString(l.get(7));
		addString(new Replacer("{0}", plugin.getTopManager().getPlayerKillsPosition(p)).addString(l.get(8)).getReplaced());
		addString(l.get(9));
		addString(l.get(10));
		addString(l.get(11));
		/*
		addString("&f Hi, " + p.getDisplayName());
		addString("&f Players online: &b" + Bukkit.getOnlinePlayers().size());
		addString("&f Active games: &b" + plugin.getGames().size());
		addString("&1 ");
		addString("&f&m------------------");
		addString("&3 ");
		addString("&f Wins: &b" + sp.getWins());
		addString("&3 &3 ");
		addString("&f Top position: &b" + plugin.getTopManager().getPlayerKillsPosition(p));
		addString("&2 ");
		addString("&f&m------------------&5 ");
		addString("&3«&b&l Playcraft.com.ua &3»");
		*/
	}
	
	private void addString(String s) {
		Score score = objective.getScore(new ColorFormat(s).format());
		score.setScore(index);
		index--;
	}
	
	
}
