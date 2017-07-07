package ua.Endertainment.QuartzDefenders;

import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import ua.Endertainment.QuartzDefenders.Utils.FireworkUtil;
import ua.Endertainment.QuartzDefenders.Utils.TitleUtil;

public class Countdown extends BukkitRunnable {

    private int time = 20;
    private Game game;

    public Countdown(Game game) {
         this.game = game;
    }

    @Override
    public void run() {
        if (time == 0) {
            cancel();
        	game.startGame();
        	for(GamePlayer p : game.getPlayers()) {
        		TitleUtil.sendTitle(p.getPlayer(), "&bGame started", "&7Good luck", 5);      
        		FireworkUtil.spawn(p.getPlayer().getLocation(), 2L);
        	}
        } else {
            if (time == 20 || time == 15 || time == 10|| time <= 5) {
            	for(GamePlayer p : game.getPlayers()) {
            		p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
            		TitleUtil.sendTitle(p.getPlayer(), game.getColorWorldName(), "&7Game will begin in &b" + time + "&7 seconds", 3);
            	}
            }
        }
        time -= 1;
    }

}
