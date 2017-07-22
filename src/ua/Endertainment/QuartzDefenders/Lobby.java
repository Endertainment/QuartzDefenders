package ua.Endertainment.QuartzDefenders;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import ua.Endertainment.QuartzDefenders.Items.QItems;
import ua.Endertainment.QuartzDefenders.Utils.TitleUtil;
import ua.Endertainment.QuartzDefenders.Utils.Language;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;
import ua.Endertainment.QuartzDefenders.Utils.ScoreboardLobby;

public class Lobby implements Listener {
	
	private QuartzDefenders plugin;
	private Location location;

	private ArrayList<Player> hide = new ArrayList<>();
	
	public Lobby(QuartzDefenders plugin) {
		this.plugin = plugin;
		
		if(plugin.getConfig().getString("Lobby.lobby_world_name") == null || plugin.getConfig().getString("Lobby.lobby_world_name").equalsIgnoreCase("null")) {
			LoggerUtil.logError("Can not find 'Lobby.lobby_world_name' in configuration file. Check config or visit web page to see more info");
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
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}

	public void sendTabList(Player p) {
		String header = Language.getString("tablist.lobby.header");
		String footer = Language.getString("tablist.lobby.footer");
		
		TitleUtil.sendTabTitle(p, header, footer);
	}
	public void sendTabListForLobby() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getWorld() == location.getWorld())	sendTabList(p);
		}
	}
	
	public void hidePlayers(Player p) {
		if(p.getLocation().getWorld() != location.getWorld()) return;
		if(!hide.contains(p)) {
			hide.add(p);
			for(Player targ : Bukkit.getOnlinePlayers()) {
				if(targ.hasPermission("QuartzDefenders.lobby.visible")) {
					if(targ.getLocation().getWorld() != location.getWorld()) {
						p.hidePlayer(targ);
						continue;
					}
					else continue;
				}
				p.hidePlayer(targ);
			}
		}
	}
	public void showPlayers(Player p) {
		if(p.getLocation().getWorld() != location.getWorld()) return;
		if(hide.contains(p)) {
			hide.remove(p);
			for(Player targ : location.getWorld().getPlayers()) {
				p.showPlayer(targ);
			}
		}
	}
	
	public ArrayList<Player> getHides() {
		return hide;
	}
	
	public void teleportToSpawn(Player p, boolean arg) {
		if(!arg) if(p.getLocation() != location.getWorld()) p.teleport(location);
		else p.teleport(location);
	}
	public Location getLocation() {
		return location;
	}
	public World getWorld() {
		return location.getWorld();
	}
	public void setLobbyTools(Player p) {
		if(p.getWorld() != getWorld()) return;
		p.getInventory().clear();
		
		p.getInventory().setItem(0, QItems.itemGamesChoose());
		p.getInventory().setItem(4, QItems.itemStats());
		p.getInventory().setItem(7, QItems.itemHidePlayers(getHides().contains(p)));
		p.getInventory().setItem(8, QItems.itemLobbyShop());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setExp(0);
		p.setLevel(0);
		p.teleport(getLocation());
		sendTabList(p);
		for(Player pp : Bukkit.getOnlinePlayers()) {
			if(hide.contains(pp) && pp.getWorld() == location.getWorld() && !p.hasPermission("QuartzDefenders.lobby.visible")) {					
				pp.hidePlayer(p);
			}
			if(pp.getWorld() != location.getWorld()) {
				p.hidePlayer(pp);
			}
		}
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!plugin.getConfig().getBoolean("Lobby.disable_damage")) return;
		if(!(e.getEntity() instanceof Player)) return;
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
		if(e.getPlayer().getLocation().getWorld() != location.getWorld()) return;
		if(!e.getPlayer().hasPermission("QuartzDefenders.lobby.blockBreak")) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getPlayer().getLocation().getWorld() != location.getWorld()) return;
		if(!e.getPlayer().hasPermission("QuartzDefenders.lobby.blockPlace")) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getPlayer().getLocation().getWorld() != location.getWorld()) return;
		if(e.getTo().getBlockY() <= 0) teleportToSpawn(e.getPlayer(), true);
	}
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		if(e.getFrom() != location.getWorld()) {
			Player bp = e.getPlayer();
			e.getPlayer().setHealth(20);
			e.getPlayer().setFoodLevel(20);
			e.getPlayer().setExp(0);
			e.getPlayer().setLevel(0);
			
			bp.setGameMode(GameMode.ADVENTURE);
			
			bp.setHealth(20);
			bp.setFoodLevel(20);
			
			setLobbyTools(bp);
			
			ScoreboardLobby sb = new ScoreboardLobby(plugin, bp);
			sb.setScoreboard();
			
			GamePlayer p = plugin.getGamePlayer(e.getPlayer());
			p.resetDisplayName();
			if(p.getPlayer().hasPermission("QuartzDefenders.lobby.colorName")) {
				p.setDisplayName(ChatColor.AQUA);
				
			}
			
			for(String s : QuartzDefenders.getInstance().getDevs()) {
				if(s.equalsIgnoreCase(p.getPlayer().getName())) {
					p.setDisplayName(ChatColor.DARK_RED);
				}
			}
			sendTabList(bp);
			
			for(Player targ : location.getWorld().getPlayers()) {
				targ.showPlayer(e.getPlayer());
				e.getPlayer().showPlayer(targ);
			}
			
		}
		
	}

}
