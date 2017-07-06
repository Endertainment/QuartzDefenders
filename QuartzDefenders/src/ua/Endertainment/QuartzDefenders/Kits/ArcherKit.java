package ua.Endertainment.QuartzDefenders.Kits;

import org.bukkit.Material;

public class ArcherKit extends Kit{

	public ArcherKit() {
		super("&4Archer", 200, 4, "&7Bow", "&7Arrow x10");

		addItem(Material.BOW, 1);
		addItem(Material.ARROW, 10);
	}

}
