package ua.Endertainment.QuartzDefenders.Items;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

public class QItems {

	
	public static ItemStack itemGamesChoose() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Games: &d" + QuartzDefenders.getInstance().getGames().size());
		lore.add(" ");
		return ItemUtil.newItem("&5Active Games &7(Right click)", lore, Material.COMPASS, 1);
	}
	
	public static ItemStack itemKitsChoose() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Kits menu");
		lore.add(" ");
		return ItemUtil.newItem("&2Kits &7(Right click)", lore, Material.NETHER_STAR, 1);
	}
	
	public static ItemStack itemStats() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Info about me");
		lore.add(" ");
		return ItemUtil.newItem("&6My Stats &7(Right click)", lore, Material.EXP_BOTTLE, 1);
	}
	
	public static ItemStack itemTeamChoose(Game game) {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Active teams");
		lore.add(" ");
		return ItemUtil.newItem(game.getGameName() + " " + ChatColor.BLUE +  "Teams &7(Right click)", lore, Material.COMPASS, 1);
	}
	
	public static ItemStack itemQuit() {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Quit to Lobby");
		lore.add(" ");
		return ItemUtil.newItem("&aQuit &7(Right click)", lore, Material.MAGMA_CREAM, 1);
	}
	
	public static ItemStack itemHidePlayers(boolean active) {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("&7Hide/Show players");
		lore.add(" ");
		Dye d = new Dye(active ? DyeColor.LIME : DyeColor.GRAY);		
		return ItemUtil.newItem((active ? "&aShow" : "&7Hide") + " players &7(Right click)", lore, d.toItemStack(1));
	}
	
}
