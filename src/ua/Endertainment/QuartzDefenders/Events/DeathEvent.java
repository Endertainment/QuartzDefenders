package ua.Endertainment.QuartzDefenders.Events;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.Language;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;
import ua.Endertainment.QuartzDefenders.Utils.Replacer;

public class DeathEvent implements Listener {

	private QuartzDefenders plugin;
	private Set<Player> freeze; 
	
	public DeathEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		this.freeze = new HashSet<>();
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Player killer = p.getKiller();
		if(plugin.getGame(p) == null) {
			return;
		}
		
		Game game = plugin.getGame(p);
		int resp = game.getPlayersRespawnTime();
		
		if(game.getTeam(p) == null) {
			p.setHealth(20);
			p.setGameMode(GameMode.SPECTATOR);
			p.teleport(game.getMapCenter());
			return;
		}
		
		GameTeam team = game.getTeam(p);
		
		if(killer != null && killer instanceof Player) {
			game.getKillsStats().addKill(plugin.getGamePlayer(killer));
			
			StatsPlayer sp = new StatsPlayer(killer);	
			sp.addKill();
		}
		
		game.getSidebar().refresh();
		
		StatsPlayer sp = new StatsPlayer(p);
		sp.addDeath();
		
		if(team.canRespawn()) {
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setGameMode(GameMode.SPECTATOR);
			p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.respawn", new Replacer("{0}", resp + ""))));
			freeze.add(p);
			if(p.getLocation().getY() <= 0) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {					
					@Override
					public void run() {
						p.teleport(new Location(p.getWorld(), p.getLocation().getX(), 80, p.getLocation().getZ()));						
					}
				});				
			}
			BukkitRunnable run = new BukkitRunnable() {
				@Override
				public void run() {							
					p.setHealth(20);
					p.setFoodLevel(20);
					p.setGameMode(GameMode.SURVIVAL);
					p.teleport(team.getSpawnLocation());
					p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5*20, 200));
					freeze.remove(p);
				}
			};
			run.runTaskLater(plugin, (resp * 20));
		} else {
			team.removePlayer(plugin.getGamePlayer(p));
			p.setHealth(20);
			p.setGameMode(GameMode.SPECTATOR);
			game.getSpectators().add(plugin.getGamePlayer(p));
			
			if(p.getLocation().getY() <= 0) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {					
					@Override
					public void run() {
						p.teleport(new Location(p.getWorld(), p.getLocation().getX(), 80, p.getLocation().getZ()));						
					}
				});				
			}
			
		}
	
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(freeze.contains(e.getPlayer())) {
			Location f = e.getFrom();
			Location t = e.getTo();
			if(f.getX() != t.getX() || f.getY() != t.getY() || f.getZ() != t.getZ()) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if(plugin.getGame(p) != null) {
			if(e.getCause().equals(TeleportCause.SPECTATE)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler 
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(plugin.getGame(p) == null) {
			return;
		}
		
		if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
			return;
		} 
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			if(e.getClickedBlock().getType().equals(Material.CHEST) || e.getClickedBlock().getType().equals(Material.FURNACE)) {
				e.setCancelled(true);
			}
			
		}
	}
	
}
