package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class TransformListener implements Listener {

    QuartzDefenders plugin;

    public TransformListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    
    //Unlikely scenario, but best to be prepared
    /*@EventHandler
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
    }*/
    
    @EventHandler
    public void onTransform(EntityTransformEvent event) {
        if(event.getTransformReason() == EntityTransformEvent.TransformReason.LIGHTNING
                && event.getEntity().getType() == EntityType.VILLAGER) {
            event.setCancelled(true);
        }
        
        if(event.getEntity().getType() == EntityType.PIGLIN_BRUTE) {
            event.setCancelled(true);
            PiglinBrute brute = (PiglinBrute) event.getEntity();
            brute.setImmuneToZombification(true);
        }
    }
}
