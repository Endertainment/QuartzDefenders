package ua.Endertainment.QuartzDefenders;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

public class GamePlayer {

	private UUID playerId;
	
	public GamePlayer(Player player) {
		this.playerId = player.getUniqueId();
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerId);
	}
	
	public String getDisplayName() {
		return getPlayer().getDisplayName();
	}
	
	public void setScoreboard(Scoreboard board) {
		getPlayer().setScoreboard(board);
	}
	
	public void setDisplayName(ChatColor color) {
		Bukkit.getPlayer(playerId).setDisplayName(color + getPlayer().getName() + ChatColor.RESET);
		Bukkit.getPlayer(playerId).setPlayerListName(color + getPlayer().getName() + ChatColor.RESET);
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
