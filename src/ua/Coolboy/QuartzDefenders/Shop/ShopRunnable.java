package ua.Coolboy.QuartzDefenders.Shop;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class ShopRunnable extends BukkitRunnable {
    
    LivingEntity villager;
    Location location;

    public ShopRunnable(LivingEntity villager, Location location) {
        this.villager = villager;
        this.location = location;
    }

    @Override
    public void run() {
        if(villager.getLocation().distanceSquared(location)>2) {
            villager.teleport(location);
        }
    }

}
