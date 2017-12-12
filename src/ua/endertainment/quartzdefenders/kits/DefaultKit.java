package ua.endertainment.quartzdefenders.Kits;

import org.bukkit.Material;

public class DefaultKit extends Kit{

	public DefaultKit() {
		super("Default", "&fDefault", 0, 1, 
				"&7Leather armor", "&7Wooden sword", "&7Baked potato x4");
		
		addItem(Material.WOOD_SWORD, 1);
		addItem(Material.BAKED_POTATO, 4);
		addItem(Material.LEATHER_HELMET, 1);
		addItem(Material.LEATHER_CHESTPLATE, 1);
		addItem(Material.LEATHER_LEGGINGS, 1);
		addItem(Material.LEATHER_BOOTS, 1);
	}
	
	
	
}
