package ua.Endertainment.QuartzDefenders.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {

    public static ItemStack newItem(String name, Material material, int amount, int type) {
        return newItem(name, null, material, amount, type);
    }

    public static ItemStack newItem(String name, ArrayList<String> lore, ItemStack item) {
        name = new ColorFormat(name).format();
        if (lore == null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
            return item;
        }
        lore = new ColorFormat(lore).getFormatedList();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack newItem(String name, ArrayList<String> lore, Material material, int amount) {
        name = new ColorFormat(name).format();
        if (lore == null) {
            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
            return item;
        }
        lore = new ColorFormat(lore).getFormatedList();
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack newItem(String name, ArrayList<String> lore, Material material, int amount, int type) {
        name = new ColorFormat(name).format();
        if (lore == null) {
            ItemStack item = new ItemStack(material, amount, (short) type);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
            return item;
        }
        lore = new ColorFormat(lore).getFormatedList();
        ItemStack item = new ItemStack(material, amount, (short) type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setName(ItemStack stack, String rename) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(rename);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack setMeta(ItemStack stack, String rename, List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(rename);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack setMeta(ItemStack stack, String rename, String lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(rename);
        meta.setLore(Arrays.asList(lore));
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack setLore(ItemStack stack, List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    //inserts a formatting character between every character, making it invisible when displayed as lore
    public static String encodeHiddenLore(String lore) {
        for (int i = 0; i < lore.length(); i += 2) {
            lore = lore.substring(0, i) + ChatColor.COLOR_CHAR + lore.substring(i, lore.length());
        }
        return lore;
    }

    //removes the formatting characters
    public static String decodeHiddenLore(String lore) {
        while (lore.contains(ChatColor.COLOR_CHAR + "")) {
            lore = lore.substring(0, lore.indexOf(ChatColor.COLOR_CHAR)) + lore.substring(lore.indexOf(ChatColor.COLOR_CHAR) + 1, lore.length());
        }
        return lore;
    }
    
    public static ItemStack hideAll(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_DESTROYS,
        ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON,
        ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        stack.setItemMeta(meta);
        return stack;
    }
}
