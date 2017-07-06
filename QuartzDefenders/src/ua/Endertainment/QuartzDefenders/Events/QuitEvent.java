package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.Game.GameState;
import ua.Endertainment.QuartzDefenders.GameLeaveTimer;
import ua.Endertainment.QuartzDefenders.GamePlayer;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

public class QuitEvent implements Listener {

	private QuartzDefenders plugin;
	
	public QuitEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("QuartzDefenders.lobby.joinAlert")) {
			e.setQuitMessage(new ColorFormat("&3» &f[&3-&f] &r" + p.getDisplayName()).format()); 
		} else {
			e.setQuitMessage("");
		}
		GamePlayer pp = plugin.getGamePlayer(p);
		if(plugin.getGame(p) != null) {
			Game game = plugin.getGame(p);
			if(game.isGameState(GameState.LOBBY) || game.isGameState(GameState.WAITING) || 
					game.isGameState(GameState.STARTING) || game.isGameState(GameState.ENDING)) {
				game.quitGame(pp);
				return;
			}
			
			if(game.isGameState(GameState.ACTIVE)) {
				if(game.getSpectators().contains(pp)) {
					game.quitGame(pp);
					return;
				}
				BukkitRunnable runnable = new GameLeaveTimer(game, pp, p);
				runnable.runTaskTimer(plugin, 0, 20);
				p.setHealth(0);
			}
		}
		
		QuartzDefenders.resetTabList(p);
	}
	
}
