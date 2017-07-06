package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.Cuboid;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class PlaceBlockEvent implements Listener {

	private QuartzDefenders plugin;
	
	public PlaceBlockEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		if(plugin.getGame(p) == null) {
			return;
		}
		
		Game game = plugin.getGame(p);
		
		if(!game.isPlayerInTeam(plugin.getGamePlayer(p))) {
			return;
		}
		
		StatsPlayer sp = new StatsPlayer(p);
		sp.addPlacedBlock();
		
		for(Cuboid c : game.getCuboids()) {
			if(c.contains(e.getBlock().getLocation())) {
				e.setCancelled(true);
				p.sendMessage(GameMsg.gameMessage("Game", "&cYou can not place block here"));
				return;
			}
		}
		
		if(game.getBuildCuboid().contains(e.getBlock().getLocation())) {
			p.sendMessage(GameMsg.gameMessage("Game", "&cYou can not place block here"));
			e.setCancelled(true);
			return;
		}
//		int rad = game.getBuildRadius();
//		int height = game.getBuildHeight();
//		Location loc = e.getBlock().getLocation();
//		Location lo1 = new Location(game.getGameWorld(), game.getMapCenter().getBlockX(), 0, game.getMapCenter().getBlockZ());
//		if(loc.getBlockX() >= lo1.getBlockX() + rad || loc.getBlockZ() >= lo1.getBlockZ()+rad) {			
//			p.sendMessage(GameMsg.gameMessage("Game", "&cYou can not place block here"));
//			e.setCancelled(true);
//			return;
//		}
//		if(loc.getBlockX() <= lo1.getBlockX()-rad || loc.getBlockZ() <= lo1.getBlockZ()-rad ) {
//			p.sendMessage(GameMsg.gameMessage("Game", "&cYou can not place block here"));
//			e.setCancelled(true);
//			return;
//		}
//		if(loc.getBlockY() >= height) {
//			p.sendMessage(GameMsg.gameMessage("Game", "&cYou can not place block here"));
//			e.setCancelled(true);
//			return;
//		}
		
	}
	
}
