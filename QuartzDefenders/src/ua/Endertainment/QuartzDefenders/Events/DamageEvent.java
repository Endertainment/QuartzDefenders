package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.Game.GameState;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class DamageEvent implements Listener {

	 private QuartzDefenders plugin;
	
	 public DamageEvent(QuartzDefenders plugin) {
		 this.plugin = plugin;
		 Bukkit.getPluginManager().registerEvents(this, plugin);
	 }
	 
	 @EventHandler
	 public void onDamage(EntityDamageEvent e) {
		 if(!(e.getEntity() instanceof Player)) {
			 return;
		 }
		 
		 Player p = (Player) e.getEntity();
		 
		 Game game = plugin.getGame(p);
		 
		 if(game == null) {
			 return;
		 }
		 if(p.getWorld() != game.getGameWorld()) {
			 return;
		 }
		 if(game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING) || game.isGameState(GameState.ENDING)) {
			 e.setCancelled(true);
		 }
	 }
}
