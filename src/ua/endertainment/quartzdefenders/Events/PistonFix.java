package ua.endertainment.quartzdefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class PistonFix implements Listener {
    
    QuartzDefenders plugin;
    
    public PistonFix(QuartzDefenders plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    
    public void onExtend(BlockPistonExtendEvent event) {
        event.setCancelled(true);
    }
    
    public void onRetract(BlockPistonRetractEvent event) {
        event.setCancelled(true);
    }
        /*if (event.getBlock().getLocation().getWorld() == plugin.getLobby().getWorld()) {
            return;
        }
        
        Game game = null;
        
        for (Game g : plugin.getGames()) {
            if (event.getBlock().getLocation().getWorld() == g.getGameWorld()) {
                game = g;
            }
            
        }
        
        if (game == null) {
            return;
        }
        
        List<Block> blocks = event.getBlocks();
        
        for (Block b : blocks) {
            if (game.getRegenerativeBlocks().containsKey(b.getType())) {
                
                Set<Location> locs = game.getRegenerativeBlocks().get(b.getType());
                for (Location loc : locs) {
                    if (b.getLocation().equals(loc)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        
    }*/
}

