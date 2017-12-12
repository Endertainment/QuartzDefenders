package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.Cuboid;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class CuboidBreakBlockEvent implements Listener {

	private QuartzDefenders plugin;
	
	public CuboidBreakBlockEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();

	        if (plugin.getGame(p) == null) {
	            return;
	        }

	        Game game = plugin.getGame(p);

	        if (!game.isPlayerInTeam(plugin.getGamePlayer(p))) {
	            return;
	        }
	        
	        for (Cuboid c : game.getCuboids()) {
	            if(!c.canBreak(plugin.getGamePlayer(p), e.getBlock().getLocation())) {
	                e.setCancelled(true);
	                p.sendMessage(LoggerUtil.gameMessage("Game", "&cYou can not break block here"));
	                return;
	            }
	        }
	}
	
}
