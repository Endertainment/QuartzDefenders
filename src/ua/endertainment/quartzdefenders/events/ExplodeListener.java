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
        if (plugin.isLobby(e.getLocation().getWorld())) {
            return;
        }
        Game game = plugin.getGame(e.getLocation().getWorld());

        if (game == null) {
            return;
        }
        List<Block> blocks = e.blockList();

        for (Block b : blocks) {
            if (game.getGameOres().isOre(b.getLocation())) {
                BlockBreakListener.regenBlock(b.getLocation());
            }
        }

    }

}
