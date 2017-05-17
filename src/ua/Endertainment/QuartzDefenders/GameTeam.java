package ua.Endertainment.QuartzDefenders;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@SuppressWarnings("deprecation")
public class GameTeam {

	private Game game;
	private String name;
	private int playersInTeam;
	private Scoreboard scoreboard;
	private Location spawnLocation;
	private ChatColor color;
	private Team team;
	private Set<GamePlayer> players = new HashSet<>();
	
	public GameTeam(Game game, String name, ChatColor color, int playersInTeam, Location spawnLocation, Scoreboard scoreboard) {
		this.game = game;
		this.name = name;
		this.playersInTeam = playersInTeam;
		this.scoreboard = scoreboard;
		this.color = color;
		this.spawnLocation = spawnLocation;
		
		this.team = this.scoreboard.registerNewTeam(this.name);
		this.team.setAllowFriendlyFire(false);
		this.team.setCanSeeFriendlyInvisibles(true);
		this.team.setPrefix(color.toString());
	}
	public Game getGame() {
		return game;
	}
	public int intPlayersInTeam() {
		return playersInTeam;
	}
	public boolean addPlayer(GamePlayer player) {
		if(team.getPlayers().contains(player.getPlayer())) {
			return false;
		} else if(team.getPlayers().size() < playersInTeam){
			team.addPlayer(player.getPlayer());
			players.add(player);
			player.setDisplayName(color);
			return true;
		}		
		return false;
	}
	public boolean removePlayer(GamePlayer player) {
		if(team.getPlayers().contains(player.getPlayer())) {
			team.removePlayer(player.getPlayer());
			players.remove(player);
			player.resetDisplayName();
			return true;
		}
		return false;
	}
	public void sendMessage(String s) {
		for(GamePlayer p : players) {
			p.sendMessage(s);
		}
	}
	public boolean contains(GamePlayer player) {
		return getPlayers().contains(player);
	}
	public ChatColor getColor() {
		return color;
	}
	public String getDefName() {
		return name;
	}
	public String getName() {
		return color + name;
	}
	public int getPlayersSize() {
		return players.size();
	}
	public Set<GamePlayer> getPlayers() {
		return players;
	}
	public void teleportToSpawnLocation() {
		for(GamePlayer player : players) {
			player.teleport(spawnLocation);
		}
	}
	
}
