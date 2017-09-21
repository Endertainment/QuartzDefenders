package ua.Endertainment.QuartzDefenders;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

public class GamePlayer {
	
	private Player player;
	
	public GamePlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void updatePlayer(Player p) {
		this.player = p;
	}
	
	public String getName() {
		return getPlayer().getName();
	}
	
	public String getDisplayName() {
		return getPlayer().getDisplayName();
	}
	
	public void setScoreboard(Scoreboard board) {
		getPlayer().setScoreboard(board);
	}
	
	public void setDisplayName(ChatColor color) {
		getPlayer().setDisplayName(color + getPlayer().getName() + ChatColor.RESET);
		getPlayer().setPlayerListName(color + getPlayer().getName() + ChatColor.RESET);
	}
	public void resetDisplayName() {
		getPlayer().setDisplayName(ChatColor.GRAY + getPlayer().getName() + ChatColor.RESET);
		getPlayer().setPlayerListName(ChatColor.GRAY + getPlayer().getName() + ChatColor.RESET);
	}
	
	public void teleport(Location loc) {
		getPlayer().teleport(loc);
	}

	public void sendMessage(String s) {
		getPlayer().sendMessage(new ColorFormat(s).format());
	}
}
