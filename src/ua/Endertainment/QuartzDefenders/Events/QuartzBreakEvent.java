package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GamePlayer;
import ua.Endertainment.QuartzDefenders.GameQuartz;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class QuartzBreakEvent implements Listener {
	
	private QuartzDefenders plugin;

	public QuartzBreakEvent(QuartzDefenders plugin) {
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
		
		if(b.getWorld() != game.getGameWorld()) {
			return;
		}
		
		if(b.getType() != Material.QUARTZ_ORE) {
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
