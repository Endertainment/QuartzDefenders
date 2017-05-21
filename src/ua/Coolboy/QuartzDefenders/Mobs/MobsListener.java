package ua.Coolboy.QuartzDefenders.Mobs;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ua.Endertainment.QuartzDefenders.Events.Game.GameStateChangeEvent;
import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.Game.GameState;
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
            Location loc = b.getLocation().add(0.5, 0, 0.5);
            Random random = new Random();
            WitherSkeleton w = (WitherSkeleton) b.getWorld()
                    .spawn(loc.add(random.nextInt(4), 4.0D, random.nextInt(4)), WitherSkeleton.class);
            Mobs.middDef(w);
            WitherSkeleton w2 = (WitherSkeleton) b.getWorld()
                    .spawn(loc.add(random.nextInt(4), 4.0D, random.nextInt(4)), WitherSkeleton.class);
            Mobs.middDef(w2);
        }
    }
    
    @EventHandler
    public void alchemistrySoul(GameStateChangeEvent event) {
        if (event.getGameStateTo().equals(GameState.ACTIVE)) {
            Game game = event.getGame();
            Map<Integer, Location> locat = event.getGame().getAlchemicsLocations();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (game.getGameState().equals(GameState.ENDING)) this.cancel();
                    for (Map.Entry<Integer, Location> entry : locat.entrySet()) {
                        int rad = entry.getKey();
                        Location loc = entry.getValue();
                        Collection<Entity> nearbyEntities = loc.getWorld().getNearbyEntities(loc, rad, rad, rad);
                        if (Mobs.countMobs(nearbyEntities, Skeleton.class) > 12) return;
                        if (Mobs.countMobs(nearbyEntities, Player.class) != 0) {
                            int randomX = Mobs.randomInRadius(rad);
                            int randomZ = Mobs.randomInRadius(rad);
                            Location spawnLoc = new Location(loc.getWorld(), randomX, loc.getBlockY(), randomZ);
                            Location testLoc =spawnLoc.clone();
                            while (!Mobs.canSpawn(testLoc)) {
                                testLoc = spawnLoc.clone();
                                testLoc.add(Mobs.randomInRadius(rad),0,Mobs.randomInRadius(rad));
                            }
                            if (loc.getWorld().getHighestBlockAt(randomX, randomZ) != null) {
                                testLoc.setY(testLoc.getWorld().getHighestBlockAt(randomX, randomZ).getLocation().getBlockY());
                                Skeleton soul = (Skeleton) testLoc.getWorld().spawnEntity(testLoc, EntityType.SKELETON);
                                Mobs.soulDef(soul);
                                Skeleton soul2 = (Skeleton) testLoc.getWorld().spawnEntity(testLoc, EntityType.SKELETON);
                                Mobs.soulDef(soul2);
                            }
                        }
                        
                    }
                }
            }.runTaskTimer(plugin, 0, 240);
        }
    }
}
