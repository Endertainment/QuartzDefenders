package ua.Coolboy.QuartzDefenders.Mobs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.ItemStack;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

public abstract class Mobs {

    public static void middDef(WitherSkeleton skeleton) {
        skeleton.setCustomName(ChatColor.DARK_PURPLE + "Дух Алхіміка");
        skeleton.setCustomNameVisible(true);
        skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(70);
        skeleton.setHealth(70);
        skeleton.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(3);
        skeleton.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
    }

    public static void soulDef(Skeleton s) {
        ItemStack coin = new ItemStack(Material.DOUBLE_PLANT, 4);
        ItemUtil.setMeta(coin, "Монета Алхіміка", Arrays.asList("Особлива валюта Алхіміків"));
        s.setCustomName(ChatColor.AQUA + "Дух Алхіміка");
        s.setCustomNameVisible(true);
        s.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(55);
        s.setHealth(55);
        s.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(3);
        s.setLastDamage(5);

        s.getEquipment().setHelmet(new ItemStack(Material.GLASS));
        s.getEquipment().setHelmetDropChance(0.0F);
        s.getEquipment().setChestplate(new ItemStack(new ItemStack(Material.ELYTRA)));
        s.getEquipment().setChestplateDropChance(0.1F);
        s.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        s.getEquipment().setLeggingsDropChance(0.3F);
        s.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        s.getEquipment().setBootsDropChance(0.2F);
        s.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
        s.getEquipment().setItemInMainHandDropChance(0.2F);
        s.getEquipment().setItemInOffHand(coin);
        s.getEquipment().setItemInOffHandDropChance(0.5F);
    }

    public static Integer randomInRadius(int rad) {
        int i;
        Random random = new Random();
        i = random.nextInt(rad);
        if (random.nextBoolean()) {
            i *= -1;
        }
        return i;
    }

    public static Integer countMobs(Collection<Entity> col, Class clazz) {
        int count = 0;
        for (Entity e : col) {
            if (e.getClass().isInstance(clazz)) {
                count++;
            }
        }
        return count;
    }

    public static boolean canSpawn(Location loc) {
        Block block = loc.getWorld().getHighestBlockAt(loc);
        return !(block.getType() == null || block.getType() == Material.WEB);
    }
}
