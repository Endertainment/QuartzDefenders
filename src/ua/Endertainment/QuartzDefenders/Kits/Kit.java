package ua.Endertainment.QuartzDefenders.Kits;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.GamePlayer;

public abstract class Kit {

	private String name;
	private int price;
	private int level;
	private ArrayList<ItemStack> items;
	
	public Kit(String name, int price, int level) {
		this.name = name;
		this.price = price;
		this.level = level;
		this.items = new ArrayList<ItemStack>();
	}
	
	public final String getName() {
		return name;
	}
	public final int getPrice() {
		return price;
	}
	public final int getLevel() {
		return level;
	}
	
	@SuppressWarnings("unchecked")
	public final ArrayList<ItemStack> getItems() {
		return (ArrayList<ItemStack>) items.clone();
	}
	
	public void apply(GamePlayer p) {
		
	}
	
	@SafeVarargs
	public final void addItem(Material material, int amount, int data, SimpleEntry<Enchantment, Integer>... ench) {
		ItemStack item = new ItemStack(material, amount, (short)data);
		for(SimpleEntry<Enchantment, Integer> entry : ench){
			item.addEnchantment(entry.getKey(), entry.getValue());
		}
		items.add(item);
	}
	public final void addItem(Material material, int amount) {
		ItemStack item = new ItemStack(material, amount);
		items.add(item);
	}
	public final void addItem(ItemStack item) {
		items.add(item);
	}
}