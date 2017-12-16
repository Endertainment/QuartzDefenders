package ua.endertainment.quartzdefenders.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class ExplodeListener implements Listener {

    private QuartzDefenders plugin;

    public ExplodeListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getLocation().getWorld() == plugin.getLobby().getWorld()) {
            return;
        }
        Game game = null;

        for (Game g : plugin.getGames()) {
            if (e.getLocation().getWorld() == g.getGameWorld()) {
                game = g;
            }

        }

        if (game == null) {
            return;
        }
        List<Block> blocks = e.blockList();

        for (Block b : blocks) {
            if (game.getGameOres().isRegenetiveOre(b.getLocation())) {
                BreakBlockListener.regenBlock(b.getLocation());

            }
        }

    }

}
