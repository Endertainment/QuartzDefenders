package ua.endertainment.quartzdefenders.events;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.Ores;

public class PistonListener implements Listener {

    private QuartzDefenders plugin;

    public PistonListener(QuartzDefenders plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onExtend(BlockPistonExtendEvent event) {
        handle(event, event.getBlocks());
    }

    @EventHandler
    public void onRetract(BlockPistonRetractEvent event) {
        handle(event, event.getBlocks());
    }

    private void handle(BlockPistonEvent event, List<Block> blocks) {
        if (plugin.isLobby(event.getBlock().getWorld())) {
            return;
        }

        Game game = plugin.getGame(event.getBlock().getWorld());

        if (game == null) {
            return;
        }

        Ores ores = game.getGameOres();

        for (Block b : blocks) {
            if (ores.isOre(b.getLocation())) {
                event.setCancelled(true);
                Location loc = event.getBlock().getLocation();
                loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc.add(0.5, 1.1, 0.5), 25, 0.1, 0.1, 0.1, 0.01);
                return;
            }
        }
    }
}
