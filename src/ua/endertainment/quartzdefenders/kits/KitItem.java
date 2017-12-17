package ua.endertainment.quartzdefenders.kits;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public interface KitItem {

	public abstract String getKitID();
	
	public abstract String getItemID();
		
	public abstract ItemStack getItem();
		
}
