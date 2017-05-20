package ua.Endertainment.QuartzDefenders;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.Connector;

public class Lobby implements Listener {
	
	private QuartzDefenders plugin;
	private Location location;

	private ArrayList<Player> hide = new ArrayList<>();
	
	public Lobby(QuartzDefenders plugin) {
		this.plugin = plugin;
		
		if(plugin.getConfig().getString("Lobby.lobby_world_name") == null || plugin.getConfig().getString("Lobby.lobby_world_name").equalsIgnoreCase("null")) {
			Bukkit.broadcastMessage(ChatColor.RED + "ERROR WHITE LOADING PLUGIN. CHECK FILE CONFIGURATION AND EDIT IT.");
			Bukkit.getPluginManager().disablePlugin(plugin);
			return;
		}
		
		World world = Bukkit.getWorld(plugin.getConfig().getString("Lobby.lobby_world_name"));
		double x = plugin.getConfig().getDouble("Lobby.spawn.x") + 0.5;
		double y = plugin.getConfig().getDouble("Lobby.spawn.y");
		double z = plugin.getConfig().getDouble("Lobby.spawn.z") + 0.5;
		float yaw = (float) plugin.getConfig().getDouble("Lobby.spawn.yaw");
		float pitch = (float) plugin.getConfig().getDouble("Lobby.spawn.pitch");
		this.location = new Location(world, x, y, z, yaw, pitch);
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
		
	}

	public void sendTabList(Player p) {
		String header = "";
		String footer = "";
		String n = "\n";
		
		String game = "&bLOBBY";
		if(plugin.getGame(p) != null) game = "&3Game&7: &b" + plugin.getGame(p).getGameName();
		
		header = " " + n
				+ "&3\u00AB &b&lPlayCraft.COM.UA &3\u00BB" + n 
				+ " ";
		footer = " " + n
				+ game + n
				+ " ";
		
		Connector.sendTabTitle(p, header, footer);
	}
	public void sendTabListForLobby() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getWorld() == location.getWorld())	sendTabList(p);
		}
	}
	
	public void hidePlayers(Player p) {
		if(!hide.contains(p)) {
			hide.add(p);
			for(Player targ : Bukkit.getOnlinePlayers()) {
				if(!targ.hasPermission("QuartzDefenders.lobby.visible")) {
					p.hidePlayer(targ);
				} else
				if(targ.hasPermission("QuartzDefenders.lobby.visible")) {
					if(targ.getWorld() != location.getWorld()) p.hidePlayer(targ);
				}
			}
		} else {
			hide.remove(p); 
			for(Player targ : Bukkit.getOnlinePlayers()) {
				if(targ.getWorld() == location.getWorld()) 
					p.showPlayer(targ);
			} 
		}
	}
	public ArrayList<Player> getHides() {
		return hide;
	}
	
	public void teleportToSpawn(Player p) {
		p.teleport(location);
	}
	public Location getLocation() {
		return location;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setExp(0);
		p.setLevel(0);
		p.teleport(location);
		sendTabList(p);
		for(Player pp : Bukkit.getOnlinePlayers()) {
			if(hide.contains(pp) && pp.getWorld() == location.getWorld() && !p.hasPermission("QuartzDefenders.lobby.visible")) {					
				pp.hidePlayer(p);
			}
			if(pp.getWorld() != location.getWorld()) {
				p.hidePlayer(pp);
			}
		}
		if(p.hasPermission("QuartzDefenders.lobby.joinAlert")) {
			Bukkit.broadcastMessage(new ColorFormat("&3« &b+ &3» &r" + p.getDisplayName()).format());
		}
		
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!plugin.getConfig().getBoolean("Lobby.disable_damage")) return;
		if(location.getWorld() == e.getEntity().getLocation().getWorld()) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if(!plugin.getConfig().getBoolean("Lobby.disable_damage")) return;
		if(location.getWorld() == e.getEntity().getLocation().getWorld()) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPermission("QuartzDefenders.lobby.blockBreak")) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPermission("QuartzDefenders.lobby.blockPlace")) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getPlayer().getLocation().getWorld() != location.getWorld()) return;
		if(e.getTo().getBlockY() <= 0) teleportToSpawn(e.getPlayer());
	}

}
