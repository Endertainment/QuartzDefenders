package ua.Endertainment.QuartzDefenders.Events;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class ExplodeEvent implements Listener {

	private QuartzDefenders plugin;
	
	public ExplodeEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		if(e.getLocation().getWorld() == plugin.getLobby().getWorld()) {
			return;
		}
		
		Bukkit.broadcastMessage("1");
		
		Game game = null;
		
		for(Game g : plugin.getGames()) {
			if(e.getLocation().getWorld() == g.getGameWorld()) game = g;
			
		}
		
		Bukkit.broadcastMessage("2");
		
		if(game == null) {
			return;
		}
		
		Bukkit.broadcastMessage("3");
		
		List<Block> blocks = e.blockList();
		
		for(Block b : blocks) {
			
			Bukkit.broadcastMessage("4");
			
			if(game.getRegenerativeBlocks().containsKey(b.getType())) {
				
				Bukkit.broadcastMessage("5");
				
				Set<Location> locs = game.getRegenerativeBlocks().get(b.getType());
				
				for(Location loc : locs) {
					
					Bukkit.broadcastMessage("6");
					
					if(b.getLocation().equals(loc)) {
						
						Bukkit.broadcastMessage("7");
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                                    b.setType(b.getType());
                                                });
					}
				}
			}
		}
		
	}
			
	
}
