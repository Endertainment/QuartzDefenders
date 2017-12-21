package ua.coolboy.quartzdefenders.mobs;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ua.endertainment.quartzdefenders.events.game.GameStartEvent;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.Game.GameState;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Ores;

public class MobsListener implements Listener {

    private QuartzDefenders plugin;

    public MobsListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void CenterMob(BlockBreakEvent e) {
        Block b = e.getBlock();
        Game game = QuartzDefenders.getInstance().getGame(b.getWorld());
        if (game == null) {
            return;
        }
        Ores ores = game.getGameOres();
        if (game.isGameState(GameState.ACTIVE)) {
            Material material = e.getBlock().getType();
            if (material.equals(Material.DIAMOND_ORE)) {
                if (ores.isRegenetiveOre(b.getLocation())) {
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
        }
    }

    @EventHandler
    public void alchemistrySoul(GameStartEvent event) {
        Game game = event.getGame();
        Map<Location, Integer> locat = event.getGame().getAlchemicsLocations();
        if (locat.isEmpty()) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (game.getGameState().equals(GameState.ENDING)) {
                    this.cancel();
                }

                for (Map.Entry<Location, Integer> entry : locat.entrySet()) {
                    int rad = entry.getValue();
                    Location loc = entry.getKey();
                    Collection<Entity> nearbyEntities = loc.getWorld().getNearbyEntities(loc, rad, rad, rad);

                    if (Mobs.countMobs(nearbyEntities, EntityType.SKELETON) >= 7) {
                        return;
                    }

                    if (Mobs.countMobs(nearbyEntities, EntityType.PLAYER) != 0) {
                        Location testLoc = loc.clone().add(Mobs.randomInRadius(rad), 0, Mobs.randomInRadius(rad));

                        while (!Mobs.canSpawn(testLoc)) {
                            testLoc = loc.clone();
                            testLoc.add(Mobs.randomInRadius(rad), 0, Mobs.randomInRadius(rad));
                        }
                        testLoc.add(0.5, 0, 0.5);
                        if (loc.getWorld().getHighestBlockYAt(testLoc) > 0) {
                            testLoc.setY(testLoc.getWorld().getHighestBlockAt(testLoc).getLocation().getBlockY() + 1);
                            Skeleton soul = (Skeleton) testLoc.getWorld().spawnEntity(testLoc, EntityType.SKELETON);
                            Mobs.soulDef(soul);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 120);
    }
}
