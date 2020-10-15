package ua.endertainment.quartzdefenders.kits;

import org.bukkit.inventory.ItemStack;

public interface KitItem {

	public abstract String getKitID();
	
	public abstract String getItemID();
		
	public abstract ItemStack getItem();
		
}
