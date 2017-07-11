package ua.Endertainment.QuartzDefenders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import ua.Coolboy.QuartzDefenders.Shop.ShopEntity;
import ua.Endertainment.QuartzDefenders.Events.Game.GameStartEvent;

import ua.Endertainment.QuartzDefenders.Events.Game.GameStateChangeEvent;
import ua.Endertainment.QuartzDefenders.Events.Game.PlayerJoinGameEvent;
import ua.Endertainment.QuartzDefenders.Items.QItems;
import ua.Endertainment.QuartzDefenders.Kits.Kit;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.BCub;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.Cuboid;
import ua.Endertainment.QuartzDefenders.Utils.FireworkUtil;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;
import ua.Endertainment.QuartzDefenders.Utils.MapManager;
import ua.Endertainment.QuartzDefenders.Utils.ScoreboardLobby;
import ua.Endertainment.QuartzDefenders.Utils.TitleUtil;

public class Game {

    private String[] validTeams = {"RED", "BLUE", "GREEN", "YELLOW", "MAGENTA", "AQUA", "GRAY", "WHITE"};

    private Game game;

    private boolean loadSuccess = false;

    private boolean lockGame = false;

    private String id;
    private String gameName;
    private String teckWorldName;
    private String colorWorldName;
    private int playersInTeam;
    private int minPlayers;
    private int turretLivetime;

    private MapManager mapManager;
    private World map;

    private Location mapSpawn;
    private int buildHeight, buildRadius;

    private int playerRespawnTime;
    private int quartzHealth;
    private int teamsCount;
    private BalanceType balanceType = BalanceType.DEFAULT_BALANCE;

    private Scoreboard gameScoreboard;

    private Map<String, GameTeam> teams = new HashMap<>();
    private boolean customShop;
    private Map<GameTeam, Location> shopLocations = new HashMap<>();
    private Map<Integer, Location> alchemistsLocations = new HashMap<>();

    private Map<GameTeam, GameQuartz> quartzs = new HashMap<>();
    private HashMap<Material, Set<Location>> regenerativeBlocks = new HashMap<>();

    private Set<Cuboid> cuboids = new HashSet<>();

    private Set<GamePlayer> spectators = new HashSet<>();
    private Set<GamePlayer> gameAllPlayers = new HashSet<>();

    private Map<GamePlayer, Kit> kits = new HashMap<>();

    private GameState state = GameState.LOBBY;

    private GameTimer timer;

    private KillsStats killsStats;
    private GameSidebar sidebar;

    private BCub buildCuboid;

    @SuppressWarnings("unchecked")
    public Game(String id) {
        this.game = this;
        FileConfiguration config = QuartzDefenders.getInstance().getConfigs().getGameInfo();

        if (config.getConfigurationSection("Games." + id) == null) {
            LoggerUtil.logInfo("&cGame with id &a" + id + "&c is not exist");
            return;
        }

        LoggerUtil.logInfo("Loading game with id &a" + id);

        gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        killsStats = new KillsStats(this);

        this.id = id;
        this.gameName = new ColorFormat(config.getString("Games." + this.id + ".game_name")).format();
        this.teckWorldName = config.getString("Games." + this.id + ".tech_map_name");
        this.colorWorldName = new ColorFormat(config.getString("Games." + this.id + ".map_name")).format();
        this.playersInTeam = config.getInt("Games." + this.id + ".players_in_team");
        this.playerRespawnTime = config.getInt("Games." + this.id + ".respawn_time");
        this.quartzHealth = config.getInt("Games." + this.id + ".quartz_health");
        this.teamsCount = config.getInt("Games." + this.id + ".teams_count");
        this.turretLivetime = config.getInt("Games." + this.id + ".turret_livetime");

        this.buildHeight = config.getInt("Games." + this.id + "build.height");
        this.buildRadius = config.getInt("Games." + this.id + "build.radius");

        this.minPlayers = config.getInt("Games." + this.id + ".min_players");

        this.balanceType = BalanceType.valueOf(config.getString("Games." + this.id + ".balance_type"));

        if (QuartzDefenders.getInstance().getGame(gameName) != null) {
            QuartzDefenders.sendInfo(LoggerUtil.gameMessage("Info", "&cGame " + gameName + "&c could not be loaded. This game is already enabled"));
            return;
        }

        LoggerUtil.logInfo("&aGame configuration load success");

        this.mapManager = new MapManager(teckWorldName);
        try {
            mapManager.resetMap();
        } catch (Exception e) {
            LoggerUtil.logError("&cCould not load map in game " + gameName);
            return;
        }
        if (!mapManager.isSuccess()) {
            return;
        }

        LoggerUtil.logInfo("&aMap load success");

        this.map = mapManager.getWorld();

        this.map.setAmbientSpawnLimit(0);
        this.map.setAnimalSpawnLimit(0);
        this.map.setMonsterSpawnLimit(0);

        this.mapSpawn = new Location(Bukkit.getWorld(teckWorldName),
                config.getDouble("Games." + this.id + ".map_spawn.x") + 0.5,
                config.getDouble("Games." + this.id + ".map_spawn.y"),
                config.getDouble("Games." + this.id + ".map_spawn.z") + 0.5);
        this.customShop = config.getBoolean("Games." + this.id + ".custom_shop", false);
        this.buildCuboid = new BCub(mapSpawn.clone().add(buildRadius, 0, buildRadius), mapSpawn.clone().subtract(buildRadius, buildHeight, buildRadius));

        int i = 0;
        for (String team : (List<String>) config.getList("Games." + this.id + ".teams")) {
            if (i != teamsCount) {
                if (isTeamValid(team)) {

                    Location spawn = new Location(Bukkit.getWorld(teckWorldName),
                            config.getDouble("Games." + this.id + ".locations." + team + ".spawn.x") + 0.5,
                            config.getDouble("Games." + this.id + ".locations." + team + ".spawn.y"),
                            config.getDouble("Games." + this.id + ".locations." + team + ".spawn.z") + 0.5,
                            (float) config.getDouble("Games." + this.id + ".locations." + team + ".spawn.yaw"),
                            (float) config.getDouble("Games." + this.id + ".locations." + team + ".spawn.pitch"));

                    Location quartz = new Location(Bukkit.getWorld(teckWorldName),
                            config.getDouble("Games." + this.id + ".locations." + team + ".quartz.x"),
                            config.getDouble("Games." + this.id + ".locations." + team + ".quartz.y"),
                            config.getDouble("Games." + this.id + ".locations." + team + ".quartz.z"));

                    Location shop = new Location(Bukkit.getWorld(teckWorldName),
                            config.getDouble("Games." + this.id + ".locations." + team + ".shop.x") + 0.5,
                            config.getDouble("Games." + this.id + ".locations." + team + ".shop.y"),
                            config.getDouble("Games." + this.id + ".locations." + team + ".shop.z") + 0.5);

                    Cuboid cuboidSpawn = new Cuboid(spawn, spawn);

                    Cuboid cuboidQuartz = new Cuboid(quartz, quartz);

                    cuboids.add(cuboidQuartz);
                    cuboids.add(cuboidSpawn);

                    teams.put(team, new GameTeam(this, team, getChatColor(team), playersInTeam, spawn, gameScoreboard));

                    quartzs.put(getTeam(team), new GameQuartz(this, getTeam(team), quartz, quartzHealth));

                    shopLocations.put(getTeam(team), shop);
                    i++;
                } else {
                    LoggerUtil.logError("&cTeam \"" + team + "\" is not valid!");
                }
            } else {
                break;
            }
        }

        for (String s : config.getConfigurationSection("Games." + this.id + ".regenerative_blocks").getKeys(false)) {
            Material blockMaterial = Material.valueOf(s);
            Set<Location> locs = new HashSet<>();
            for (String loc : config.getConfigurationSection("Games." + this.id + ".regenerative_blocks").getStringList(s + ".list")) {
                int x, y, z;
                String[] array = loc.split(",");
                x = Integer.parseInt(array[0]);
                y = Integer.parseInt(array[1]);
                z = Integer.parseInt(array[2]);
                locs.add(new Location(map, x, y, z));
            }
            regenerativeBlocks.put(blockMaterial, locs);
        }
        
        int alchemistRadius = config.getInt("Games." + this.id + ".alchemists.radius");
        for (String loc : config.getConfigurationSection("Games." + this.id + ".alchemists").getStringList(".list")) {
            String[] array = loc.split(",");
            Location alchemist = new Location(Bukkit.getWorld(teckWorldName),
                    Integer.parseInt(array[0]),
                    Integer.parseInt(array[1]),
                    Integer.parseInt(array[2]));
            alchemistsLocations.put(alchemistRadius, alchemist);
        }

        LoggerUtil.logInfo("&aGame " + gameName + "&a successfully loaded");
        loadSuccess = true;
    }

    /*
	 * JOIN GAME
     */
    public void joinGame(GamePlayer player) {

        if (isGameLocked()) {
            if (!player.getPlayer().hasPermission("QuartzDefenders.admin.gameJoin")) {
                player.sendMessage(LoggerUtil.gameMessage("Game", "&7This game is locked. Only admins can join"));
                return;
            }
        }

        if (gameAllPlayers.contains(player)) {
            return;
        }

        player.getPlayer().setCollidable(false);
        
        // EVENT
        PlayerJoinGameEvent e = new PlayerJoinGameEvent(this, player);        
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return;
        }

        if (isGameState(GameState.LOBBY)) {

            gameAllPlayers.add(player);
            this.broadcastMessage(LoggerUtil.gameMessage("Game", "&7Player &r" + player.getDisplayName() + "&7 joined the game"));
            player.sendMessage(LoggerUtil.gameMessage("Game", "Waiting for more players"));

            if (gameAllPlayers.size() >= minPlayers) {
                setGameState(GameState.WAITING);
                this.sidebar = new GameSidebar(this, gameScoreboard);
                for (GamePlayer p : gameAllPlayers) {
                    p.getPlayer().setGameMode(GameMode.ADVENTURE);
                    p.getPlayer().getInventory().clear();
                    p.getPlayer().teleport(mapSpawn);
                    p.getPlayer().getInventory().setItem(0, QItems.itemTeamChoose(this));
                    p.getPlayer().getInventory().setItem(7, QItems.itemKitsChoose());
                    p.getPlayer().getInventory().setItem(8, QItems.itemQuit());

                    player.setScoreboard(gameScoreboard);

                    p.sendMessage(LoggerUtil.gameMessage("Game", "&7Choose a &ateams&7 and wait a &agame &7start"));
                    sendTabList();
                }
            }
            return;
        }

        if (isGameState(GameState.WAITING) || isGameState(GameState.STARTING)) {

            gameAllPlayers.add(player);
            this.broadcastMessage(LoggerUtil.gameMessage("Game", "&7Player &r" + player.getDisplayName() + "&7 joined the game"));
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
            player.getPlayer().getInventory().clear();
            player.getPlayer().teleport(mapSpawn);
            player.getPlayer().getInventory().setItem(0, QItems.itemTeamChoose(this));
            player.getPlayer().getInventory().setItem(7, QItems.itemKitsChoose());
            player.getPlayer().getInventory().setItem(8, QItems.itemQuit());

            player.setScoreboard(gameScoreboard);

            player.sendMessage(LoggerUtil.gameMessage("Game", "&7Choose a &ateams&7 and wait a &agame &7start"));
            sendTabList();
            return;
        }

        if (isGameState(GameState.ACTIVE)) {
            gameAllPlayers.add(player);
            spectators.add(player);
            this.broadcastMessage(LoggerUtil.gameMessage("Game", "&7Player &r" + player.getDisplayName() + "&7 joined the game"));
            player.getPlayer().getInventory().clear();
            player.getPlayer().teleport(mapSpawn);
            player.getPlayer().setGameMode(GameMode.SPECTATOR);

            player.setScoreboard(gameScoreboard);
            player.sendMessage(LoggerUtil.gameMessage("Game", "Game is running now. You can choose team (/team) and play or stay watching"));
            sendTabList();
            return;
        }

        if (isGameState(GameState.ENDING)) {
            player.sendMessage(LoggerUtil.gameMessage("Game", "&7This game is not available now"));
            return;
        }
    }

    /*
	 * QUIT GAME
     */
    public void quitGame(GamePlayer player) {
        gameAllPlayers.remove(player);

        if (isPlayerInTeam(player)) {
            getTeam(player.getPlayer()).quitTeam(player);
        }

        this.broadcastMessage(LoggerUtil.gameMessage("Game", "&aPlayer &r" + player.getDisplayName() + "&a quit the game"));
        QuartzDefenders.getInstance().getLobby().teleportToSpawn(player.getPlayer(), false);

        Player p = player.getPlayer();
        p.setDisplayName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
        p.setPlayerListName(ChatColor.GRAY + p.getName() + ChatColor.RESET);

        if (p.hasPermission("QuartzDefenders.lobby.colorName")) {
            p.setDisplayName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
            p.setPlayerListName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
        }

        for (String s : QuartzDefenders.getInstance().getDevs()) {
            if (p.getName().equals(s)) {
                p.setDisplayName(ChatColor.DARK_RED + p.getName() + ChatColor.RESET);
                p.setPlayerListName(ChatColor.DARK_RED + p.getName() + ChatColor.RESET);
            }
        }

        Iterator<PotionEffect> i = p.getPlayer().getActivePotionEffects().iterator();
        while (i.hasNext()) {
            p.getPlayer().addPotionEffect(new PotionEffect(i.next().getType(), 2, 0), true);
        }

        p.setGameMode(GameMode.ADVENTURE);

        p.getInventory().clear();

        p.getInventory().setItem(0, QItems.itemGamesChoose());
        p.getInventory().setItem(4, QItems.itemStats());
        p.getInventory().setItem(7, QItems.itemHidePlayers(QuartzDefenders.getInstance().getLobby().getHides().contains(p)));
        p.getInventory().setItem(8, QItems.itemLobbyShop());

        ScoreboardLobby s = new ScoreboardLobby(QuartzDefenders.getInstance(), p);
        s.setScoreboard();
        QuartzDefenders.getInstance().getLobby().sendTabList(p.getPlayer());

        if (gameAllPlayers.isEmpty()) {
            QuartzDefenders.getInstance().deleteGame(this);
            QuartzDefenders.getInstance().addGame(id);
        }
    }

    private void quitGame1(GamePlayer player) {
    	 if (isPlayerInTeam(player)) {
             getTeam(player.getPlayer()).quitTeam(player);
         }

         QuartzDefenders.getInstance().getLobby().teleportToSpawn(player.getPlayer(), false);

         Player p = player.getPlayer();
         p.setDisplayName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
         p.setPlayerListName(ChatColor.GRAY + p.getName() + ChatColor.RESET);

         if (p.hasPermission("QuartzDefenders.lobby.colorName")) {
             p.setDisplayName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
             p.setPlayerListName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
         }

         for (String s : QuartzDefenders.getInstance().getDevs()) {
             if (p.getName().equals(s)) {
                 p.setDisplayName(ChatColor.DARK_RED + p.getName() + ChatColor.RESET);
                 p.setPlayerListName(ChatColor.DARK_RED + p.getName() + ChatColor.RESET);
             }
         }

         Iterator<PotionEffect> i = p.getPlayer().getActivePotionEffects().iterator();
         while (i.hasNext()) {
             p.getPlayer().addPotionEffect(new PotionEffect(i.next().getType(), 2, 0), true);
         }

         p.setGameMode(GameMode.ADVENTURE);

         p.getInventory().clear();

         p.getInventory().setItem(0, QItems.itemGamesChoose());
         p.getInventory().setItem(4, QItems.itemStats());
         p.getInventory().setItem(7, QItems.itemHidePlayers(QuartzDefenders.getInstance().getLobby().getHides().contains(p)));
         p.getInventory().setItem(8, QItems.itemLobbyShop());

         ScoreboardLobby s = new ScoreboardLobby(QuartzDefenders.getInstance(), p);
         s.setScoreboard();
         QuartzDefenders.getInstance().getLobby().sendTabList(p.getPlayer());

         if (gameAllPlayers.isEmpty()) {
             QuartzDefenders.getInstance().deleteGame(this);
             QuartzDefenders.getInstance().addGame(id);
         }
    }
    
    
    /*
	 * Start game
     */
    public void startGame() {
        if (isGameState(GameState.ACTIVE) || isGameState(GameState.ENDING)) {
            return;
        }
        setGameState(GameState.ACTIVE);
        getSidebar().refresh();
        this.timer = new GameTimer(this);
        timer.runTaskTimer(QuartzDefenders.getInstance(), 0, 20);
        ShopEntity.loadShops(game);
        Bukkit.getPluginManager().callEvent(new GameStartEvent(game));
        for (GamePlayer p : gameAllPlayers) {
            spectators.add(p);
            p.getPlayer().setGameMode(GameMode.SPECTATOR);
            p.getPlayer().getInventory().clear();
        }
        for (GameTeam team : teams.values()) {
            for (GamePlayer p : team.getPlayers()) {
                spectators.remove(p);
                p.getPlayer().setGameMode(GameMode.SURVIVAL);
                p.getPlayer().setVelocity(new Vector(0, 0, 0));
                p.getPlayer().teleport(team.getSpawnLocation());
                p.getPlayer().setHealth(20);
                p.getPlayer().setFoodLevel(20);
                p.getPlayer().setExp(0);
                p.getPlayer().setLevel(0);
                p.getPlayer().setTotalExperience(0);
                Iterator<PotionEffect> i = p.getPlayer().getActivePotionEffects().iterator();
                while (i.hasNext()) {
                    p.getPlayer().addPotionEffect(new PotionEffect(i.next().getType(), 2, 0), true);
                }
                StatsPlayer sp = new StatsPlayer(p.getPlayer());
                sp.addPlayedGame();
            }
        }
        for (GamePlayer p : gameAllPlayers) {
            if (spectators.contains(p)) {
                continue;
            }
            if (kits.containsKey(p)) {
                kits.get(p).apply(p);
            }
        }
        for (GameQuartz quartz : quartzs.values()) {
            quartz.replace();
        }
    }

    public boolean startCountdown() {
        if (!isGameState(GameState.WAITING)) {
            return false;
        }
               
        setGameState(GameState.STARTING);
        getSidebar().refresh();
        new Countdown(this).runTaskTimer(QuartzDefenders.getInstance(), 0, 20);
        return true;
    }

    public void checkGameEnd() {
        if (!isGameState(GameState.ACTIVE)) {
            return;
        }
        GameTeam winner = null;
        int i = 0;
        
        for (GameTeam team : teams.values()) {
            if (!team.isEmpty()) {
                winner = team;
                i++;
            }
            if (team.isEmpty()) {
                getQuartz(team).destroyQuartz();
            }
            
        }
        if (i == 1) {
            setGameState(GameState.ENDING);
            getSidebar().refresh();
            getGameTimer().stop();
            for (GamePlayer p : gameAllPlayers) {
                TitleUtil.sendTitle(p.getPlayer(), winner.getName(), "&fteam win the game", 10);
                FireworkUtil.spawn(p.getPlayer().getLocation(), 20L);
            }
            for (GamePlayer p : winner.getPlayers()) {
                StatsPlayer sp = new StatsPlayer(p.getPlayer());
                sp.addWin();
            }

            getKillsStats().sendKillsStats();
            
            Bukkit.broadcastMessage(LoggerUtil.gameMessage(gameName, winner.getName() + "&7 team win the game"));

            BukkitRunnable runnable = new BukkitRunnable() {

                @Override
                public void run() {
                    endGame();
                    QuartzDefenders.sendInfo(LoggerUtil.gameMessage(gameName, "The game is now restarting"));
                }
            };
            runnable.runTaskLater(QuartzDefenders.getInstance(), 15 * 20);
        }
        if(i == 0) {
        	setGameState(GameState.ENDING);
            getSidebar().refresh();
            getGameTimer().stop();
            getKillsStats().sendKillsStats();
            
            BukkitRunnable runnable = new BukkitRunnable() {

                @Override
                public void run() {
                    endGame();
                    QuartzDefenders.sendInfo(LoggerUtil.gameMessage(gameName, "The game is now restarting"));
                }
            };
            runnable.runTaskLater(QuartzDefenders.getInstance(), 15 * 20);
        }
    }

    public void endGame() {    	
        setGameState(GameState.ENDING);
        getSidebar().refresh();
        try {
            getGameTimer().stop();
        } catch (Exception e) {

        }
        disableGame();
    }

    public void broadcastMessage(String message) {
        for (GamePlayer p : gameAllPlayers) {
            p.sendMessage(message);
        }
    }

    public void sendTabList() {
        String header = "";
        String footer = "";
        String n = "\n";

        String map = getColorWorldName();
        String time = "00:00:00";

        if (getGameTimer() != null) {
            time = getGameTimer().getStringTime();
        }

        header = " " + n
                + "&3\u00AB &b&lPlayCraft.COM.UA &3\u00BB" + n
                + " " + n
                + "&3Time&f: " + time + n
                + " ";
        footer = " " + n
                + map + n
                + " ";
        for (GamePlayer p : gameAllPlayers) {
            if (!p.getPlayer().isOnline()) {
                continue;
            }
            TitleUtil.sendTabTitle(p.getPlayer(), header, footer);
        }
    }

    public void disableGame() {    	
        for (GamePlayer p : gameAllPlayers) {
            p.getPlayer().teleport(QuartzDefenders.getInstance().getLobby().getLocation());
            p.getPlayer().setGameMode(GameMode.ADVENTURE);
            
            quitGame1(p);                      

            Iterator<PotionEffect> i = p.getPlayer().getActivePotionEffects().iterator();
            while (i.hasNext()) {
                p.getPlayer().addPotionEffect(new PotionEffect(i.next().getType(), 2, 0), true);
            }

        }
        
        gameAllPlayers.clear();
          
        mapManager.deleteMap();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "game remove " + id);
        LoggerUtil.logInfo("&aGame " + gameName + "&a with id " + id + "&a successfully disabled");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "game add " + id);

        this.game = null;
    }

    /*
	 * ENUMS
     */
    public enum GameState {
        LOBBY, WAITING, STARTING, ACTIVE, ENDING
    }

    public enum BalanceType {
        NO_BALANCE, DEFAULT_BALANCE, TEAM_KD_BALANCE
    }

    /*
	 * IS
     */
    public boolean isLoadSuccess() {
        return loadSuccess;
    }

    public boolean isGameState(GameState state) {
        return getGameState() == state;
    }

    public boolean containsPlayer(GamePlayer player) {
        return gameAllPlayers.contains(player);
    }

    private boolean isTeamValid(String s) {
        for (String ss : validTeams) {
            if (ss.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameReady() {
        int i = 0;
        for (GameTeam team : teams.values()) {
            i += team.getPlayersSize();
        }

        return isGameState(GameState.WAITING) && i >= minPlayers;
    }

    public boolean isPlayerInTeam(GamePlayer player) {
        for (GameTeam team : teams.values()) {
            if (team.contains(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameLocked() {
        return lockGame;
    }

    /*
	 * GETTERS
     */
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
        for (GameTeam team : teams.values()) {
            if (team.contains(QuartzDefenders.getInstance().getGamePlayer(p))) {
                return team;
            }
        }
        return null;
    }

    public World getGameWorld() {
        return map;
    }
    
    public boolean getCustomShop() {
        return customShop;
    }

    public Collection<Location> getShopLocations() {
        return shopLocations.values();
    }

    public Map<Integer, Location> getAlchemicsLocations() {
        return alchemistsLocations;
    }

    public HashMap<Material, Set<Location>> getRegenerativeBlocks() {
        return regenerativeBlocks;
    }

    public int getTurretLivetime() {
        return turretLivetime;
    }

    public String getTechWorldName() {
        return teckWorldName;
    }

    public String getColorWorldName() {
        return colorWorldName;
    }

    public int getPlayersInTeam() {
        return playersInTeam;
    }

    public int getTeamsCount() {
        return teamsCount;
    }

    public int getQuartzHealth() {
        return quartzHealth;
    }

    public BalanceType getBalanceType() {
        return balanceType;
    }

    public int getPlayersRespawnTime() {
        return playerRespawnTime;
    }

    public GameTimer getGameTimer() {
        return timer;
    }

    public GameState getGameState() {
        return state;
    }

    private ChatColor getChatColor(String name) {
        switch (name) {
            case "RED":
                return ChatColor.RED;
            case "BLUE":
                return ChatColor.BLUE;
            case "GREEN":
                return ChatColor.GREEN;
            case "YELLOW":
                return ChatColor.YELLOW;
            case "MAGENTA":
                return ChatColor.DARK_PURPLE;
            case "AQUA":
                return ChatColor.AQUA;
            case "GRAY":
                return ChatColor.DARK_GRAY;
            case "WHITE":
                return ChatColor.WHITE;
        }

        return ChatColor.GRAY;
    }

    public Set<GamePlayer> getPlayers() {
        return gameAllPlayers;
    }

    public Set<GamePlayer> getSpectators() {
        return spectators;
    }

    public GameQuartz getQuartz(GameTeam team) {
        return quartzs.get(team);
    }

    public GameQuartz getQuartz(Location loc) {
        for (GameQuartz quartz : quartzs.values()) {
            Location loc1 = quartz.getLocation();
            if (loc1.getWorld() == loc.getWorld()
                    && loc1.getBlockX() == loc.getBlockX()
                    && loc1.getBlockY() == loc.getBlockY()
                    && loc1.getBlockZ() == loc.getBlockZ()) {
                return quartz;
            }
        }
        return null;
    }

    public Location getMapCenter() {
        return mapSpawn;
    }

    public KillsStats getKillsStats() {
        return killsStats;
    }

    public Map<GameTeam, GameQuartz> getQuartzsLocations() {
        return quartzs;
    }

    public GameSidebar getSidebar() {
        return sidebar;
    }

    public Set<Cuboid> getCuboids() {
        return cuboids;
    }

    public int getBuildHeight() {
        return buildHeight;
    }

    public int getBuildRadius() {
        return buildRadius;
    }

    public BCub getBuildCuboid() {
        return buildCuboid;
    }

    /*
	 * SETTERS
     */
    public void setKit(GamePlayer p, Kit kit) {
        kits.put(p, kit);
    }

    public void setGameState(GameState state) {
        // Event
        GameStateChangeEvent e = new GameStateChangeEvent(this, this.state, state);
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return;
        }

        this.state = state;
    }

    public void setLockGame(boolean arg) {
        this.lockGame = arg;
    }

    /*
     * SETUP GAME
     */
    public void addRegenerativeBlock(Block b, Player p) {
    	FileConfiguration cfg = QuartzDefenders.getInstance().getConfigs().getGameInfo();
    	
    	if(!cfg.isConfigurationSection("Games." + id + ".regenerative_blocks." + b.getType().toString())) {
    		cfg.set("Games." + id + ".regenerative_blocks." + b.getType().toString() + ".regenerate_time", 200);
    		ArrayList<String> l = new ArrayList<>();
    		String s = b.getX() + "," + b.getY() + "," + b.getZ();
    		l.add(s);
    		cfg.set("Games." + id + ".regenerative_blocks." + b.getType().toString() + ".list", l);
    	} else {
    		ArrayList<String> l = (ArrayList<String>) cfg.getStringList("Games." + id + ".regenerative_blocks." + b.getType().toString() + ".list");
    		String s = b.getX() + "," + b.getY() + "," + b.getZ();
    		if(l.contains(s)) {
    			l.remove(s);
    			p.sendMessage(LoggerUtil.gameMessage("Setup", "&aBlock removed"));
    			cfg.set("Games." + id + ".regenerative_blocks." + b.getType().toString() + ".list", l);
    			QuartzDefenders.getInstance().getConfigs().saveGameInfo();
    			return;
    		}
    		l.add(s);
    		cfg.set("Games." + id + ".regenerative_blocks." + b.getType().toString() + ".list", l);
    	}
    	p.sendMessage(LoggerUtil.gameMessage("Setup", "&aAdded new block&f: Material &a" + b.getType().toString() 
    			+ "&f, X:&a" + b.getX() + "&f,Y:&a" + b.getY() + "&f,Z:&a" + b.getZ()));
    	QuartzDefenders.getInstance().getConfigs().saveGameInfo();
    }
    public void setQuartz(Block b, String team, Player p) {
    	if(!isTeamValid(team)) {
    		p.sendMessage(LoggerUtil.gameMessage("Setup", "&cTeam " + team + "&c is not valid"));
    		return;
    	}
    	
    	FileConfiguration cfg = QuartzDefenders.getInstance().getConfigs().getGameInfo();
    	
    	cfg.set("Games." + id + ".locations." + team + ".quartz.x", b.getX());
    	cfg.set("Games." + id + ".locations." + team + ".quartz.y", b.getY());
    	cfg.set("Games." + id + ".locations." + team + ".quartz.z", b.getZ());
    	
    	p.sendMessage(LoggerUtil.gameMessage("Setup", "&aQuartz setup success. Team&f: " + team 
    			+ "&f, X:&a" + b.getX() + "&f,Y:&a" + b.getY() + "&f,Z:&a" + b.getZ())); 	
    	QuartzDefenders.getInstance().getConfigs().saveGameInfo();
    }
    
    public void setSpawn(Location loc, String team, Player p) {
    	if(!isTeamValid(team)) {
    		p.sendMessage(LoggerUtil.gameMessage("Setup", "&cTeam " + team + "&c is not valid"));
    		return;
    	}
    	
    	FileConfiguration cfg = QuartzDefenders.getInstance().getConfigs().getGameInfo();
    	
    	cfg.set("Games." + id + ".locations." + team + ".spawn.x", loc.getBlockX());
    	cfg.set("Games." + id + ".locations." + team + ".spawn.y", loc.getBlockY());
    	cfg.set("Games." + id + ".locations." + team + ".spawn.z", loc.getBlockZ());
    	cfg.set("Games." + id + ".locations." + team + ".spawn.yaw", loc.getYaw());
    	cfg.set("Games." + id + ".locations." + team + ".spawn.pitch", loc.getPitch());
    	
    	p.sendMessage(LoggerUtil.gameMessage("Setup", "&aQuartz setup success. Team&f: " + team 
    			+ "&f, X:&a" + loc.getBlockX() + "&f,Y:&a" + loc.getBlockY() + "&f,Z:&a" + loc.getBlockZ() + "&f,Yaw:&a" + loc.getYaw() + "&f,Pitch:&a" + loc.getPitch())); 	
    	QuartzDefenders.getInstance().getConfigs().saveGameInfo();
    }
}
