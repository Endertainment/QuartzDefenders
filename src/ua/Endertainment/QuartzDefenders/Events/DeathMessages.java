package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

public class DeathMessages implements Listener {

	private QuartzDefenders plugin;
	
	public DeathMessages(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Player killer = p.getKiller();
		
		Game game = plugin.getGame(p); 
		
		if(game != null) {
			if(killer == null) {
				e.setDeathMessage("");
				game.broadcastMessage("&8» &r" + p.getDisplayName() + "&f died");
				return;
			}
			if(killer != null && killer instanceof Player) {
				e.setDeathMessage("");
				game.broadcastMessage("&8» &r" + p.getDisplayName() + "&f killed by &r" + killer.getDisplayName());
				return;
			}			
			return;
		}
		
		if(killer == null) {
			e.setDeathMessage(new ColorFormat("&8» &r" + p.getDisplayName() + "&f died").format());
		}
		if(killer != null && killer instanceof Player) {
			e.setDeathMessage(new ColorFormat("&8» &r" + p.getDisplayName() + "&f killed by &r" + killer.getDisplayName()).format());
		}
	}
	
}
