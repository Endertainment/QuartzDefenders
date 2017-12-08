package ua.Endertainment.QuartzDefenders;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import ua.Endertainment.QuartzDefenders.Game.GameState;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.Language;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;
import ua.Endertainment.QuartzDefenders.Utils.Replacer;

public class GameTeam {

    private Game game;
    private String name;
    private int playersInTeam;
    private Scoreboard scoreboard;
    private Location spawnLocation;
    private ChatColor color;
    private Team team;
    private Set<GamePlayer> players = new HashSet<>();

    private boolean allowJoin;

    public GameTeam(Game game, String name, ChatColor color, int playersInTeam, Location spawnLocation, Scoreboard scoreboard) {
        this.game = game;
        this.name = name;
        this.playersInTeam = playersInTeam;
        this.scoreboard = scoreboard;
        this.color = color;
        this.spawnLocation = spawnLocation;
        this.allowJoin = true;

        this.team = this.scoreboard.registerNewTeam(this.name);
        this.team.setAllowFriendlyFire(false);
        this.team.setCanSeeFriendlyInvisibles(true);
        this.team.setPrefix(color.toString());
        this.team.setOption(Option.COLLISION_RULE, OptionStatus.FOR_OWN_TEAM);
    }

    public Game getGame() {
        return game;
    }

    public int intPlayersInTeam() {
        return playersInTeam;
    }

    public void joinTeam(GamePlayer player, boolean access) {
        if (!isJoinAllow() && !access) {
            if (!player.getPlayer().hasPermission("QuartzDefenders.team.vipJoin")) {
                player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.team_locked")));
                return;
            }
        }
        if (!canJoin() && !access) {
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("team.team_join_disallow")));
            return;
        }
        if(game.isJoinTeamDisabled(player)) {
        	player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("team.team_join_disallow")));
        	return;
        }        
        if (game.isPlayerInTeam(player) && game.getTeam(player.getPlayer()) != this) {
            game.getTeam(player.getPlayer()).quitTeam(player);
        }

        if (game.getSpectators().contains(player)) {
            game.getSpectators().remove(player);
        }

        if (addPlayer(player)) {
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("team.team_join", new Replacer("{0}", getName()))));

                if (game.isAutostart() && game.isGameReady()) {
                    game.startCountdown();
                }

            if (game.isGameState(GameState.ACTIVE)) {
                respawnPlayer(player);
                //player.getPlayer().setCollidable(false); not working with arrows!

                StatsPlayer sp = new StatsPlayer(player.getPlayer());
                sp.addPlayedGame();
            }
            game.refreshScoreboard();
            return;
        }
        player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.team_already")));
    }

    public void quitTeam(GamePlayer player) {
        game.getSidebar().refresh();
        if (removePlayer(player)) {
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("team.team_quit", new Replacer("{0}", getName()))));

            if (game.isGameState(GameState.ACTIVE) || game.isGameState(GameState.ENDING)) {
                game.getSpectators().add(player);
                player.getPlayer().setGameMode(GameMode.SPECTATOR);
                player.getPlayer().getInventory().clear();
                game.disableJoinTeam(player);
            }

            return;
        }
    }

    public boolean addPlayer(GamePlayer player) {
        if (players.contains(player)) {
            return false;
        } else if (players.size() < playersInTeam) {
            team.addEntry(player.getPlayer().getUniqueId().toString());
            players.add(player);
            player.setDisplayName(color);
            return true;
        }
        return false;
    }

    public boolean addEntity(Entity entity) {
        if (team.getEntries().contains(entity.getUniqueId().toString())) {
            return false;
        } else {
            team.addEntry(entity.getUniqueId().toString());
            return true;
        }
    }

    public boolean removePlayer(GamePlayer player) {
        if (players.contains(player)) {
            team.removeEntry(player.getPlayer().getUniqueId().toString());
            players.remove(player);
            player.resetDisplayName();
            return true;
        }
        return false;
    }

    public void setAllowJoin(boolean arg) {
        this.allowJoin = arg;
    }

    public boolean isJoinAllow() {
        return allowJoin;
    }

    public void sendTeamMessage(String s) {
        for (GamePlayer p : players) {
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

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void teleportToSpawnLocation() {
        for (GamePlayer player : players) {
            player.teleport(spawnLocation);
        }
    }

    public void teleportToSpawnLocation(GamePlayer p) {
        p.teleport(spawnLocation);
    }

    public boolean canRespawn() {
        return game.getQuartz(this).getQuartzHealth() > 0;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean canJoin() {
        int x = game.getQuartzHealth() * game.getTeamsCount();
        int y = 0;
        for (GameQuartz quartz : game.getQuartzsLocations().values()) {
            y += quartz.getQuartzHealth();
        }
        return x == y;
    }
    
    private void respawnPlayer(GamePlayer p) {
    	p.getPlayer().setHealth(20);
    	p.getPlayer().setFoodLevel(20);
    	p.getPlayer().setGameMode(GameMode.SPECTATOR);
    	p.getPlayer().sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.respawn", new Replacer("{0}", game.getPlayersRespawnTime() + ""))));
		if(p.getPlayer().getLocation().getY() <= 0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(QuartzDefenders.getInstance(), new Runnable() {					
				@Override
				public void run() {
					p.teleport(new Location(p.getPlayer().getWorld(), p.getPlayer().getLocation().getX(), 80, p.getPlayer().getLocation().getZ()));						
				}
			});				
		}
		BukkitRunnable run = new BukkitRunnable() {
			@Override
			public void run() {							
				p.getPlayer().setHealth(20);
				p.getPlayer().setFoodLevel(20);
				p.getPlayer().setGameMode(GameMode.SURVIVAL);
				p.getPlayer().teleport(getSpawnLocation());
				p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5*20, 200));
				p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5*20, 24));
			}
		};
		run.runTaskLater(QuartzDefenders.getInstance(), (game.getPlayersRespawnTime() * 20));
    }
}
