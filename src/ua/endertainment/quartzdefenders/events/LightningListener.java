package ua.endertainment.quartzdefenders.events;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ua.coolboy.quartzdefenders.shop.Shop;
import ua.coolboy.quartzdefenders.shop.ShopEntity;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class LightningListener implements Listener {

    QuartzDefenders plugin;

    public LightningListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLightning(LightningStrikeEvent event) {
        Location loc = event.getLightning().getLocation().add(0, 20, 0);
        Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 3, 22, 3);
        for (Entity e : entities) {
            if (e instanceof Villager && e.getName().equals(Shop.name)) {
                Location locat = e.getLocation();
                e.remove();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ShopEntity.spawnShop(locat);
                    }
                }.runTaskLater(plugin, 20L);
            }
        }
    }
}
