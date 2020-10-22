package ua.endertainment.quartzdefenders;

import java.util.ArrayList;
import java.util.Collections;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.Game;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ua.endertainment.quartzdefenders.events.MobsListener;
import ua.coolboy.quartzdefenders.nms.NMSHandler;
import ua.coolboy.quartzdefenders.shop.ShopInventory;
import ua.endertainment.quartzdefenders.achievements.AchievementsManager;
import ua.endertainment.quartzdefenders.commands.CommandGameBroadcast;
import ua.endertainment.quartzdefenders.commands.CommandQuartzDefenders;
import ua.endertainment.quartzdefenders.commands.game.CommandGame;
import ua.endertainment.quartzdefenders.commands.kit.CommandKit;
import ua.endertainment.quartzdefenders.commands.stats.CommandStats;
import ua.endertainment.quartzdefenders.commands.team.CommandTeam;
import ua.endertainment.quartzdefenders.configuration.database.Database;
import ua.endertainment.quartzdefenders.configuration.Info;
import ua.endertainment.quartzdefenders.events.*;
import ua.endertainment.quartzdefenders.events.game.GameRegisterEvent;
import ua.endertainment.quartzdefenders.kits.KitsManager;
import ua.endertainment.quartzdefenders.stats.PlayerJoinStats;
import ua.endertainment.quartzdefenders.stats.TopManager;
import ua.endertainment.quartzdefenders.configuration.Config;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.ScoreboardLobby;
import ua.endertainment.quartzdefenders.utils.TitleUtil;

//TODO Totems, Kits, Abilities

public class QuartzDefenders extends JavaPlugin {

    private static QuartzDefenders main;

    public static QuartzDefenders getInstance() {
        return main;
    }
    
    private boolean disable = false;
    
    private Config files;
    private TopManager top;
    private Lobby lobby;
    private AchievementsManager achvM;
    private KitsManager kitsManager;
    private Database database;
    private NMSHandler nmsHandler;
    
    private List<Enchantment> blockedEnchantments;

    private final static Set<JavaPlugin> plugins = new HashSet<>();
    private final Set<Game> games = new HashSet<>();
    private final HashMap<UUID, GamePlayer> gamePlayers = new HashMap<>();

    @Override
    public void onEnable() {
        main = this;

        saveDefaultConfig();

        files = new Config(this);
        lobby = new Lobby(this);
        top = new TopManager(this);
        achvM = new AchievementsManager(this);
        kitsManager = new KitsManager(this);
        database = new Database(this);
        new Info(this);
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
            addGame(gameId);
        }

        LoggerUtil.info(LoggerUtil.getPrefix() + Language.getString("logger.games_loaded"));

        /*
		 * Register GamePlayers
         */
        for (Player p : Bukkit.getOnlinePlayers()) {
            gamePlayers.put(p.getUniqueId(), new GamePlayer(p));
            ScoreboardLobby s = new ScoreboardLobby(this, p);
            s.setScoreboard();
            lobby.sendTabList(p);
        }
        if (Bukkit.getSpawnRadius() > 0) {
            Bukkit.setSpawnRadius(0);
        }
        
        blockedEnchantments = new ArrayList<>();
        for(String string : getConfig().getStringList("blocked_enchants")) {
            Enchantment ench = Enchantment.getByName(string.toUpperCase());
            if(ench != null) blockedEnchantments.add(ench);
        }
        nmsHandler = new NMSHandler();
        nmsHandler.getNMS().removeEnchantments(blockedEnchantments);
    }

    @Override
    public void onDisable() {
    	disable = true;
    	
        nmsHandler.getNMS().onDisable();
        for (Game game : games) {
            game.disableGame();
        }
        //show all players on reload
        for(Player player : Bukkit.getOnlinePlayers()) {
            for(Player pl:Bukkit.getOnlinePlayers()) {
                player.showPlayer(this, pl);
            }
        }
        database.close();
        
        main = null;
    }

    static void resetTabList(Player p) {
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

    private GamePlayer getGamePlayer(UUID id) {
        return gamePlayers.get(id);
    }

    public void addGamePlayer(Player p) {
        if (gamePlayers.containsKey(p.getUniqueId())) {
            getGamePlayer(p.getUniqueId()).updatePlayer(p);
            return;
        }
        gamePlayers.put(p.getUniqueId(), new GamePlayer(p));
        database.insertPlayerIfNotExist(p);
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
        new JoinListener(this);
        new QuitListener(this);
        new ItemUseListener(this);
        new InventoryClickListener(this);
        new ChatFormatListener(this);
        new ShopInventory(this);
        new PlayerJoinStats(this);
        new ProjectileDamageListener(this);
        new MobsListener(this);
        new BlockBreakListener(this);
        new DeathListener(this);
        new QuartzBreakListener(this);
        new BlockPlaceListener(this);
        new WorldChangeListener(this);
        new DamageListener(this);
        new DeathMessages(this);
        new ExplodeListener(this);
        new LightningListener(this);
        new FireArrowListener(this);
        new PistonListener(this);
        new CuboidBlockBreakListener(this);
        new ItemCraftListener(this);
        new CommandListener(this);
    }

    /*
     * Logger
     */
    public static void sendInfo(String s) {
        Bukkit.getConsoleSender().sendMessage(s);
    }
    
    public static void hook(JavaPlugin plugin) {
        plugins.add(plugin);
        LoggerUtil.info("Successfully hooked plugin: " + plugin.getName());
    }
    
    public static void unhook(JavaPlugin plugin) {
        if(plugins.remove(plugin)) {
        LoggerUtil.info("Successfully unhooked plugin: " + plugin.getName());   
        }
    }
    
    public static boolean isHooked(JavaPlugin plugin) {
        return plugins.contains(plugin);
    }
    
    public static Set<JavaPlugin> getHookedPlugins() {
        return Collections.unmodifiableSet(plugins);
    }
    /*
	 * Games Management
     */
    public Set<Game> getGames() {
        return games;
    }
    
    public void restartGame(Game game) {
    	if(disable) return;
        String id = game.getGameId();
        deleteGame(game);
        addGame(id);
    }
    
    public void deleteGame(Game gameToDelete) {
        games.remove(gameToDelete);
    }

    public boolean addGame(String gameId) {
        Game game = new Game(gameId);
	 // EVENT
        GameRegisterEvent e = new GameRegisterEvent(game, gameId);
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return false;
        }
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
    
    public Game getGame(World world) {
        for (Game game : games) {
            if (game.getGameWorld().equals(world)) {
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
        for (Game game : games) {
            if (game.getGameId().equalsIgnoreCase(gameID)) {
                return game;
            }
        }
        return null;
    }
    
    public boolean isLobby(World world) {
        return lobby.getWorld().equals(world);
    }

    /*
	 * Another Managers
     */
    public Config getConfigs() {
        return files;
    }
    
    public Database getDatabase() {
        return database;
    }

    public TopManager getTopManager() {
        return top;
    }
    
    public KitsManager getKitManager() {
        return kitsManager;
    }
    
    public NMSHandler getNMSHandler() {
        return nmsHandler;
    }
    
    public Lobby getLobby() {
        return lobby;
    }

    public AchievementsManager getAchievementsManager() {
        return achvM;
    }
}
