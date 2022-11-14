package ua.coolboy.quartzdefenders.shop;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public abstract class ShopEntity {

    public static Villager spawnShop(Location loc) {
        Villager v = loc.getWorld().spawn(loc, Villager.class);
        v.setCustomName(Shop.name);
        v.setCustomNameVisible(true);
        v.setCanPickupItems(false);
        v.setInvulnerable(true);
        v.setProfession(Profession.NITWIT);
        v.setSilent(true);
        v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false));
        v.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
        //v.setCollidable(false); not working with arrows
        new ShopRunnable(v, loc).runTaskTimerAsynchronously(QuartzDefenders.getInstance(), 0, 40);
        return v;
    }

    
    public static void loadShops(Game game) {
        for(Location loc : game.getShopLocations()) {
            spawnShop(loc);
        }
    }
}
