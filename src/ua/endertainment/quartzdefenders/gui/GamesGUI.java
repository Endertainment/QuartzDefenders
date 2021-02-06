package ua.endertainment.quartzdefenders.gui;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.ItemUtil;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class GamesGUI {

	private QuartzDefenders plugin;
	
	private Inventory inventory;
	
	private String title;
	
	public GamesGUI(QuartzDefenders plugin) {
		this.plugin = plugin;
		this.title = new ColorFormat(Language.getString("GUI.games.name")).format();
		this.inventory = Bukkit.createInventory(new QuartzInventoryHolder(), 6*9, title);
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
			if(x > 33) break;
			ArrayList<String> lore = new ArrayList<>();
			lore.add(" ");
			lore.add(Language.getRawString("GUI.games.game", new Replacer("{0}", game.getGameName())));
			lore.add(Language.getRawString("GUI.games.mode", new Replacer("{0}", game.getTeamsCount() + ""), new Replacer("{1}", game.getPlayersInTeam() + "")));
			lore.add(" ");
			lore.add(Language.getRawString("GUI.games.state", new Replacer("{0}", game.getGameState().toString())));
			lore.add(Language.getRawString("GUI.games.players", new Replacer("{0}", game.getPlayers().size() + "")));
			lore.add(" ");
			ItemStack s = ItemUtil.newItem(game.getColorWorldName(), lore, Material.NETHER_QUARTZ_ORE, 1);
			if(x == 25) x = 29;
			gamesIS.put(s, x);
			x++;
		}
		for(ItemStack s : gamesIS.keySet()) inventory.setItem(gamesIS.get(s), s);
	}
	
	private void menuCorner() {
		int[] arg1 = {0,2,4,6,8,18,26,36,44,46,48,50,52}, arg2 = {1,3,5,7,9,17,27,35,45,47,49,51,53}; // z, y
		for(int a : arg1) inventory.setItem( a, ItemUtil.newItem(" ", Material.MAGENTA_STAINED_GLASS_PANE, 1));
		for(int b : arg2) inventory.setItem( b, ItemUtil.newItem(" ", Material.PINK_STAINED_GLASS_PANE, 1));
	}
	
	
}

//00 01 02 03 04 05 06 07 08
//09 10 11 12 13 14 15 16 17
//18 19 20 21 22 23 24 25 26
//27 28 29 30 31 32 33 34 35
//36 37 38 39 40 41 42 43 44
//45 46 47 48 49 50 51 52 53
