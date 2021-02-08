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
import ua.endertainment.quartzdefenders.utils.LobbySidebar;
import ua.endertainment.quartzdefenders.utils.Symbols;

public class QuitListener implements Listener {

    private QuartzDefenders plugin;

    public QuitListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("QuartzDefenders.lobby.alert.quit")) {
            e.setQuitMessage(new ColorFormat("&3"+Symbols.RIGHT_QUOTE+" &f[&3-&f] &r" + p.getDisplayName()).format());
        } else {
            e.setQuitMessage("");
        }

        for (Player pp : Bukkit.getOnlinePlayers()) {
            if (pp.getUniqueId() == p.getUniqueId()) {
                continue;
            }
            new LobbySidebar(plugin, pp).setScoreboard();
        }

        GamePlayer pp = plugin.getGamePlayer(p);
        if (plugin.getGame(p) != null) {
            Game game = plugin.getGame(p);
            if (!game.isGameState(GameState.ACTIVE) || game.getSpectators().contains(pp)) {
                game.quitGame(pp);
            } else {
                BukkitRunnable runnable = new GameLeaveTimer(game, pp);
                runnable.runTaskTimerAsynchronously(plugin, 0, 20); //may cause some thoubles
                p.setHealth(0);
            }
        }

    }

}
