package ua.Coolboy.QuartzDefenders.Shop;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ua.Endertainment.QuartzDefenders.Game;

public abstract class ShopEntity {

    private static Villager spawnShop(Location loc) {
        Villager v;
        v = loc.getWorld().spawn(loc, Villager.class);
        v.setCustomName(Shop.name);
        v.setCustomNameVisible(true);
        v.setCanPickupItems(false);
        v.setInvulnerable(true);
        v.setProfession(Profession.NITWIT);
        v.setSilent(true);
        v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false));
        v.setCollidable(false);
        return v;
    }

    public static void loadShops(Game game) {
        for (Location loc : game.getShopLocations()) {
            spawnShop(loc);
        }
    }
}
