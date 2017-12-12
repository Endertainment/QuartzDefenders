package ua.endertainment.quartzdefenders.stats;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ua.endertainment.quartzdefenders.QuartzDefenders;

public class PlayerJoinStats implements Listener{

	public QuartzDefenders plugin;
	
	public PlayerJoinStats(QuartzDefenders plugin) {
		this.plugin = plugin;		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		String uuid = p.getUniqueId().toString();
		
		FileConfiguration f = plugin.getConfigs().getStatsInfo();
		
		if(!f.isConfigurationSection(uuid)) {		
			f.set(uuid + ".points", 0);
			f.set(uuid + ".coins",  0);
			f.set(uuid + ".kills",  0);
			f.set(uuid + ".deaths", 0);
			f.set(uuid + ".wins",   0);	
			f.set(uuid + ".games",  0);
			f.set(uuid + ".quartz", 0);
			f.set(uuid + ".placed_blocks", 0);
			f.set(uuid + ".ores", 0);
			
			plugin.getConfigs().saveStatsInfo();
		}
	}
	
}
