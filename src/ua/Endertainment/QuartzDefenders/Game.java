package ua.Endertainment.QuartzDefenders;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import ua.Endertainment.QuartzDefenders.Events.Game.GameStateChangeEvent;
import ua.Endertainment.QuartzDefenders.Events.Game.PlayerJoinGameEvent;
import ua.Endertainment.QuartzDefenders.Items.QItems;
import ua.Endertainment.QuartzDefenders.Kits.Kit;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;
import ua.Endertainment.QuartzDefenders.Utils.MapManager;
import ua.Endertainment.QuartzDefenders.Utils.ScoreboardLobby;

public class Game {
	private String[] realTeams = {"RED","BLUE","GREEN","YELLOW","MAGENTA","AQUA","GRAY","WHITE"};
	
	private boolean loadSuccess = false;
	
	private String id;
	private String gameName;
	private String worldName;
	private int playersInTeam;
	private int minPlayers;

	private Location mapCenter;
	
	private int playerRespawnTime;
	private int quartzHealth;
	private int teamsCount;
	
	private Scoreboard gameScoreboard;
	
	private Map<String, GameTeam> teams = new HashMap<>();
	
	private Map<GameTeam, Location> shopLocations = new HashMap<>();
        private Map<Integer, Location> alchemistsLocations = new HashMap<>();
	
	private Map<GameTeam, GameQuartz> quartzs = new HashMap<>();
	
	private Set<GamePlayer> spectators = new HashSet<>();
	private Set<GamePlayer> gameLobbyPlayers = new HashSet<>();
	
	private Map<GamePlayer, Kit> kits = new HashMap<>();
		
	private GameState state = GameState.LOBBY;
	
	@SuppressWarnings("unchecked")
	public Game(String id) {
		FileConfiguration config = QuartzDefenders.getInstance().getConfigs().getGameInfo();
		
		gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		this.id = id;
		this.gameName = new ColorFormat(config.getString("Games." + this.id + ".game_name")).format();
		this.worldName = config.getString("Games." + this.id + ".world_name");
		this.playersInTeam = config.getInt("Games." + this.id + ".players_in_team");
		this.playerRespawnTime = config.getInt("Games." + this.id + ".respawn_time");
		this.quartzHealth = config.getInt("Games." + this.id + ".quartz_health");
		this.teamsCount = config.getInt("Games." + this.id + ".teams_count");
		
		this.minPlayers = config.getInt("Games." + this.id + ".min_players");
		

		MapManager mapManager = new MapManager(worldName);
		try {
			mapManager.resetMap();
		} catch(Exception e) {
			Bukkit.broadcastMessage(GameMsg.gameMessage("Error", "&cCould not load map in game " + gameName));
			return;
		}		
		if(!mapManager.isSuccess()) return;
		
		this.mapCenter = new Location(Bukkit.getWorld(worldName), 
				config.getDouble("Games." + this.id + ".map_center.x") + 0.5,
				config.getDouble("Games." + this.id + ".map_center.y"),
				config.getDouble("Games." + this.id + ".map_center.z") + 0.5);
		
		
		int i = 0;
		for(String team : (List<String>)config.getList("Games." + this.id + ".teams")) {
			if(i != teamsCount) {
				if(isTeamReal(team)) {					
					
					Location spawn = new Location(Bukkit.getWorld(worldName), 
							config.getDouble("Games." + this.id + ".locations." + team + ".spawn.x" + 0.5), 
							config.getDouble("Games." + this.id + ".locations." + team + ".spawn.y"), 
							config.getDouble("Games." + this.id + ".locations." + team + ".spawn.z") + 0.5);
					
					Location quartz = new Location(Bukkit.getWorld(worldName), 
							config.getDouble("Games." + this.id + ".locations." + team + ".quartz.x" + 0.5), 
							config.getDouble("Games." + this.id + ".locations." + team + ".quartz.y"), 
							config.getDouble("Games." + this.id + ".locations." + team + ".quartz.z") + 0.5);
					
					Location shop = new Location(Bukkit.getWorld(worldName), 
							config.getDouble("Games." + this.id + ".locations." + team + ".shop.x") + 0.5, 
							config.getDouble("Games." + this.id + ".locations." + team + ".shop.y"), 
							config.getDouble("Games." + this.id + ".locations." + team + ".shop.z") + 0.5);
                                        Location alchemist = new Location(Bukkit.getWorld(worldName), 
							config.getDouble("Games." + this.id + ".locations." + team + ".alchemists.x") + 0.5, 
							config.getDouble("Games." + this.id + ".locations." + team + ".alchemists.y"), 
							config.getDouble("Games." + this.id + ".locations." + team + ".alchemists.z") + 0.5);
                                        int alchemistRadius = config.getInt("Games." + this.id + ".locations." + team + ".alchemists.r");
					
					teams.put(team, new GameTeam(this, team, getChatColor(team), playersInTeam, spawn, gameScoreboard));
										
					quartzs.put(getTeam(team), new GameQuartz(this, getTeam(team), quartz, quartzHealth));
					
					shopLocations.put(getTeam(team), shop);
                                        
                                        alchemistsLocations.put(alchemistRadius, alchemist);
                                        
					
					i++;
				} else Bukkit.broadcastMessage(GameMsg.gameMessage(gameName, "&cTeam \"" + team + "\" is not real!"));
			} else break;
		}
		
		Bukkit.broadcastMessage(GameMsg.gameMessage(gameName, "&aLoaded " + i + " teams of " + teamsCount));
		
		
		loadSuccess = true;
	}
	public boolean isLoadSuccess() {
		return loadSuccess;
	}
	
	public String getGameName() {
		return gameName;
	}
	public String getGameId() {
		return id;
	}
	
	public Map<String, GameTeam> getTeams() {
		return teams;
	}
	public GameTeam getTeam(String team) {
		return teams.get(team);
	}
	public GameTeam getTeam(Player p) {
	    for(GameTeam team : teams.values()) {
	       if(team.contains(QuartzDefenders.getInstance().getGamePlayer(p))) return team;
	    }
	   return null;
	}
	public Collection<Location> getShopLocations() { 
		return shopLocations.values();
	}
        public Map<Integer, Location> getAlchemicsLocations() { 
		return alchemistsLocations;
	}
	public String getWorldName() {
		return worldName;
	}
	public int getPlayersInTeam() {
		return playersInTeam;
	}
	public int getTeamsCount() {
		return teamsCount;
	}
	
	public int getPlayersRespawnTime() {
		return playerRespawnTime;
	}
	public void setKit(GamePlayer p, Kit kit) {
		kits.put(p, kit);
	}
	private boolean isTeamReal(String s) {
		for(String ss : realTeams) {
			if(ss.equals(s)) return true;
		}		
		return false;
	}
	
	public Set<GamePlayer> getPlayers() {
		return gameLobbyPlayers;		
	}
	public Set<GamePlayer> getSpectators() {
		return spectators;
	}
	public boolean containsPlayer(GamePlayer player) {
		return gameLobbyPlayers.contains(player);
	}
	
	public void joinGame(GamePlayer player) {
		
		if(gameLobbyPlayers.contains(player)) return;
		
		// EVENT
		PlayerJoinGameEvent e = new PlayerJoinGameEvent(this, player);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled()) return;
		
		if(isGameState(GameState.LOBBY)) {
			
			gameLobbyPlayers.add(player);
			this.broadcastMessage(GameMsg.gameMessage("Join", "&aPlayer &r" + player.getDisplayName() + "&a joined the game"));
			if(gameLobbyPlayers.size() >= minPlayers) {
				setGameState(GameState.WAITING);
				for(GamePlayer p : gameLobbyPlayers) {
					p.getPlayer().getInventory().clear();
					p.getPlayer().teleport(mapCenter);
					p.getPlayer().getInventory().setItem(2, QItems.itemTeamChoose(this));
					p.getPlayer().getInventory().setItem(6, QItems.itemKitsChoose());
					p.getPlayer().getInventory().setItem(8, QItems.itemQuit());
					p.sendMessage(GameMsg.gameMessage("Game", "&7Choose a &ateams&7 and wait a &agame &7start"));
				}
			}
			
		}
		
	}
	public void startGame() {
		
	}
	
	public void quitGame(GamePlayer player) {
		gameLobbyPlayers.remove(player);
		this.broadcastMessage(GameMsg.gameMessage("Quit", "&aPlayer &r" + player.getDisplayName() + "&a quit the game"));
		player.teleport(QuartzDefenders.getInstance().getLobby().getLocation());
		
		Player p = player.getPlayer();
		p.setDisplayName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
		p.setPlayerListName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
		
		if(p.hasPermission("QuartzDefenders.lobby.colorName")) {
			p.setDisplayName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
			p.setPlayerListName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
		}
		
		p.getInventory().clear();
		
		p.getInventory().setItem(1, QItems.itemGamesChoose());
		p.getInventory().setItem(4, QItems.itemStats());
		p.getInventory().setItem(7, QItems.itemHidePlayers(QuartzDefenders.getInstance().getLobby().getHides().contains(p)));
		p.getInventory().setItem(8, QItems.itemLobbyShop());
		
		ScoreboardLobby s = new ScoreboardLobby(QuartzDefenders.getInstance(), p);
		s.setScoreboard();
	}
	
	public void teleportAllToSpawnLocs() {
		for(GameTeam team : teams.values()) {
			team.teleportToSpawnLocation();
		}
	}
	
	public void broadcastMessage(String message) {
		for(GamePlayer p : gameLobbyPlayers) {
			p.sendMessage(message);
		}
	}
	
	public GameState getGameState() {
		return state;
	}
	public void setGameState(GameState state) {
		// Event
		GameStateChangeEvent e = new GameStateChangeEvent(this, this.state, state);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled()) return;
		
		this.state = state;
	}
	public boolean isGameState(GameState state) {
		return getGameState() == state;
	}
	
	public enum GameState { 
		LOBBY, WAITING, STARTING, ACTIVE, ENDING
	}
	
	private ChatColor getChatColor(String name) {
		switch(name) 
		{
			case "RED": 	return ChatColor.RED;
			case "BLUE": 	return ChatColor.BLUE;
			case "GREEN":	return ChatColor.GREEN;
			case "YELLOW": 	return ChatColor.YELLOW;
			case "MAGENTA": return ChatColor.DARK_PURPLE;
			case "AQUA": 	return ChatColor.AQUA;
			case "GRAY": 	return ChatColor.DARK_GRAY;
			case "WHITE":	return ChatColor.WHITE;
		}
		
		return ChatColor.GRAY;
	}
}
