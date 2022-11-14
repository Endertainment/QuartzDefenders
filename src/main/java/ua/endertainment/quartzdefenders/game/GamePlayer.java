package ua.endertainment.quartzdefenders.game;

import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
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
        
        public void reset(GameTeam team) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setVelocity(new Vector(0, 0, 0));
            setRespawn(team.getSpawnLocation());
            
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.setTotalExperience(0);
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
        
    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other instanceof GamePlayer) {
            GamePlayer gp = (GamePlayer) other;
            return gp.getPlayer().getUniqueId().equals(player.getUniqueId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.player.getUniqueId());
        return hash;
    }
}
