package ua.Endertainment.QuartzDefenders.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

public class SetupItems {
	
	public static ItemStack itemSetupOres() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Right click to block");
		lore.add("that you need to regenerate");
		lore.add(" ");
		return ItemUtil.newItem("&aSetupOres &7(Right click)", lore, Material.BLAZE_ROD, 1);
	}
	
}
