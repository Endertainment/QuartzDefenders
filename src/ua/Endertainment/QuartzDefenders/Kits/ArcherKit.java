package ua.Endertainment.QuartzDefenders.Kits;

import java.util.AbstractMap.SimpleEntry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class ArcherKit extends Kit{

	public ArcherKit() {
		super("Archer", "&4Archer", 200, 4, "&7Bow", "&7Arrow x64");

		addItem(Material.BOW, 1, 0, new SimpleEntry<>(Enchantment.ARROW_INFINITE, 1));
		addItem(Material.ARROW, 64);
	}

}
