package ua.endertainment.quartzdefenders.game;

import ua.endertainment.quartzdefenders.game.GameQuartz;
import ua.endertainment.quartzdefenders.game.Game;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {

    private Game game;

    private int time = 0;
    private String format;

    public GameTimer(Game game) {
        this.game = game;
        format = "HH:MM:SS";
    }

    @Override
    public void run() {
        time++;
        game.sendTabList();
        for (GameQuartz q : game.getQuartzsLocations().values()) {
            q.checkQuartz();
        }
        if (time >= 120) {
            game.checkGameEnd();
        }
        if (time >= (60 * 60)) {
            if (time % 60 == 0) {
                for (GameQuartz quartz : game.getQuartzsLocations().values()) {
                    if (quartz.getQuartzHealth() > 0) {
                        quartz.breakQuartz();
                    }
                }
            }
        }
    }

    public void stop() {
        cancel();
    }

    public int getSeconds() {
        return time;
    }

    public String getStringTime() {
        int h = time / 3600;
        int m = time / 60 % 60;
        int s = time % 60;
        return format.replace("HH", a(h)).replace("MM", a(m)).replace("SS", a(s));
    }

    private String a(int time) {
        return time < 10 ? "0" + time : "" + time;
    }
}
