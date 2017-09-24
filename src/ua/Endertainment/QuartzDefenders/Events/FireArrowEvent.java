package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class FireArrowEvent implements Listener {

    public FireArrowEvent(QuartzDefenders plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (event.getHitBlock() == null) {
            return;
        }
        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow) event.getEntity();
        if (arrow.getFireTicks() > 0) {
            Block block = event.getHitBlock();
            Block fire = block.getRelative(getFacing(arrow.getLocation()).getOppositeFace());
            if(fire.isEmpty()) fire.setType(Material.FIRE);
        }
    }
    
    private BlockFace getFacing(Location loc) {
        float pitch = loc.getPitch();
        for (; pitch < 0; pitch += 360F);
        pitch %= 360F;
        int pitchdir = Math.round(pitch / 90F) % 4;

        switch (pitchdir) {
            case 1:
                return BlockFace.UP;
            case 3:
                return BlockFace.DOWN;
            default:
                break;
        }

        float yaw = loc.getYaw();
        for (; yaw < 0; yaw += 360F);
        yaw %= 360F;
        int yawdir = Math.round(yaw / 90F) % 4;
        switch (yawdir) {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.EAST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.WEST;
        }
        return BlockFace.SELF;
    }
}
