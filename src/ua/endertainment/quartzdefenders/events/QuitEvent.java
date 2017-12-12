package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.Game.GameState;
import ua.endertainment.quartzdefenders.game.GameLeaveTimer;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.ColorFormat;

public class QuitEvent implements Listener {

	private QuartzDefenders plugin;
	
	public QuitEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("QuartzDefenders.lobby.alert.join")) {
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
				runnable.runTaskTimerAsynchronously(plugin, 0, 20); //may cause some thoubles
				p.setHealth(0);
			}
		}
		
	}
	
}
