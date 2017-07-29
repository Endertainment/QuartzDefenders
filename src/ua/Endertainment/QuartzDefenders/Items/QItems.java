package ua.Endertainment.QuartzDefenders.Items;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;
import ua.Endertainment.QuartzDefenders.Utils.Language;
import ua.Endertainment.QuartzDefenders.Utils.Replacer;

public class QItems {

	
	public static ItemStack itemGamesChoose() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(Language.getRawString("items.games.games", new Replacer("{0}", QuartzDefenders.getInstance().getGames().size() + "")));
		lore.add(" ");
		return ItemUtil.newItem(Language.getRawString("items.games.name"), lore, Material.COMPASS, 1);
	}
	
	public static ItemStack itemKitsChoose() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(" ");
		return ItemUtil.newItem(Language.getRawString("items.kits.name"), lore, Material.BOOK, 1);
	}
	
	public static ItemStack itemStats() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(" ");
		return ItemUtil.newItem(Language.getRawString("items.stats.name"), lore, Material.EXP_BOTTLE, 1);
	}
	
	public static ItemStack itemTeamChoose(Game game) {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(" ");
		return ItemUtil.newItem(Language.getRawString("items.teams.name"), lore, Material.COMPASS, 1);
	}
	
	public static ItemStack itemQuit() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(" ");
		return ItemUtil.newItem(Language.getRawString("items.quit.name"), lore, Material.MAGMA_CREAM, 1);
	}
	
	public static ItemStack itemHidePlayers(boolean active) {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(" ");
		Dye d = new Dye(active ? DyeColor.GRAY : DyeColor.LIME);	
		String x = active ? Language.getRawString("items.hide_show.show") : Language.getRawString("items.hide_show.show");
		return ItemUtil.newItem(Language.getString("items.hide_show", new Replacer("{0}", x)), lore, d.toItemStack(1));
	}
	
	public static ItemStack itemLobbyShop() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(" ");
		return ItemUtil.newItem(Language.getRawString("items.shop.name"), lore, Material.EMERALD, 1);		
	}
	
}
