package ua.Endertainment.QuartzDefenders;

import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {

	private Game game;
	
	private int time = 0;
	
	public GameTimer(Game game) {
		this.game = game;
	}
	
	@Override
	public void run() {
		time++;
		//Bukkit.broadcastMessage("GameTimer debug");
		game.sendTabList();
		for(GameQuartz q : game.getQuartzsLocations().values()) {
			q.checkQuartz();
		}
		if(time >= 120) {
			game.checkGameEnd();
		}
		if(time >= (60*60)) {
			for(GameQuartz quartz : game.getQuartzsLocations().values()) {
				quartz.breakQuartz();
			}
		}
	}
	
	public void stop() {
		cancel();
	}
	
	public int getIntTime() {
		return time;
	}
	public String getStringTime() {
		String times = "HH:MM:SS";		
		int h = time / 3600;		
		if((h + "").length() == 1 ) times = times.replace("HH", "0" + h);
		else times = times.replace("HH", h + "");		
		int x = time - (h*3600);		
		int m = x / 60;		
		if((m + "").length() == 1 ) times = times.replace("MM", "0" + m);
		else times = times.replace("MM", m + "");		
		int s = time - ((h*3600) + (m*60));				
		if((s + "").length() == 1 ) times = times.replace("SS", "0" + s);
		else times = times.replace("SS", s + "");
		return times;
	}
}
