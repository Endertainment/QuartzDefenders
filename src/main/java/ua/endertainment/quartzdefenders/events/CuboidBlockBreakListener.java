package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.Cuboid;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class CuboidBlockBreakListener implements Listener {

	private QuartzDefenders plugin;
	
	public CuboidBlockBreakListener(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			
			if(e.isCancelled()) return;
			
	        if (plugin.getGame(p) == null) {
	            return;
	        }

	        Game game = plugin.getGame(p);

	        if (!game.isInTeam(plugin.getGamePlayer(p))) {
	            return;
	        }
	        
	        for (Cuboid c : game.getCuboids()) {
	            if(!c.canBreak(plugin.getGamePlayer(p), e.getBlock().getLocation())) {
	                e.setCancelled(true);
	                p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.block.cant_break")));
	                return;
	            }
	        }
	}
	
}
