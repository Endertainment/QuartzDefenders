package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import ua.endertainment.quartzdefenders.QuartzDefenders;

public class WorldChangeListener implements Listener {

	private QuartzDefenders plugin;
	
	public WorldChangeListener(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		World to = e.getPlayer().getLocation().getWorld();
		Player player = e.getPlayer();
		if(to.equals(plugin.getLobby().getWorld())) {
			return;
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.hidePlayer(plugin,player);
			player.hidePlayer(plugin,p);
		}
		for(Player p : to.getPlayers()) {
			p.showPlayer(plugin,player);
			player.showPlayer(plugin,p);
		}
	}
	
}
