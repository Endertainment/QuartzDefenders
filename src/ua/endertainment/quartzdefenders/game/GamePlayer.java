package ua.endertainment.quartzdefenders.game;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import ua.coolboy.quartzdefenders.voting.Vote;

import ua.endertainment.quartzdefenders.utils.ColorFormat;

public class GamePlayer {
	
    private Vote vote;
	private Player player;
	
	public GamePlayer(Player player) {
		this.player = player;
                vote = new Vote(this);
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
		getPlayer().setDisplayName(getPlayer().hasPermission("QuartzDefenders.lobby.colorName") ? ChatColor.AQUA + getPlayer().getName() + ChatColor.RESET : ChatColor.GRAY + getPlayer().getName() + ChatColor.RESET);
		getPlayer().setPlayerListName(getPlayer().hasPermission("QuartzDefenders.lobby.colorName") ? ChatColor.AQUA + getPlayer().getName() + ChatColor.RESET : ChatColor.GRAY + getPlayer().getName() + ChatColor.RESET);
	}
	
	public void teleport(Location loc) {
		getPlayer().teleport(loc);
	}
	
	public void setRespawn(Location loc) {
		getPlayer().setBedSpawnLocation(loc, true);
	}
	
	public String getDefaultDisplayName() {
		return getPlayer().hasPermission("QuartzDefenders.lobby.colorName") ? ChatColor.AQUA + getPlayer().getName() + ChatColor.RESET : ChatColor.GRAY + getPlayer().getName() + ChatColor.RESET;
	}

	public void sendMessage(String s) {
		getPlayer().sendMessage(new ColorFormat(s).format());
	}
        
        public Vote getVote() {
            return vote;
        }
        
        public void resetVote() {
            vote = new Vote(this);
        }
}
