package ua.Endertainment.QuartzDefenders.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

public class GamesGUI {

	private QuartzDefenders plugin;
	
	private Inventory inventory;
	
	private String title = new ColorFormat("&5Active Games").format();
	
	public GamesGUI(QuartzDefenders plugin) {
		this.plugin = plugin;
		this.inventory = Bukkit.createInventory(null, 6*9, title);
		this.menuCorner();
		this.a();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	public void openInventory(Player p) {
		a();
		p.openInventory(inventory);
	}
	public String getName() {
		return title;
	}
	
	private void a() {
		HashMap<ItemStack, Integer> gamesIS = new HashMap<>();
		int x = 20;
		for(Game game : plugin.getGames()) {
			ArrayList<String> lore = new ArrayList<>();
			lore.add(" ");
			lore.add("&7Map: &d" + game.getColorWorldName());
			lore.add("&7Mode: &d" + game.getTeamsCount() + "&7 by &d" + game.getPlayersInTeam());
			lore.add(" ");
			lore.add("&7State: &d" + game.getGameState().toString());
			lore.add("&7Players: &d" + game.getPlayers().size());
			lore.add(" ");
			ItemStack s = ItemUtil.newItem(game.getGameName(), lore, Material.QUARTZ_ORE, 1);
			if(x == 24) x = 29;
			gamesIS.put(s, x);
			x++;
		}
		for(ItemStack s : gamesIS.keySet()) inventory.setItem(gamesIS.get(s), s);
	}
	
	private void menuCorner() {
		short z = 2, y = 6;
		int[] arg1 = {0,2,4,6,8,18,26,36,44,46,48,50,52}, arg2 = {1,3,5,7,9,17,27,35,45,47,49,51,53}; // z, y
		for(int a : arg1) inventory.setItem( a, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, z));
		for(int b : arg2) inventory.setItem( b, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, y));
	}
	
	
}

//00 01 02 03 04 05 06 07 08
//09 10 11 12 13 14 15 16 17
//18 19 20 21 22 23 24 25 26
//27 28 29 30 31 32 33 34 35
//36 37 38 39 40 41 42 43 44
//45 46 47 48 49 50 51 52 53
