package ua.Coolboy.QuartzDefenders.Shop;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class ShopEntity {

    QuartzDefenders plugin;
    Shop shop;
    Game game;

    public ShopEntity(Location loc, QuartzDefenders plugin) {
        this.plugin = plugin;
        this.shop = new Shop(plugin);
        Villager v = loc.getWorld().spawn(loc, Villager.class);
        v.setCustomName(shop.name);
        v.setCustomNameVisible(true);
        v.setCanPickupItems(false);
        v.setInvulnerable(true);
        v.setProfession(Profession.NITWIT);
        v.setSilent(true);
        v.setAI(false);
    }
    
    public void loadShops() {
    
    }
}
