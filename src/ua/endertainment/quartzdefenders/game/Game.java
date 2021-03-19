package ua.endertainment.quartzdefenders.game;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import ua.coolboy.quartzdefenders.shop.ShopEntity;
import ua.coolboy.quartzdefenders.voting.Vote;
import ua.coolboy.quartzdefenders.voting.VoteManager;
import ua.coolboy.quartzdefenders.voting.VoteObject;
import ua.endertainment.quartzdefenders.events.game.GameStartEvent;
import ua.endertainment.quartzdefenders.events.game.GameStateChangeEvent;
import ua.endertainment.quartzdefenders.events.game.PlayerJoinGameEvent;
import ua.endertainment.quartzdefenders.items.QItems;
import ua.endertainment.quartzdefenders.stats.KillsStats;
import ua.endertainment.quartzdefenders.kits.Kit;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.combo.ComboManager;
import ua.endertainment.quartzdefenders.events.game.GameEndEvent;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.BCub;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.Cuboid;
import ua.endertainment.quartzdefenders.utils.Cuboid.CubType;
import ua.endertainment.quartzdefenders.utils.FireworkUtil;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.MapManager;
import ua.endertainment.quartzdefenders.utils.Replacer;
import ua.endertainment.quartzdefenders.utils.LobbySidebar;

public class Game {

    private String[] validTeams = {"RED", "BLUE", "GREEN", "YELLOW", "MAGENTA", "AQUA", "GRAY", "PINK", "WHITE", "BLACK"};

    private Game game;

    private boolean loadSuccess = false;

    private boolean lockGame = false;
    
    private VoteManager voteManager;

    private String id;
    private String gameName;
    private String teckWorldName;
    private String colorWorldName;
    private int playersInTeam;
    private int minPlayers;
    private boolean autostart;
    
    private ComboManager comboManager;
    private MapManager mapManager;
    private World map;

    private Location mapSpawn;
    private int buildHeight, buildRadius;
    private boolean generateSpectator;

    private int playerRespawnTime;
    private int quartzHealth;
    private int teamsCount;
    private BalanceType balanceType = BalanceType.DEFAULT_BALANCE;

    private Scoreboard gameScoreboard;

    private Map<String, GameTeam> teams = new HashMap<>();

    private boolean customShop;
    private boolean diamondDef;
    private Map<GameTeam, Location> shopLocations = new HashMap<>();
    private Map<Location, Integer> alchemistsLocations = new HashMap<>();
    private Map<Location, Integer> defendersLocations = new HashMap<>();
    private int alchemistDelay;
    private int defenderDelay;

    private Map<GameTeam, GameQuartz> quartzs = new HashMap<>();
    private Ores ores;

    private boolean blockRangedCraft;
    private boolean autosmelt;

    private Set<Cuboid> cuboids = new HashSet<>();

    private Set<GamePlayer> spectators = new HashSet<>();
    private Set<GamePlayer> gameAllPlayers = new HashSet<>();

    private Map<GamePlayer, Kit> kits = new HashMap<>();

    private Set<GamePlayer> disableJoinGame = new HashSet<>();
    private Set<GamePlayer> disableJoinTeam = new HashSet<>();

    private GameState state = GameState.LOBBY;

    private GameTimer timer;
    private Calendar started;

    private KillsStats killsStats;
    private GameSidebar sidebar;

    private BCub buildCuboid;

    @SuppressWarnings("unchecked")
    public Game(String id) {
        this.game = this;
        FileConfiguration config = QuartzDefenders.getInstance().getConfigs().getGameInfo();

        if (config.getConfigurationSection("Games." + id) == null) {
            LoggerUtil.info(Language.getString("logger.game_not_exist", new Replacer("{0}", id)));

            return;
        }

        if (config.getBoolean("Games." + id + ".skip")) {
            return;
        }

        LoggerUtil.info(Language.getString("logger.loading_game", new Replacer("{0}", id)));

        gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sidebar = new GameSidebar(this, gameScoreboard);
        killsStats = new KillsStats(this);

        this.id = id;
        this.gameName = new ColorFormat(config.getString("Games." + this.id + ".game_name")).format();
        this.teckWorldName = config.getString("Games." + this.id + ".world_name");
        this.colorWorldName = new ColorFormat(config.getString("Games." + this.id + ".map_name")).format();
        this.playersInTeam = config.getInt("Games." + this.id + ".players_in_team");
        this.playerRespawnTime = config.getInt("Games." + this.id + ".respawn_time");
        this.quartzHealth = config.getInt("Games." + this.id + ".quartz_health");
        this.teamsCount = config.getStringList("Games." + this.id + ".teams").size();
        this.autostart = config.getBoolean("Games." + this.id + ".autostart", true);

        this.buildHeight = config.getInt("Games." + this.id + ".build.height");
        this.buildRadius = config.getInt("Games." + this.id + ".build.radius");

        this.generateSpectator = config.getBoolean("Games." + this.id + ".spectator_room", false);

        this.minPlayers = config.getInt("Games." + this.id + ".min_players");

        this.balanceType = BalanceType.valueOf(config.getString("Games." + this.id + ".balance_type"));

        if (QuartzDefenders.getInstance().getGame(gameName) != null) {
            LoggerUtil.info(LoggerUtil.gameMessage(Language.getString("logger.info"), Language.getString("game.game_already_exist", new Replacer("{0}", gameName))));
            return;
        }

        LoggerUtil.info(Language.getString("logger.cfg_load_success"));

        this.mapManager = new MapManager(teckWorldName);
        try {
            mapManager.resetMap();
        } catch (Exception e) {
            LoggerUtil.error(Language.getString("logger.load_map_failed", new Replacer("{0}", gameName)));
            return;
        }
        if (!mapManager.isSuccess()) {
            return;
        }

        LoggerUtil.info(Language.getString("logger.load_map_success"));

        this.map = mapManager.getWorld();

        this.map.setAmbientSpawnLimit(0);
        this.map.setAnimalSpawnLimit(0);
        this.map.setMonsterSpawnLimit(0);
        this.map.setGameRule(GameRule.KEEP_INVENTORY, false);
        this.map.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        this.map.setDifficulty(Difficulty.valueOf(config.getString("Games." + this.id + ".difficulty", "NORMAL")));

        this.mapSpawn = new Location(Bukkit.getWorld(teckWorldName),
                config.getDouble("Games." + this.id + ".map_spawn.x") + 0.5,
                config.getDouble("Games." + this.id + ".map_spawn.y"),
                config.getDouble("Games." + this.id + ".map_spawn.z") + 0.5);
        this.customShop = config.getBoolean("Games." + this.id + ".custom_shop", false);
        this.buildCuboid = new BCub(mapSpawn, this);
        
        map.getWorldBorder().setCenter(mapSpawn);
        map.getWorldBorder().setDamageBuffer(1);
        map.getWorldBorder().setSize(buildRadius*2);
        map.getWorldBorder().setDamageAmount(1);
        
        if (this.generateSpectator) {
            generateSpectatorRoom(Material.GLASS);
        }

        this.blockRangedCraft = config.getBoolean("Games." + this.id + ".block_bow", false);

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

                    teams.put(team, new GameTeam(this, team, getChatColor(team), playersInTeam, spawn, gameScoreboard));

                    quartzs.put(getTeam(team), new GameQuartz(this, getTeam(team), quartz, quartzHealth));

                    shopLocations.put(getTeam(team), shop);

                    Cuboid cuboidSpawn = new Cuboid(spawn, 1, getTeam(team), CubType.SPAWN);

                    Cuboid cuboidQuartz = new Cuboid(quartz, 2, getTeam(team), CubType.QUARTZ);

                    cuboids.add(cuboidQuartz);
                    cuboids.add(cuboidSpawn);

                    i++;
                } else {
                    LoggerUtil.error(Language.getString("logger.invalid_team", new Replacer("{0}", team)));
                }
            } else {
                break;
            }
        }

        this.ores = new Ores(this, config.getConfigurationSection("Games." + this.id + ".regenerative_blocks"));

        int alchemistRadius = config.getInt("Games." + this.id + ".alchemists.radius",5);
        alchemistDelay = config.getInt("Games." + this.id + ".alchemists.delay",10);
        for (String loc : config.getConfigurationSection("Games." + this.id + ".alchemists").getStringList(".list")) {
            String[] array = loc.split(",");
            Location alchemist = new Location(Bukkit.getWorld(teckWorldName),
                    Integer.parseInt(array[0]),
                    Integer.parseInt(array[1]),
                    Integer.parseInt(array[2]));
            alchemistsLocations.put(alchemist, alchemistRadius);
        }
        
        int defenderRadius = config.getInt("Games." + this.id + ".defender.radius", 5);
        defenderDelay = config.getInt("Games." + this.id + ".defender.delay", 10);
        for (String loc : config.getConfigurationSection("Games." + this.id + ".defender").getStringList(".list")) {
            String[] array = loc.split(",");
            Location brute = new Location(Bukkit.getWorld(teckWorldName),
                    Integer.parseInt(array[0]),
                    Integer.parseInt(array[1]),
                    Integer.parseInt(array[2]));
            defendersLocations.put(brute, defenderRadius);
        }
        
        this.diamondDef = config.getBoolean("Games." + this.id + ".diamond_defenders", false);
        
        LoggerUtil.info(Language.getString("logger.game_load_success", new Replacer("{0}", gameName)));
        loadSuccess = true;
        
        this.autosmelt = config.getBoolean("Games." + this.id + ".auto_smelt", false);
        
        voteManager = new VoteManager(config,this);
        comboManager = new ComboManager();
    }

    /*
	 * JOIN GAME
     */
    public void joinGame(GamePlayer player) {

        if (isGameLocked()) {
            if (!player.getPlayer().hasPermission("QuartzDefenders.admin.gameJoin")) {
                player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.game_locked")));
                return;
            }
        }

        if (isJoinGameDisabled(player)) {
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.join_disabled")));
            return;
        }

        if (gameAllPlayers.contains(player)) {
            return;
        }

        // EVENT
        PlayerJoinGameEvent e = new PlayerJoinGameEvent(this, player);
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return;
        }
        
        player.resetVote();
        player.setScoreboard(gameScoreboard);

        if (isGameState(GameState.LOBBY)) {

            gameAllPlayers.add(player);
            this.broadcastMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.join_game", new Replacer("{0}", player.getDisplayName()))));
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.waiting_players")));

            if (gameAllPlayers.size() >= minPlayers) {
                setGameState(GameState.WAITING);
                refreshScoreboard();
                for (GamePlayer p : gameAllPlayers) {
                    p.getPlayer().setGameMode(GameMode.ADVENTURE);
                    p.getPlayer().getInventory().clear();
                    p.getPlayer().teleport(mapSpawn);
                    p.getPlayer().getInventory().setItem(0, QItems.itemTeamChoose());
                    if(isVoting()) {
                    p.getPlayer().getInventory().setItem(4, QItems.itemVote());
                    }
                    //p.getPlayer().getInventory().setItem(7, QItems.itemKitsChoose());
                    p.getPlayer().getInventory().setItem(8, QItems.itemQuit());

                    p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.choose_team")));
                    sendTabList();
                }
            }
            return;
        }

        if (isGameState(GameState.WAITING) || isGameState(GameState.STARTING)) {

            gameAllPlayers.add(player);
            this.broadcastMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.join_game", new Replacer("{0}", player.getDisplayName()))));
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
            player.getPlayer().getInventory().clear();
            player.getPlayer().teleport(mapSpawn);
            player.getPlayer().getInventory().setItem(0, QItems.itemTeamChoose());
            if(isVoting()) player.getPlayer().getInventory().setItem(4, QItems.itemVote());
            //player.getPlayer().getInventory().setItem(7, QItems.itemKitsChoose());
            player.getPlayer().getInventory().setItem(8, QItems.itemQuit());

            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.choose_team")));
            sendTabList();
            return;
        }

        if (isGameState(GameState.ACTIVE)) {
            gameAllPlayers.add(player);
            spectators.add(player);
            this.broadcastMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.join_game", new Replacer("{0}", player.getDisplayName()))));
            player.getPlayer().getInventory().clear();
            player.getPlayer().teleport(mapSpawn);
            player.getPlayer().setGameMode(GameMode.SPECTATOR);
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.game_running")));
            player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.BLUE + "/team" + ChatColor.GRAY + " to choose team");
            sendTabList();
            return;
        }

        if (isGameState(GameState.ENDING)) {
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.game_unavailable")));
            return;
        }
    }

    public void refreshScoreboard() {
        sidebar.refresh();
    }

    /*
	 * QUIT GAME
     */
    public void quitGame(GamePlayer player) {
        gameAllPlayers.remove(player);

        if (isInTeam(player)) {
            getTeam(player.getPlayer()).quitTeam(player);
        }

        this.broadcastMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.quit_game", new Replacer("{0}", player.getDisplayName()))));
        QuartzDefenders.getInstance().getLobby().teleportToSpawn(player.getPlayer(), false);
        for(Player pl : player.getPlayer().getWorld().getPlayers()) {
            player.getPlayer().showPlayer(QuartzDefenders.getInstance(), pl);
            pl.showPlayer(QuartzDefenders.getInstance(), player.getPlayer());
        }

        Player p = player.getPlayer();
        p.setDisplayName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
        p.setPlayerListName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
        
        p.setExp(0);
        p.setLevel(0);

        p.setDisplayName(QuartzDefenders.getInstance().getGamePlayer(p).getDefaultDisplayName());
        p.setPlayerListName(QuartzDefenders.getInstance().getGamePlayer(p).getDefaultDisplayName());
        

        for(PotionEffect e : p.getActivePotionEffects()) {
            p.removePotionEffect(e.getType());
        }

        p.setGameMode(GameMode.ADVENTURE);
        p.setBedSpawnLocation(QuartzDefenders.getInstance().getLobby().getLocation());
        QuartzDefenders.getInstance().getLobby().setLobbyTools(p);

        LobbySidebar s = new LobbySidebar(QuartzDefenders.getInstance(), p);
        s.setScoreboard();
        QuartzDefenders.getInstance().getLobby().sendTabList(p);
        
        checkGameEnd();

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
        started = Calendar.getInstance();
        getSidebar().refresh();
        this.timer = new GameTimer(this);
        this.timer.runTaskTimer(QuartzDefenders.getInstance(), 0, 20);
        if (generateSpectator) {
            generateSpectatorRoom(Material.AIR);
        }
        Bukkit.broadcastMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.game_started", new Replacer("{0}", gameName))));
        map.setTime(6000);
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
                p.setRespawn(team.getSpawnLocation());
                p.getPlayer().teleport(team.getSpawnLocation());
                p.getPlayer().setHealth(20);
                p.getPlayer().setFoodLevel(20);
                p.getPlayer().setExp(0);
                p.getPlayer().setLevel(0);
                p.getPlayer().setTotalExperience(0);
                disableJoinTeam(p);
                for(PotionEffect e : p.getPlayer().getActivePotionEffects()) {
                    p.getPlayer().removePotionEffect(e.getType());
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
    
    public void endVoting() {
        Map<VoteObject, Vote.VoteResult> results = voteManager.getResults();
        for(Map.Entry<VoteObject, Vote.VoteResult> entry : results.entrySet()) {
            Boolean bool = entry.getValue().asBoolean();
            if(bool==null) bool = entry.getKey().getDefault().asBoolean();
            switch(entry.getKey().getType()) {
                case AUTOSMELT:
                    autosmelt = bool;
                    break;
                case BLOCK_RANGED:
                    blockRangedCraft = bool;
                    break;
                case DIAMOND_DEFENDERS:
                    diamondDef = bool;
                    break;
                default:
                	break;
            }
        }
        
        for(GamePlayer player : getPlayers()) {
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("vote.chat_prefix"), Language.getString("vote.ended")));
            for(Map.Entry<VoteObject, Vote.VoteResult> entry : results.entrySet()) {
                String name = entry.getKey().getType().getItem().getItemMeta().getDisplayName();
                String status="";
                
                String enabled = Language.getString("generic.enabled");
                String disabled = Language.getString("generic.disabled");
                
                switch(entry.getKey().getType()) {
                    case AUTOSMELT:
                        status = autosmelt ? enabled : disabled;
                        break;
                    case BLOCK_RANGED:
                        status = blockRangedCraft ? enabled : disabled;
                        break;
                    case DIAMOND_DEFENDERS:
                        status = diamondDef ? enabled : disabled;
                        break;
                    default:
                    	break;
                }
                if(!status.equals("")) {
                    player.sendMessage(Language.getString("vote.result", name, status));
                }
            }
        }
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
                p.getPlayer().sendTitle(Language.getString("game.title.win.top", new Replacer("{0}", winner.getName())), Language.getString("game.title.win.bot"), 10, 10*20, 20);
                FireworkUtil.spawn(p.getPlayer().getLocation(), 20L);
            }
            for (GamePlayer p : winner.getPlayers()) {
                StatsPlayer sp = new StatsPlayer(p.getPlayer());
                sp.addWin();
            }

            getKillsStats().sendKillsStats();

            Bukkit.broadcastMessage(LoggerUtil.gameMessage(gameName, Language.getString("team.win", new Replacer("{0}", winner.getName()))));
            Bukkit.broadcastMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.game_ended", new Replacer("{0}", gameName))));

            BukkitRunnable runnable = new BukkitRunnable() {

                @Override
                public void run() {
                    endGame();
                    QuartzDefenders.sendInfo(LoggerUtil.gameMessage(gameName, Language.getString("logger.game_restarting")));
                }
            };
            runnable.runTaskLater(QuartzDefenders.getInstance(), 15 * 20);
        }
        if (i == 0) {
            setGameState(GameState.ENDING);
            getSidebar().refresh();
            getGameTimer().stop();
            getKillsStats().sendKillsStats();

            BukkitRunnable runnable = new BukkitRunnable() {

                @Override
                public void run() {
                    endGame();
                    QuartzDefenders.sendInfo(LoggerUtil.gameMessage(gameName, Language.getString("logger.game_restarting")));
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
        Bukkit.getPluginManager().callEvent(new GameEndEvent(game));
        disableGame();
    }

    public void broadcastMessage(String message) {
        for (GamePlayer p : gameAllPlayers) {
            p.sendMessage(message);
        }
    }

    public void sendTabList() {
        String map = getColorWorldName();
        String time = "00:00:00";

        if (getGameTimer() != null) {
            time = getGameTimer().getStringTime();
        }

        String header = Language.getString("tablist.game.header", new Replacer("{0}", time + ""));
        String footer = Language.getString("tablist.game.footer", new Replacer("{0}", map));

        for (GamePlayer p : gameAllPlayers) {
            if (!p.getPlayer().isOnline()) {
                continue;
            }
            p.getPlayer().setPlayerListHeaderFooter(header, footer);
        }
    }

    public void disableGame() {
        for (GamePlayer p : gameAllPlayers) {

            if (isInTeam(p)) {
                getTeam(p.getPlayer()).quitTeam(p);
            }

            p.getPlayer().teleport(QuartzDefenders.getInstance().getLobby().getLocation());
            p.setRespawn(QuartzDefenders.getInstance().getLobby().getLocation());
            p.getPlayer().setHealth(20);
            p.getPlayer().setFoodLevel(20);
            p.getPlayer().setGameMode(GameMode.ADVENTURE);

            QuartzDefenders.getInstance().getLobby().setLobbyTools(p.getPlayer());
            
            p.resetDisplayName();
            
            comboManager.reset();
            QuartzDefenders.getInstance().getTopManager().refresh();
            QuartzDefenders.getInstance().getTopManager().setupSigns();

            //quitGame1(p);                      
            for(PotionEffect e : p.getPlayer().getActivePotionEffects()) {
                p.getPlayer().removePotionEffect(e.getType());
            }

        }

        gameAllPlayers.clear();

        mapManager.deleteMap();
        LoggerUtil.info(Language.getString("logger.game_disable", new Replacer("{0}", gameName), new Replacer("{1}", id)));
        QuartzDefenders.getInstance().restartGame(this);

        this.game = null;
    }

    public void showBossBar(BossBar bar) {
        for (GamePlayer p : gameAllPlayers) {
            bar.addPlayer(p.getPlayer());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                bar.removeAll();
            }
        }.runTaskLater(QuartzDefenders.getInstance(), 100);
    }

    public void reconnect(GamePlayer p) {
        GameTeam team = getTeam(p.getPlayer());
        if (team == null) {
            return;
        }
        Location loc = team.getSpawnLocation();
        p.getPlayer().getInventory().clear();
        p.getPlayer().setGameMode(GameMode.SURVIVAL);
        p.getPlayer().setVelocity(new Vector(0, 0, 0));
        p.getPlayer().teleport(loc);
        for(Player player : loc.getWorld().getPlayers()) {
            p.getPlayer().showPlayer(QuartzDefenders.getInstance(), player);
        }
        p.getPlayer().setScoreboard(gameScoreboard);
        
        p.setDisplayName(team.getColor());
        
        p.getPlayer().setHealth(20);
        p.getPlayer().setFoodLevel(20);
        p.getPlayer().setExp(0);
        p.getPlayer().setLevel(0);
        p.getPlayer().setTotalExperience(0);
        
        for(PotionEffect e : p.getPlayer().getActivePotionEffects()) {
            p.getPlayer().removePotionEffect(e.getType());
        }
        
        broadcastMessage(LoggerUtil.gameMessage(Language.getString("game.game"),
                Language.getString("game.reconnect_success_2", new Replacer("{0}", p.getDisplayName()))));

        p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.reconnect_success_1")));
        
        //team.respawnPlayer(p);
        // Simulate death to make player respawn
        Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(p.getPlayer(), Collections.EMPTY_LIST, 0, p.getDisplayName() + " joined the game"));
        
    }

    public void disableJoinGame(GamePlayer p) {
        if (!disableJoinGame.contains(p)) {
            disableJoinGame.add(p);
        }
    }

    public boolean isJoinGameDisabled(GamePlayer p) {
        return disableJoinGame.contains(p);
    }

    public void disableJoinTeam(GamePlayer p) {
        if (!disableJoinTeam.contains(p)) {
            disableJoinTeam.add(p);
        }
    }

    public boolean isJoinTeamDisabled(GamePlayer p) {
        return disableJoinTeam.contains(p);
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
    
    public boolean isAutoSmelt() {
        return autosmelt;
    }
    
    public boolean isLoadSuccess() {
        return loadSuccess;
    }

    public boolean isGameState(GameState state) {
        return getGameState() == state;
    }

    public boolean containsPlayer(GamePlayer player) {
        for(GamePlayer gp : gameAllPlayers) {
            if(gp.getPlayer().getUniqueId().equals(player.getPlayer().getUniqueId())) return true;
        }
        return false;
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

    public boolean isInTeam(GamePlayer player) {
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
    
    public boolean isRangedBlocked() {
        return blockRangedCraft;
    }

    /*
	 * GETTERS
     */
    
    public ComboManager getComboManager() {
        return comboManager;
    }
    
    public VoteManager getVoteManager() {
        return voteManager;
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

    public final GameTeam getTeam(String team) {
        return teams.get(team.toUpperCase());
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

    public boolean isAutostart() {
        return this.autostart;
    }

    public Collection<Location> getShopLocations() {
        return shopLocations.values();
    }

    public Map<Location, Integer> getAlchemicsLocations() {
        return alchemistsLocations;
    }
    
    public int getAlchemistDelay() {
        return alchemistDelay;
    }
    
    public Map<Location, Integer> getDefendersLocations() {
        return defendersLocations;
    }
    
    public int getDefenderDelay() {
        return defenderDelay;
    }

    public boolean isDiamondDefenders() {
        return diamondDef;
    }

    public Ores getGameOres() {
        return ores;
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

    public Calendar getStartTime() {
        return started;
    }

    public GameState getGameState() {
        return state;
    }

    public Scoreboard getScoreboard() {
        return gameScoreboard;
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
            case "AQUA":
                return ChatColor.AQUA;
            case "WHITE":
                return ChatColor.WHITE;
            case "MAGENTA":
                return ChatColor.DARK_PURPLE;
            case "BLACK":
                return ChatColor.BLACK;
            case "PINK":
                return ChatColor.LIGHT_PURPLE;
            case "GRAY":
                return ChatColor.DARK_GRAY;
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

    private void generateSpectatorRoom(Material material) {
        Location start = mapSpawn.clone().subtract(10, 1, 10);
        for (int x = 0; x <= 20; x++) {
            for (int z = 0; z <= 20; z++) {
                start.clone().add(x, 0, z).getBlock().setType(material);
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x <= 20; x++) {
                start.clone().add(x, y, 0).getBlock().setType(material);
            }
            for (int x = 0; x <= 20; x++) {
                start.clone().add(x, y, 20).getBlock().setType(material);
            }
            for (int z = 0; z <= 20; z++) {
                start.clone().add(0, y, z).getBlock().setType(material);
            }
            for (int z = 0; z <= 20; z++) {
                start.clone().add(20, y, z).getBlock().setType(material);
            }
        }
    }

    /*
     * SETUP GAME
     */
    public void addRegenerativeBlock(Block b, Player p) {
        FileConfiguration cfg = QuartzDefenders.getInstance().getConfigs().getGameInfo();

        if (!cfg.isConfigurationSection("Games." + id + ".regenerative_blocks." + b.getType().toString())) {
            cfg.set("Games." + id + ".regenerative_blocks." + b.getType().toString() + ".regenerate_time", 200);
            ArrayList<String> l = new ArrayList<>();
            String s = b.getX() + "," + b.getY() + "," + b.getZ();
            l.add(s);
            cfg.set("Games." + id + ".regenerative_blocks." + b.getType().toString() + ".list", l);
        } else {
            ArrayList<String> l = (ArrayList<String>) cfg.getStringList("Games." + id + ".regenerative_blocks." + b.getType().toString() + ".list");
            String s = b.getX() + "," + b.getY() + "," + b.getZ();
            if (l.contains(s)) {
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
        if(!b.getType().equals(Material.NETHER_QUARTZ_ORE)) {
            p.sendMessage(LoggerUtil.gameMessage("Setup", "&cIt's not quartz ore!"));
            return;
        }
        if (!isTeamValid(team.toUpperCase())) {
            p.sendMessage(LoggerUtil.gameMessage("Setup", "&cTeam " + team + "&c is not valid"));
            return;
        }

        FileConfiguration cfg = QuartzDefenders.getInstance().getConfigs().getGameInfo();
        
        cfg.set("Games." + id + ".locations." + team + ".quartz.x", b.getX());
        cfg.set("Games." + id + ".locations." + team + ".quartz.y", b.getY());
        cfg.set("Games." + id + ".locations." + team + ".quartz.z", b.getZ());

        p.sendMessage(LoggerUtil.gameMessage("Setup", "&aQuartz setup success. Team&f: " + team
                + "&f, X:&a " + b.getX() + "&f,Y:&a " + b.getY() + "&f,Z:&a " + b.getZ()));
        QuartzDefenders.getInstance().getConfigs().saveGameInfo();
    }

    public void setSpawn(Location loc, String team, Player p) {
        if (!isTeamValid(team.toUpperCase())) {
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
    
    private boolean isVoting() {
        for(VoteObject obj : voteManager.getVoteObjects()) {
            if(obj.isVoting()) return true;
        }
        return false;
    }
}
