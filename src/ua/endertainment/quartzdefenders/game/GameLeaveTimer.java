package ua.endertainment.quartzdefenders.game;

import ua.endertainment.quartzdefenders.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class GameLeaveTimer extends BukkitRunnable {

	private Game game;
	private GamePlayer player;
	private int time;
	
	public GameLeaveTimer(Game game, GamePlayer player) {
		this.game = game;
		this.player = player;
		this.time = 300;
	}
	
	
	@Override
	public void run() {
		if(Bukkit.getServer().getPlayerExact(player.getPlayer().getName()) == null) {
			if(time == 0) {
				cancel();
				game.quitGame(player);
			} 
			else if(time == 300 || time == 120 || time == 60) {
				int x = time/60;
				game.broadcastMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.quit_game_reconnect", new Replacer("{0}", player.getPlayer().getDisplayName()), new Replacer("{1}", x + ""))));
			}		
			time--;			
		} else cancel();
	}

	
}
