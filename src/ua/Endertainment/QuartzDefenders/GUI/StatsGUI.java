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
import ua.Endertainment.QuartzDefenders.Utils.Language;
import ua.Endertainment.QuartzDefenders.Utils.Replacer;

public class StatsGUI {

	private Inventory inventory;
	private Player player;
	private String title;
	
	public StatsGUI(Player player) {
		this.player = player;
		this.title = new ColorFormat(Language.getString("GUI.stats.name", new Replacer("{0}", player.getDisplayName()))).format();
		this.inventory = Bukkit.createInventory(null, 6*9, title);
		this.menuCorner();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	public void openInventory() {
		a(new StatsPlayer(player));
		player.openInventory(inventory);
	}
	public void openInventory(Player p) {
		a(new StatsPlayer(player));
		p.openInventory(inventory);
	}
	
	private void a(StatsPlayer p) {
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(" ");
		lore1.add(Language.getRawString("GUI.stats.item_kills.kills", new Replacer("{0}", p.getKills() + "")));
		lore1.add(Language.getRawString("GUI.stats.item_kills.kd", new Replacer("{0}", p.getKdr() + "")));
		lore1.add(Language.getRawString("GUI.stats.item_kills.top_kills", new Replacer("{0}", QuartzDefenders.getInstance().getTopManager().getPlayerPositionK(p.getPlayer()) + "")));
		lore1.add(" ");
		ItemStack kills = ItemUtil.newItem(Language.getRawString("GUI.stats.item_kills.name"), lore1, Material.DIAMOND_SWORD, 1);
		
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(" ");
		lore2.add(Language.getRawString("GUI.stats.item_level.level", new Replacer("{0}", p.getLevel() + "")));
		lore2.add(Language.getRawString("GUI.stats.item_level.next_level", new Replacer("{0}", p.getNextLevel() + ""), new Replacer("{1}", p.getPoints() + ""), new Replacer("{2}", p.getNextLevelPoints() + "")));
		lore2.add(" ");
		ItemStack info = ItemUtil.newItem(Language.getRawString("GUI.stats.item_level.name"), lore2, Material.BOOK_AND_QUILL, 1);
		
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(" ");
		lore3.add(Language.getRawString("GUI.stats.item_points.points", new Replacer("{0}", p.getPoints() + "")));
		lore3.add(" ");
		ItemStack points = ItemUtil.newItem(Language.getRawString("GUI.stats.item_points.name"), lore3, Material.EXP_BOTTLE, 1);
		
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(" ");
		lore4.add(Language.getRawString("GUI.stats.item_wins.wins", new Replacer("{0}", p.getWins() + "")));
		lore4.add(Language.getRawString("GUI.stats.item_wins.games_played", new Replacer("{0}", p.getPlayedGames() + "")));
		lore4.add(Language.getRawString("GUI.stats.item_wins.win_rate", new Replacer("{0}", p.getWinRate() + "")));
		lore4.add(" ");
		ItemStack wins = ItemUtil.newItem(Language.getRawString("GUI.stats.item_wins.name"), lore4, Material.NETHER_STAR, 1);
		
		ArrayList<String> lore5 = new ArrayList<String>();
		lore5.add(" ");
		lore5.add(Language.getRawString("GUI.stats.item_deaths.deaths", new Replacer("{0}", p.getDeaths() + "")));
		lore5.add(" ");
		ItemStack deaths = ItemUtil.newItem(Language.getRawString("GUI.stats.item_deaths.name"), lore5, Material.DEAD_BUSH, 1);
		
		ArrayList<String> lore6 = new ArrayList<String>();
		lore6.add(" ");
		lore6.add(Language.getRawString("GUI.stats.item_coins.coins", new Replacer("{0}", p.getCoins() + "")));
		lore6.add(" ");
		ItemStack coins = ItemUtil.newItem(Language.getRawString("GUI.stats.item_coins.name"), lore6, Material.DOUBLE_PLANT, 1);
		
		ArrayList<String> lore7 = new ArrayList<String>();
		lore7.add(" ");
		lore7.add(Language.getRawString("GUI.stats.item_another.broken_quartz", new Replacer("{0}", p.getBrokenQuartz() + "")));
		lore7.add(Language.getRawString("GUI.stats.item_another.broken_ores", new Replacer("{0}", p.getBrokenOres() + "")));
		lore7.add(Language.getRawString("GUI.stats.item_another.placed_blocks", new Replacer("{0}", p.getPlacedBlocks() + "")));
		lore7.add(" ");
		ItemStack another = ItemUtil.newItem(Language.getRawString("GUI.stats.item_another.name"), lore7, Material.QUARTZ_BLOCK, 1);
		
		inventory.setItem(20, kills);
		inventory.setItem(22, info);
		inventory.setItem(24, deaths);
		inventory.setItem(30, points);
		inventory.setItem(31, another);
		inventory.setItem(32, wins);
		inventory.setItem(40, coins);
	}
	
	private void menuCorner() {
		short z = Short.parseShort(Language.getString("GUI.stats.glass_id_1")),
				  y = Short.parseShort(Language.getString("GUI.stats.glass_id_2"));
		int[] arg1 = {0,2,4,6,8,18,26,36,44,46,48,50,52}, arg2 = {1,3,5,7,9,17,27,35,45,47,49,51,53}; // z, y
		for(int a : arg1) inventory.setItem( a, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, z));
		for(int b : arg2) inventory.setItem( b, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, y));
	}

}
