package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class WorldChangeEvent implements Listener {

	private QuartzDefenders plugin;
	
	public WorldChangeEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		World to = e.getPlayer().getLocation().getWorld();
		Player player = e.getPlayer();
		if(to == plugin.getLobby().getWorld()) {
			return;
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.hidePlayer(player);
			player.hidePlayer(p);
		}
		for(Player p : to.getPlayers()) {
			p.showPlayer(player);
			player.showPlayer(p);
		}
	}
	
}
