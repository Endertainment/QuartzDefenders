package ua.Endertainment.QuartzDefenders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ua.Coolboy.QuartzDefenders.Shop.ShopInventory;
import ua.Coolboy.QuartzDefenders.Turrets.Turret;
import ua.Coolboy.QuartzDefenders.Turrets.TurretInventory;
import ua.Coolboy.QuartzDefenders.Turrets.TurretListener;

import ua.Endertainment.QuartzDefenders.Commands.TempCommandJoin;
import ua.Endertainment.QuartzDefenders.Events.ChatFormatEvent;
import ua.Endertainment.QuartzDefenders.Events.InvClickEvent;
import ua.Endertainment.QuartzDefenders.Events.ItemsUseEvent;
import ua.Endertainment.QuartzDefenders.Events.JoinEvent;
import ua.Endertainment.QuartzDefenders.GUI.GamesGUI;
import ua.Endertainment.QuartzDefenders.Stats.PlayerJoinStats;
import ua.Endertainment.QuartzDefenders.Stats.TopManager;
import ua.Endertainment.QuartzDefenders.Utils.FilesUtil;

public class QuartzDefenders extends JavaPlugin {

	private static QuartzDefenders main;
	public static QuartzDefenders getInstance() {
		return main;
	}
	
	private FilesUtil files;
	private GamesGUI gamesGUI;
	private TopManager top;
	private Lobby lobby;
	
	private Set<Game> games = new HashSet<Game>();
	private HashMap<UUID, GamePlayer> gamePlayers = new HashMap<>();
	
        @Override
	public void onEnable() {
		main = this;
		
		saveDefaultConfig();
		getConfig();
		
		files = new FilesUtil(this);
		gamesGUI = new GamesGUI(this);
		lobby = new Lobby(this);
		top = new TopManager(this);
		
		if(!isEnabled()) return;
		
		registerEvents();
		registerCommands();
				
		for(String gameId : getConfigs().getGameInfo().getConfigurationSection("Games").getKeys(false)) {
			Game game = new Game(gameId);
			if(game.isLoadSuccess()) games.add(game);
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			gamePlayers.put(p.getUniqueId(), new GamePlayer(p));
		}
		
	}
	
	public GamePlayer getGamePlayer(Player p) {
		return gamePlayers.get(p.getUniqueId());
	}
	public void addGamePlayer(Player p) {
		gamePlayers.put(p.getUniqueId(), new GamePlayer(p));
	}
	
	private void registerCommands() {
		new TempCommandJoin(this);
		
	}

	private void registerEvents() {
		new JoinEvent(this);
		new ItemsUseEvent(this);
		new InvClickEvent(this);
		new ChatFormatEvent(this);
		new ShopInventory(this);
		new PlayerJoinStats(this);
                new TurretListener(this);
                new TurretInventory(this);
	}

	public Set<Game> getGames() {
		return games;
	}
	public void deleteGame(Game gameToDelete) {
		games.remove(gameToDelete);
	}
	public boolean addGame(String gameId) {
		Game game = new Game(gameId);
		if(game.isLoadSuccess()) games.add(game); 
		return game.isLoadSuccess();
	}
	public Game getGame(String gameName) {
        for (Game game : games) {
            if (game.getGameName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }
        return null;
    }
	public Game getGame(Player player) {
		for(Game game : games) {
			if(game.containsPlayer(getGamePlayer(player))) {
				return game;
			}
		}
		return null;
	}
	
        @Override
	public void onDisable() {
                List<Entity> stands;
                stands = Turret.getStands();
                for(Entity st : stands) {
                    stands.remove(st);
                }
                main = null;
	}
	
	public FilesUtil getConfigs() {
		return files;
	}
	public GamesGUI getGamesGUI() {
		return gamesGUI;
	}
	public TopManager getTopManager() {
		return top;
	}
	public Lobby getLobby() {
		return lobby;
	}
	
}
