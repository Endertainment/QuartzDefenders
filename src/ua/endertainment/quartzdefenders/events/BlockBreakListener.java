package ua.endertainment.quartzdefenders.events;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.Game.GameState;
import ua.endertainment.quartzdefenders.game.Ores;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.events.game.OreBreakEvent;
import ua.endertainment.quartzdefenders.events.game.OreRegenerationEvent;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class BlockBreakListener implements Listener {

    private QuartzDefenders plugin;

    public BlockBreakListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (plugin.getGame(p) != null) {
            Game game = plugin.getGame(p);
            GamePlayer gpl = plugin.getGamePlayer(p);
            Ores ores = game.getGameOres();
            Block block = e.getBlock();

            if (game.isGameState(GameState.ACTIVE)) {
                Material material = e.getBlock().getType();
                if (ores.isOre(block.getLocation())) {

                    OreBreakEvent bEvent = new OreBreakEvent(block);
                    Bukkit.getPluginManager().callEvent(bEvent);
                    if (bEvent.isCancelled()) {
                        return;
                    }
                    StatsPlayer player = new StatsPlayer(p);
                    player.addBrokenOre();

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        teleportOres(block);
                        block.setType(Material.BEDROCK);
                    });

                    BukkitRunnable runnable = new BukkitRunnable() {

                        @Override
                        public void run() {
                            if (e.getBlock().getWorld() != null) {

                                OreRegenerationEvent event = new OreRegenerationEvent(block);
                                Bukkit.getPluginManager().callEvent(event);
                                if (event.isCancelled()) {
                                    return;
                                }

                                block.setType(material);
                            }
                        }
                    };
                    runnable.runTaskLater(plugin, ores.getRegenerateTime(material));
                } else if (isBelowTeamMate(gpl, block)) {
                    gpl.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), "You can't break blocks below your teammate!"));
                    e.setCancelled(true);
                }
            }

        }

    }

    private boolean isBelowTeamMate(GamePlayer pl, Block block) {
        Game game = plugin.getGame(block.getWorld());
        GameTeam team = game.getTeam(pl.getPlayer());
        if (team != null) {
            for(GamePlayer player : team.getPlayers()) {
                if(isBelow(player.getPlayer().getLocation(),block.getLocation())) {
                    if(player.equals(pl)) continue;
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isBelow(Location what, Location below) {
        Location cloned = what.clone();
        below = below.clone().add(0.5,0,0.5);
        cloned.setY(below.getY());
        if(cloned.distanceSquared(below)<0.8) {
            return true;
        }
        return false;
    }

    private void teleportOres(Block block) {
        Set<Item> items = new HashSet<>();
        Collection<Entity> list = block.getWorld().getNearbyEntities(block.getLocation().add(0.5, 0, 0.5), 0.5, 1, 0.5);
        for (Entity entity : list) {
            if (entity instanceof Item) {
                Item item = (Item) entity;
                Game game = plugin.getGame(block.getWorld());
                if (game != null && game.isAutoSmelt()) {
                    ItemStack stack = item.getItemStack().clone();
                    if (stack.getType().equals(Material.GOLD_ORE)) {
                        stack.setType(Material.GOLD_INGOT);
                        item.setItemStack(stack);
                    }
                    if (stack.getType().equals(Material.IRON_ORE)) {
                        stack.setType(Material.IRON_INGOT);
                        item.setItemStack(stack);
                    }
                }
                items.add(item);
            }
        }
        for (Item item : items) {
            item.teleport(block.getLocation().add(0.5, 1.5, 0.5));
            item.setVelocity(new Vector());
        }
    }

    public static void regenBlock(Location location) {
        Game game = null;
        QuartzDefenders plugin = QuartzDefenders.getInstance();
        for (Game gam : plugin.getGames()) {
            if (gam.getGameWorld().equals(location.getWorld())) {
                game = gam;
            }
        }
        if (game != null) {
            Ores ores = game.getGameOres();
            if (game.isGameState(GameState.ACTIVE)) {
                Material material = location.getBlock().getType();
                if (ores.isRegenerativeMaterial(material)) {
                    Block block = location.getBlock();
                    if (ores.isOre(location)) {
                        int time = ores.getRegenerateTime(material);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                block.setType(Material.BEDROCK);
                            }
                        }.runTaskLater(QuartzDefenders.getInstance(), 0);
                        BukkitRunnable runnable = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (location.getBlock().getWorld() != null) {
                                    OreRegenerationEvent event = new OreRegenerationEvent(block);
                                    Bukkit.getPluginManager().callEvent(event);
                                    if (event.isCancelled()) {
                                        return;
                                    }
                                    block.setType(material);
                                }
                            }
                        };
                        runnable.runTaskLater(plugin, time);
                    }
                }
            }
        }
    }

}
