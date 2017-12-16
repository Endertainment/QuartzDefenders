package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.Cuboid;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class BlockPlaceListener implements Listener {

    private QuartzDefenders plugin;

    public BlockPlaceListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (plugin.getGame(p) == null) {
            return;
        }

        Game game = plugin.getGame(p);

        if (!game.isPlayerInTeam(plugin.getGamePlayer(p))) {
            return;
        }

        StatsPlayer sp = new StatsPlayer(p);
        sp.addPlacedBlock();

        for (Cuboid c : game.getCuboids()) {
            if (!c.canBuild(plugin.getGamePlayer(p), e.getBlock().getLocation())) {
                e.setCancelled(true);
                p.sendMessage(LoggerUtil.gameMessage("Game", "&cYou can not place block here"));
                return;
            }
        }

        if (!game.getBuildCuboid().contains(e.getBlock().getLocation())) {
            p.sendMessage(LoggerUtil.gameMessage("Game", "&cYou can not place block here"));
            e.setCancelled(true);
            return;
        }
        
        if(e.getBlock().getType().equals(Material.PISTON_BASE) || e.getBlock().getType().equals(Material.PISTON_STICKY_BASE)) {
            e.setCancelled(true);
        }

    }

}
