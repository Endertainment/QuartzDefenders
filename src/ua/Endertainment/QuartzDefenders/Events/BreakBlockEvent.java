package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.Game.GameState;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class BreakBlockEvent implements Listener {

	private QuartzDefenders plugin;
	
	public BreakBlockEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(plugin.getGame(p) != null) {
			Game game = plugin.getGame(p);
			FileConfiguration config = plugin.getConfigs().getGameInfo();
			
			if(game.isGameState(GameState.ACTIVE)) {
				Material material = e.getBlock().getType();
				
				if(game.getRegenerativeBlocks().containsKey(material)) {
					Block block = e.getBlock();
					
					for(Location loc : game.getRegenerativeBlocks().get(material)) {						
						if(loc.getBlockX() == block.getX() && loc.getBlockY() == block.getY() && loc.getBlockZ() == block.getZ()) {
													
							StatsPlayer sp = new StatsPlayer(p);
							sp.addBrokenOre();
							
							int regenerateTime = config.getInt("Games." + game.getGameId() + 
									".regenerative_blocks." + material.toString() + ".regenerate_time");
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { 
								public void run() {block.setType(Material.BEDROCK);}
							} );
							
							BukkitRunnable runnable = new BukkitRunnable() {
								
								@Override
								public void run() {
									if(e.getBlock().getWorld() != null) {
										block.setType(material);
									}
								}
							};
							runnable.runTaskLater(plugin, regenerateTime);
						}
						
					}
					
				}
				
			}
			
		}
	}
	
}
