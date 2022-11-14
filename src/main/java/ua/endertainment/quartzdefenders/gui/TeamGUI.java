package ua.endertainment.quartzdefenders.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.DataAdapter;
import ua.endertainment.quartzdefenders.utils.ItemUtil;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class TeamGUI {
	
	private Inventory inventory;
	
	private Game game;
	
	private Player p;
	
	private String title;
	
	public TeamGUI(Game game, Player p) {
		this.game = game;
		this.p = p;
		this.title = new ColorFormat(Language.getString("GUI.teams.name")).format();
		this.inventory = Bukkit.createInventory(new QuartzInventoryHolder(), 6*9, title);
		this.menuCorner();
		this.a();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	public void openInventory() {
		p.openInventory(inventory);
	}

	
	private void menuCorner() {
		int[] arg1 = {0,2,4,6,8,18,26,36,44,46,48,50,52}, arg2 = {1,3,5,7,9,17,27,35,45,47,49,51,53}; // z, y
		for(int a : arg1) inventory.setItem( a, ItemUtil.newItem(" ", Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
		for(int b : arg2) inventory.setItem( b, ItemUtil.newItem(" ", Material.BLUE_STAINED_GLASS_PANE, 1));
	}
	
	private void a() {
		HashMap<ItemStack, Integer> teamsIS = new HashMap<>();
		int x = 20;
		for(GameTeam team : game.getTeams().values()) {
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(" ");
			lore.add(Language.getRawString("GUI.teams.players", new Replacer("{c}", team.getColor() + ""), new Replacer("{0}", team.getPlayersSize() + ""), new Replacer("{1}", team.intPlayersInTeam() + "")));
			lore.add(" ");
			ItemStack itemWool = ItemUtil.newItem(
                                Language.getRawString("GUI.teams.item_name", new Replacer("{c}", team.getColor() + ""), new Replacer("{0}", team.getName())),
                                lore,
                                DataAdapter.getForColor(team.getColor(), Tag.WOOL));
			if(x == 25) x = 29;
			teamsIS.put(itemWool, x);
			x++;
		}
		
		teamsIS.put(ItemUtil.newItem(Language.getRawString("GUI.teams.item_leave"), Arrays.asList(" ", " ", " "), Material.BARRIER, 1), 40);
		for(ItemStack s : teamsIS.keySet()) inventory.setItem(teamsIS.get(s), s);
		
	}
	
}
//00 01 02 03 04 05 06 07 08
//09 10 11 12 13 14 15 16 17
//18 19 20 21 22 23 24 25 26
//27 28 29 30 31 32 33 34 35
//36 37 38 39 40 41 42 43 44
//45 46 47 48 49 50 51 52 53
