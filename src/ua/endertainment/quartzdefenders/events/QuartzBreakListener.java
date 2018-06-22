package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.GameQuartz;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class QuartzBreakListener implements Listener {
	
	private QuartzDefenders plugin;

	public QuartzBreakListener(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		
		if(plugin.getGame(p) == null) {
			return;
		}
		
		Game game = plugin.getGame(p);
		Block b = e.getBlock();
		
		if(!b.getWorld().equals(game.getGameWorld())) {
			return;
		}
		
		if(b.getType().equals(Material.QUARTZ_ORE)) {
			return;
		}
		
		if(game.getQuartz(b.getLocation()) == null) {
			return;
		}
		
		GamePlayer gp = plugin.getGamePlayer(p);
				
		GameQuartz quartz = game.getQuartz(b.getLocation());
		
		if(!quartz.breakQuartz(gp)) e.setCancelled(true);
		game.getSidebar().refresh();
	}

}
