package ua.Coolboy.QuartzDefenders.Mobs;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class MobsListener implements Listener {

    QuartzDefenders plugin;

    public MobsListener(QuartzDefenders plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void CenterMob(BlockBreakEvent e) {
        Block b = e.getBlock();
        Block barrier = b.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if ((e.getBlock().getType().equals(Material.DIAMOND_ORE))
                && (barrier.getType() == Material.BEDROCK)) {
            Location loc = b.getLocation();
            Random random = new Random();
            loc.add(random.nextInt(4), 4.0D, random.nextInt(4));
            WitherSkeleton w = (WitherSkeleton)b.getWorld().spawn(loc, WitherSkeleton.class);
            Mobs.middDef(w);
            WitherSkeleton w2 = (WitherSkeleton)b.getWorld().spawn(loc, WitherSkeleton.class);
            Mobs.middDef(w2);
        }
    }
}
