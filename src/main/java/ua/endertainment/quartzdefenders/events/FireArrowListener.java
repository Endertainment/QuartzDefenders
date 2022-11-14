package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class FireArrowListener implements Listener {

    public FireArrowListener(QuartzDefenders plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (event.getHitBlock() == null || !(event.getEntity() instanceof Arrow)) {
            return;
        }

        Arrow arrow = (Arrow) event.getEntity();
        if (arrow.getFireTicks() > 0) {
            Block block = event.getHitBlock();
            Block fire = block.getRelative(event.getHitBlockFace());
            if (!fire.isEmpty()) {
                fire = arrow.getLocation().getBlock();
                if (!fire.isEmpty()) {
                    return;
                }
            }
            fire.setType(Material.FIRE);
            Fire f = (Fire) fire.getBlockData();
            BlockFace oppositeFace = event.getHitBlockFace().getOppositeFace();
            if(oppositeFace != BlockFace.DOWN) f.setFace(oppositeFace, true);
            fire.setBlockData(f);
        }
    }
}
