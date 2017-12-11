package ua.endertainment.quartzdefenders.items;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.ItemUtil;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class QItems {

	public static ArrayList<ItemStack> getAllTechItems() {
		ArrayList<ItemStack> l = new ArrayList<>();
		l.add(itemGamesChoose());
		l.add(itemKitsChoose());
		l.add(itemStats());
		l.add(itemTeamChoose());
		l.add(itemQuit());
		l.add(itemLobbyShop());
		l.add(itemHidePlayers(true));
		l.add(itemHidePlayers(false));
		l.add(itemAchievements());
		l.add(SetupItems.itemSetupOres());
		l.add(SetupItems.itemSetupSignsK());
		l.add(SetupItems.itemSetupSignsW());
		return l;
	}
	
	public static ItemStack itemGamesChoose() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(Language.getRawString("items.games.games", new Replacer("{0}", QuartzDefenders.getInstance().getGames().size() + "")));
		lore.add(" ");
		return ItemUtil.newItem(Language.getRawString("items.games.name"), lore, Material.COMPASS, 1);
	}
	
	public static ItemStack itemKitsChoose() {		
		return ItemUtil.newItem(Language.getRawString("items.kits.name"), null, Material.BOOK, 1);
	}
	
	public static ItemStack itemStats() {		
		return ItemUtil.newItem(Language.getRawString("items.stats.name"), null, Material.EXP_BOTTLE, 1);
	}
	
	public static ItemStack itemTeamChoose() {		
		return ItemUtil.newItem(Language.getRawString("items.teams.name"), null, Material.COMPASS, 1);
	}
	
	public static ItemStack itemQuit() {
		return ItemUtil.newItem(Language.getRawString("items.quit.name"), null, Material.MAGMA_CREAM, 1);
	}
	
	public static ItemStack itemHidePlayers(boolean active) {
		Dye d = new Dye(active ? DyeColor.GRAY : DyeColor.LIME);	
		String x = active ? Language.getRawString("items.hide_show.show") : Language.getRawString("items.hide_show.hide");
		return ItemUtil.newItem(Language.getString("items.hide_show.name", new Replacer("{0}", x)), null, d.toItemStack(1));
	}
	
	public static ItemStack itemLobbyShop() {
		return ItemUtil.newItem(Language.getRawString("items.shop.name"), null, Material.EMERALD, 1);		
	}
	
	public static ItemStack itemAchievements() {
		return ItemUtil.newItem(Language.getString("items.achievements.name"), null, Material.BOOK, 1);
	}
	
}
