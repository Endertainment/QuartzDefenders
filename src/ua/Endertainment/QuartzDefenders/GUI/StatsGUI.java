package ua.Endertainment.QuartzDefenders.GUI;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

public class StatsGUI {

	private Inventory inventory;
	
	private String title = new ColorFormat("&6My Stats").format();
	
	public StatsGUI(QuartzDefenders plugin) {
		this.inventory = Bukkit.createInventory(null, 6*9, title);
		this.menuCorner();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	public void openInventory(Player p) {
		a(new StatsPlayer(p));
		p.openInventory(inventory);
	}
	
	private void a(StatsPlayer p) {
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(" ");
		lore1.add("&7My kills: &6" + p.getKills());
		lore1.add("&7My KDR: &6" + p.getKdr());
		lore1.add(" ");
		ItemStack kills = ItemUtil.newItem("&6My Kills", lore1, Material.DIAMOND_SWORD, 1);
		
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(" ");
		lore2.add("&7My level: &6" + p.getLevel());
		lore2.add("&7Next level &6" + p.getNextLevel() + " &7(&6" + p.getPoints() + "&7/&6" + p.getNextLevelPoints() + "&7)");
		lore2.add(" ");
		ItemStack info = ItemUtil.newItem("&6My Level", lore2, Material.BOOK_AND_QUILL, 1);
		
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(" ");
		lore3.add("&7My points: &6" + p.getPoints());
		lore3.add(" ");
		ItemStack points = ItemUtil.newItem("&6My Points", lore3, Material.EXP_BOTTLE, 1);
		
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(" ");
		lore4.add("&7My wins: &6" + p.getWins());
		lore4.add("&7Games played: &6" + p.getPlayedGames());
		lore4.add("&7Win rate: &6" + p.getWinRate());
		lore4.add(" ");
		ItemStack wins = ItemUtil.newItem("&6My Wins", lore4, Material.NETHER_STAR, 1);
		
		ArrayList<String> lore5 = new ArrayList<String>();
		lore5.add(" ");
		lore5.add("&7My deaths: &6" + p.getDeaths());
		lore5.add(" ");
		ItemStack deaths = ItemUtil.newItem("&6My Deaths", lore5, Material.DEAD_BUSH, 1);
		
		ArrayList<String> lore6 = new ArrayList<String>();
		lore6.add(" ");
		lore6.add("&7My coins: &6" + p.getCoins());
		lore6.add(" ");
		ItemStack coins = ItemUtil.newItem("&6My Coins", lore6, Material.DOUBLE_PLANT, 1);
		
		inventory.setItem(20, kills);
		inventory.setItem(22, info);
		inventory.setItem(24, deaths);
		inventory.setItem(30, points);
		inventory.setItem(32, wins);
		inventory.setItem(40, coins);
	}
	
	private void menuCorner() {
		short z = 1, y = 4;
		int[] arg1 = {0,2,4,6,8,18,26,36,44,46,48,50,52}, arg2 = {1,3,5,7,9,17,27,35,45,47,49,51,53}; // z, y
		for(int a : arg1) inventory.setItem( a, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, z));
		for(int b : arg2) inventory.setItem( b, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, y));
	}

}
