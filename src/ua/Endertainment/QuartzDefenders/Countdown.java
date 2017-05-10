package ua.Endertainment.QuartzDefenders;

import org.bukkit.scheduler.BukkitRunnable;

import ua.Endertainment.QuartzDefenders.Utils.Connector;

public class Countdown extends BukkitRunnable {

	
	private int time = 21;
    private Game game;

    public Countdown(Game game) {
         this.game = game;
    }

    @Override
    public void run() {
    	time -= 1;

        if (time == 0) {
            // Start
            Connector.sendTitle("&6Game started", "&7by Endertainment", 30);
            cancel();
            // TODO: Run game task
        } else {
            if (time == 20 || time == 15 || time <= 10) {
            	Connector.sendTitle(game.getGameName(), "&7Game will begin after &6" + time + "&7 seconds", 10);
            }
        }
    }

}
