package ua.Endertainment.QuartzDefenders;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

public class GamePlayer {

	private Player player;
	private StatsPlayer statsPlayer;
	
	public GamePlayer(Player player) {
		this.player = player;
		this.statsPlayer = new StatsPlayer(player);
	}

	public Player getPlayer() {
		return player;
	}

	public String getDisplayName() {
		return player.getDisplayName();
	}
	
	public void setDisplayName(ChatColor color) {
		player.setDisplayName(color + player.getName() + ChatColor.RESET);
		player.setPlayerListName(color + player.getName() + ChatColor.RESET);
	}
	public void resetDisplayName() {
		player.setDisplayName(new ColorFormat("&7").format() + player.getName());
		player.setPlayerListName(new ColorFormat("&7").format() + player.getName());
	}
	
	public void teleport(Location loc) {
		getPlayer().teleport(loc);
	}

	public void sendMessage(String s) {
		player.sendMessage(new ColorFormat(s).format());
	}
}
