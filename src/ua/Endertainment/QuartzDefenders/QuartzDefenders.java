package ua.Endertainment.QuartzDefenders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ua.Coolboy.QuartzDefenders.Mobs.MobsListener;
import ua.Coolboy.QuartzDefenders.Shop.ShopInventory;
import ua.Coolboy.QuartzDefenders.Turrets.TurretInventory;
import ua.Coolboy.QuartzDefenders.Turrets.TurretListener;
import ua.Endertainment.QuartzDefenders.Commands.CommandGameBroadcast;
import ua.Endertainment.QuartzDefenders.Commands.CommandQuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.Game.CommandGame;
import ua.Endertainment.QuartzDefenders.Commands.Kit.CommandKit;
import ua.Endertainment.QuartzDefenders.Commands.Stats.CommandStats;
import ua.Endertainment.QuartzDefenders.Commands.Team.CommandTeam;
import ua.Endertainment.QuartzDefenders.Events.BreakBlockEvent;
import ua.Endertainment.QuartzDefenders.Events.ChatFormatEvent;
import ua.Endertainment.QuartzDefenders.Events.DamageEvent;
import ua.Endertainment.QuartzDefenders.Events.DeathEvent;
import ua.Endertainment.QuartzDefenders.Events.DeathMessages;
import ua.Endertainment.QuartzDefenders.Events.ExplodeEvent;
import ua.Endertainment.QuartzDefenders.Events.InvClickEvent;
import ua.Endertainment.QuartzDefenders.Events.ItemsUseEvent;
import ua.Endertainment.QuartzDefenders.Events.JoinEvent;
import ua.Endertainment.QuartzDefenders.Events.PlaceBlockEvent;
import ua.Endertainment.QuartzDefenders.Events.QuartzBreakEvent;
import ua.Endertainment.QuartzDefenders.Events.QuitEvent;
import ua.Endertainment.QuartzDefenders.Events.WorldChangeEvent;
import ua.Endertainment.QuartzDefenders.Events.Game.GameRegisterEvent;
import ua.Endertainment.QuartzDefenders.Items.QItems;
import ua.Endertainment.QuartzDefenders.Stats.PlayerJoinStats;
import ua.Endertainment.QuartzDefenders.Stats.TopManager;
import ua.Endertainment.QuartzDefenders.Utils.FilesUtil;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;
import ua.Endertainment.QuartzDefenders.Utils.ScoreboardLobby;
import ua.Endertainment.QuartzDefenders.Utils.TitleUtil;

public class QuartzDefenders extends JavaPlugin {

    private static QuartzDefenders main;

    public static QuartzDefenders getInstance() {
        return main;
    }
    
    private final String[] devs = {"_Endertainment_", "Cool_boy"};

    private FilesUtil files;
    private TopManager top;
    private Lobby lobby;

    private final Set<Game> games = new HashSet<>();
    private final HashMap<UUID, GamePlayer> gamePlayers = new HashMap<>(); 

    @Override
    public void onEnable() {
        main = this;
        
        saveDefaultConfig();
        getConfig();

        files = new FilesUtil(this);
        lobby = new Lobby(this);
        top = new TopManager(this);
        
        /*
		 * Prevent an exceptions when plugin is disabled
         */
        if (!isEnabled()) {
            return;
        }
        /*
		 * Register Events&Commands
         */
        
        registerEvents();
        registerCommands();

        /*
		 * Register games
         */
        for (String gameId : getConfigs().getGameInfo().getConfigurationSection("Games").getKeys(false)) {
            Game game = new Game(gameId);

            // EVENT
            GameRegisterEvent e = new GameRegisterEvent(game, gameId);
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                return;
            }

            if (game.isLoadSuccess()) {
                games.add(game);
            }
        }

        Bukkit.broadcastMessage(GameMsg.gameMessage("Info", "&aAll games successfully loaded"));

        /*
		 * Register GamePlayers
         */
        for (Player p : Bukkit.getOnlinePlayers()) {
            gamePlayers.put(p.getUniqueId(), new GamePlayer(p));
        }

    }

    @Override
    public void onDisable() {        
        
        for(Game game : games) {
        	game.disableGame();
        }
        
        for(Player p : Bukkit.getOnlinePlayers()) {
        	p.setDisplayName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
    		p.setPlayerListName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
    		
    		if(p.hasPermission("QuartzDefenders.lobby.colorName")) {
    			p.setDisplayName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
    			p.setPlayerListName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
    		}
    		
    		for(String s : getDevs()) {
    			if(s.equalsIgnoreCase(p.getName())) {
    				p.setDisplayName(ChatColor.DARK_RED + p.getName() + ChatColor.RESET);
    				p.setPlayerListName(ChatColor.DARK_RED + p.getName() + ChatColor.RESET);
    			}
    		}
    		
    		p.setGameMode(GameMode.ADVENTURE);
    		
    		p.setHealth(20);
    		p.setFoodLevel(20);
    		
    		p.getInventory().clear();
    		
    		p.getInventory().setItem(0, QItems.itemGamesChoose());
    		p.getInventory().setItem(4, QItems.itemStats());
    		p.getInventory().setItem(7, QItems.itemHidePlayers(getLobby().getHides().contains(p)));
    		p.getInventory().setItem(8, QItems.itemLobbyShop());
    		
    		ScoreboardLobby s = new ScoreboardLobby(this, p);
    		s.setScoreboard();
        }
        main = null;
    }

    public static void resetTabList(Player p) {
    	String header = "";
		String footer = "";
		String n = "\n";
						
		header = " " + n
				+ "&3\u00AB &b&lPlayCraft.COM.UA &3\u00BB" + n 
				+ " ";
		footer = " ";
		
		TitleUtil.sendTabTitle(p, header, footer);
    }
    
    /*
	 * GamePlayers Management
     */
    public GamePlayer getGamePlayer(Player p) {
        return gamePlayers.get(p.getUniqueId());
    }

    public void addGamePlayer(Player p) {
        if (gamePlayers.containsKey(p.getUniqueId())) {
            return;
        }
        gamePlayers.put(p.getUniqueId(), new GamePlayer(p));
    }

    /*
	 * Events & Commands 
     */
    private void registerCommands() {
        new CommandTeam(this);
        new CommandStats(this);
        new CommandGame(this);
        new CommandGameBroadcast(this);
        new CommandQuartzDefenders(this);
        new CommandKit(this);
    }

    private void registerEvents() {
        new JoinEvent(this);
        new QuitEvent(this);
        new ItemsUseEvent(this);
        new InvClickEvent(this);
        new ChatFormatEvent(this);
        new ShopInventory(this);
        new PlayerJoinStats(this);
        new TurretListener(this);
        new TurretInventory(this);
        new MobsListener(this);
        new BreakBlockEvent(this);
        new DeathEvent(this);
        new QuartzBreakEvent(this);
        new PlaceBlockEvent(this);
        new WorldChangeEvent(this);
        new DamageEvent(this);
        new DeathMessages(this);
        new ExplodeEvent(this);
    }
    
    /*
     * Logger
     */
    public static void sendInfo(String s) {
    	Bukkit.getConsoleSender().sendMessage(s);
    }

    /*
	 * Games Management
     */
    public Set<Game> getGames() {
        return games;
    }

    public void deleteGame(Game gameToDelete) {
        games.remove(gameToDelete);
    }

    public boolean addGame(String gameId) {
        Game game = new Game(gameId);
        if (game.isLoadSuccess()) {
            games.add(game);
        }
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
        for (Game game : games) {
            if (game.containsPlayer(getGamePlayer(player))) {
                return game;
            }
        }
        return null;
    }

    public Game getGame(String gameID, boolean arg) {
    	for(Game game : games) {
    		if(game.getGameId().equalsIgnoreCase(gameID)) {
    			return game;
    		}
    	}
    	return null;
    }
    /*
	 * Another Managers
     */
    public FilesUtil getConfigs() {
        return files;
    }



    public TopManager getTopManager() {
        return top;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public String[] getDevs() {
    	return devs;
    }

}
