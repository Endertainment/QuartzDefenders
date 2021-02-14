package ua.endertainment.quartzdefenders.events;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ua.coolboy.quartzdefenders.mobs.MobDraft;
import ua.coolboy.quartzdefenders.mobs.MobsFactory;
import ua.endertainment.quartzdefenders.events.game.GameStartEvent;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.Game.GameState;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Ores;

public class MobsListener implements Listener {

    private QuartzDefenders plugin;
    private Random random;
    private MobDraft alchemist, diamondDef, brute;

    public MobsListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        random = new Random();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        alchemist = MobsFactory.getDraftFor("alchemist");
        brute = MobsFactory.getDraftFor("brute");
        diamondDef = MobsFactory.getDraftFor("diamond_defender");
    }

    @EventHandler(ignoreCancelled = true)
    public void CenterBrute(BlockBreakEvent e) {
        Block b = e.getBlock();
        Game game = QuartzDefenders.getInstance().getGame(b.getWorld());
        if (game == null) {
            return;
        }
        Ores ores = game.getGameOres();
        if (game.isGameState(GameState.ACTIVE)) {
            Material material = e.getBlock().getType();
            if (material.equals(Material.ANCIENT_DEBRIS)) {
                if (ores.isOre(b.getLocation())) {
                    Location loc = b.getLocation().add(0.5, 0, 0.5);

                    brute.spawn(loc.clone().add(random.nextInt(4), 4.0D, random.nextInt(4)));
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void CenterDefender(BlockBreakEvent e) {
        Block b = e.getBlock();
        Game game = QuartzDefenders.getInstance().getGame(b.getWorld());
        if (game == null) {
            return;
        }
        if (!game.isDiamondDefenders()) {
            return;
        }
        Ores ores = game.getGameOres();
        if (game.isGameState(GameState.ACTIVE)) {
            Material material = e.getBlock().getType();
            if (material.equals(Material.DIAMOND_ORE)) {
                if (ores.isOre(b.getLocation())) {
                    Location loc = b.getLocation().add(0.5, 0, 0.5);

                    diamondDef.spawn(loc.clone().add(random.nextInt(4), 4.0D, random.nextInt(4)));
                    //diamondDef.spawn(loc.clone().add(random.nextInt(4), 4.0D, random.nextInt(4)));
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

                    if (countMobs(nearbyEntities, EntityType.SKELETON) >= 7) {
                        return;
                    }

                    if (countMobs(nearbyEntities, EntityType.PLAYER) > 0) {
                        Location testLoc = loc.clone().add(randomInRadius(rad), 0, randomInRadius(rad));

                        int tries = 0;
                        while (!canSpawn(testLoc)) {
                            if(tries > 10) {
                                return;
                            }
                            testLoc = loc.clone();
                            testLoc.add(randomInRadius(rad), 0, randomInRadius(rad));
                        }
                        testLoc.add(0.5, 0, 0.5);
                        if (loc.getWorld().getHighestBlockYAt(testLoc) > 0) {
                            testLoc.setY(testLoc.getWorld().getHighestBlockAt(testLoc).getLocation().getBlockY() + 1);
                            alchemist.spawn(testLoc);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, game.getAlchemistDelay() * 20);
    }

    private Integer randomInRadius(int rad) {
        int i;
        i = random.nextInt(rad * 2);

        return rad - i;
    }

    private Integer countMobs(Collection<Entity> col, EntityType type) {
        int count = 0;
        for (Entity e : col) {
            if (e.getType().equals(type)) {
                if (type.equals(EntityType.PLAYER) && ((Player) e).getGameMode().equals(GameMode.SPECTATOR)) {
                    continue;
                }
                count++;
            }
        }
        return count;
    }

    private boolean canSpawn(Location loc) {
        Block block = loc.getWorld().getHighestBlockAt(loc);
        return !(block.getY() == 0
                || block.getType() == Material.COBWEB
                || block.getRelative(BlockFace.DOWN).getType().equals(Material.MAGMA_BLOCK));
    }
}
