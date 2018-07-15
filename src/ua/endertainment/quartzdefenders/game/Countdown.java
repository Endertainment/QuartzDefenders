package ua.endertainment.quartzdefenders.game;

import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.Game;
import java.util.Map;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import ua.endertainment.quartzdefenders.utils.FireworkUtil;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.Replacer;
import ua.endertainment.quartzdefenders.utils.TitleUtil;

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
            for (GamePlayer p : game.getPlayers()) {
                TitleUtil.sendTitle(p.getPlayer(), Language.getString("game.title.started.top"), Language.getString("game.title.started.bot"), 5);
            }
            for (Map.Entry<String, GameTeam> team : game.getTeams().entrySet()) {
                FireworkUtil.spawn(team.getValue().getSpawnLocation().add(0, 1, 0), 2L);
            }
        } else if (time == 20 || time == 15 || time == 10 || time <= 5) {
            for (GamePlayer p : game.getPlayers()) {
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                p.getPlayer().sendTitle(Language.getString("game.title.waiting.top", new Replacer("{0}", game.getColorWorldName())), Language.getString("game.title.waiting.bot", new Replacer("{0}", time + "")), 0, 70, 20);
            }
            if(time==5) {
                game.endVoting();
            }
        }
        time -= 1;
    }

}
