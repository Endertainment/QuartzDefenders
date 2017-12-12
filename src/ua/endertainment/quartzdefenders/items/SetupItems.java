package ua.endertainment.quartzdefenders.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.utils.ItemUtil;

public class SetupItems {
	
	public static ItemStack itemSetupOres() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Right click to block");
		lore.add("&7that you need to regenerate");
		lore.add(" ");
		return ItemUtil.newItem("&aSetupOres &7(Right click)", lore, Material.BLAZE_ROD, 1);
	}
	
	public static ItemStack itemSetupQuartz(GameTeam team) {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Right click to ");
		lore.add("&7set quartz of " + team.getName() + "&7 team");
		lore.add(" ");
		return ItemUtil.newItem("&aSetupQuartz " + team.getName() + "&7 (Right click)", lore, Material.QUARTZ, 1);
	}
	
	public static ItemStack itemSetupSignsK() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Right click to set sign");
		lore.add(" ");
		return ItemUtil.newItem("&aSetupSigns &7(Kills) (Right click)", lore, Material.STICK, 1);
	}
	
	public static ItemStack itemSetupSignsW() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Right click to set sign");
		lore.add(" ");
		return ItemUtil.newItem("&aSetupSigns &7(Wins) (Right click)", lore, Material.STICK, 1);
	}
}
