package ua.Endertainment.QuartzDefenders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

public class GameLeaveTimer extends BukkitRunnable {

	private Game game;
	private GamePlayer player;
	private Player pplayer;
	private int time;
	
	public GameLeaveTimer(Game game, GamePlayer player, Player pplayer) {
		this.game = game;
		this.player = player;
		this.pplayer = pplayer;
		this.time = 300;
	}
	
	
	@Override
	public void run() {
		if(Bukkit.getServer().getPlayerExact(pplayer.getName()) == null) {
			if(time == 0) {
				cancel();
				game.quitGame(player);
			} 
			else if(time == 300 || time == 120 || time == 60) {
				game.broadcastMessage(LoggerUtil.gameMessage("Quit", "Player " + pplayer.getDisplayName() 
									+ "&7 has &b" + (time/60) + "&7 minutes to reconnect"));
			}		
			time--;			
		} else cancel();
	}

	
}
