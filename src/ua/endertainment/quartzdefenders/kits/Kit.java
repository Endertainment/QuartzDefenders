package ua.endertainment.quartzdefenders.kits;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ua.endertainment.quartzdefenders.game.GamePlayer;

public abstract class Kit {

	private String name;
	private String displayName;
	private int price;
	private int level;
	private ArrayList<ItemStack> items;
	
	private final ArrayList<String> description;
	
	public Kit(String name, String displayName, int price, int level, String... description) {
		this.name = name;
		this.displayName = displayName;
		this.price = price;
		this.level = level;
		this.items = new ArrayList<>();
		this.description = new ArrayList<>();
		for(String s : description) {
			this.description.add(s);
		}
	}
	
	public final String getDisplayName() {
		return displayName;
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
	public ArrayList<String> getDescription() {
		return description;
	}
	@SuppressWarnings("unchecked")
	public final ArrayList<ItemStack> getItems() {
		return (ArrayList<ItemStack>) items.clone();
	}
	
	public void apply(GamePlayer gp) {
		Player p = gp.getPlayer();
		p.getInventory().clear();
			p.getInventory().addItem(items.toArray(new ItemStack[items.size()]));
	}
	
	@SafeVarargs
	public final void addItem(Material material, int amount, int data, SimpleEntry<Enchantment, Integer>... ench) {
		ItemStack item = new ItemStack(material, amount, (byte)data);
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