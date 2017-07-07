package ua.Endertainment.QuartzDefenders.GUI;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.Kits.Kit;
import ua.Endertainment.QuartzDefenders.Kits.KitsManager;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

public class LobbyShopGUI {
	
	private Inventory inventory;
	
	private String title;
	
	private Player player;
	
	private int[] slots = {20,21,22,23,24,28,29,30,31,32,33,37,38,39,40,41,42};
	
	private ArrayList<ItemStack> items; 
	
	public LobbyShopGUI(Player player) {
		this.title = new ColorFormat("&2Shop").format();		
		this.inventory = Bukkit.createInventory(null, 6*9, title);		
		this.player = player;		
		
		this.items = new ArrayList<>();
		
		this.menuCorner();
		
		for(Kit kit : KitsManager.getInstance().getKits()) {
			ArrayList<String> l = new ArrayList<>();
			for(String s : kit.getDescription()) l.add(s);
			l.add(" ");
			l.add(KitsManager.getInstance().accessBuy(kit, player));
			ItemStack i = ItemUtil.newItem(kit.getName(), l, kit.getItems().get(0).clone());
			items.add(i);
		}
				
		
	}
	public Inventory getInventory() {
		return inventory;
	}
	
	public void openInventory() {
		a();
		player.openInventory(inventory);
	}
	
	private void menuCorner() {
		short z = 5, y = 13;
		int[] arg1 = {0,2,4,6,8,18,26,36,44,46,48,50,52}, arg2 = {1,3,5,7,9,17,27,35,45,47,49,51,53}; // z, y
		for(int a : arg1) inventory.setItem( a, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, z));
		for(int b : arg2) inventory.setItem( b, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, y));
	}
	
	private void a() {

		int index = 0;
		
		for(int i : slots) {
			if(index >= items.size()) return;
			inventory.setItem(i, items.get(index));
			index++;
		}
		
	}
	
//00 01 02 03 04 05 06 07 08
//09 10 11 12 13 14 15 16 17
//18 19 20 21 22 23 24 25 26
//27 28 29 30 31 32 33 34 35
//36 37 38 39 40 41 42 43 44
//45 46 47 48 49 50 51 52 53
	
}
