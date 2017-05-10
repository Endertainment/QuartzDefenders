package ua.Endertainment.QuartzDefenders.Utils;

import java.util.ArrayList;

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
		if(lore == null){
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
		if(lore == null){
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
		if(lore == null){
			ItemStack item = new ItemStack(material, amount, (short)type);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(meta);
			return item;
		} 
		lore = new ColorFormat(lore).getFormatedList();
		ItemStack item = new ItemStack(material, amount, (short)type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}
	
}