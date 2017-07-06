package ua.Endertainment.QuartzDefenders.Kits;

import org.bukkit.Material;

public class DefaultKit extends Kit{

	public DefaultKit() {
		super("Default", 100, 3, 
				"&7Leather armor", "&7Wooden sword", "&7Baked potato x4");		
		
		addItem(Material.WOOD_SWORD, 1);
		addItem(Material.BAKED_POTATO, 4);
		addItem(Material.LEATHER_HELMET, 1);
		addItem(Material.LEATHER_CHESTPLATE, 1);
		addItem(Material.LEATHER_LEGGINGS, 1);
		addItem(Material.LEATHER_BOOTS, 1);
	}
	
	
	
}