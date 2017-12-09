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
		Game game = null;
		
		for(Game g : plugin.getGames()) {
			if(e.getLocation().getWorld() == g.getGameWorld()) game = g;
			
		}

		if(game == null) {
			return;
		}
		List<Block> blocks = e.blockList();
		
		for(Block b : blocks) {
			if(game.getRegenerativeBlocks().containsKey(b.getType())) {
				
				Set<Location> locs = game.getRegenerativeBlocks().get(b.getType());
				for(Location loc : locs) {
					if(b.getLocation().equals(loc)) {
						BreakBlockEvent.regenBlock(loc);
					}
				}
			}
		}
		
	}
			
	
}
