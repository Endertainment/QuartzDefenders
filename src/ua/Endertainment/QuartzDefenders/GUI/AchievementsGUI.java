package ua.Endertainment.QuartzDefenders.GUI;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.Dye;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Achievements.Achievement;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;
import ua.Endertainment.QuartzDefenders.Utils.Language;

public class AchievementsGUI {
	
	private Player player;
	
	private String title;
	
	private Inventory inventory;
	
	public AchievementsGUI(Player player) {
		this.player = player;
		this.title = Language.getString("GUI.achievements.name");
		this.inventory = Bukkit.createInventory(null, 9*6, title);
		
		this.menuCorner();
		
		this.a();
	}
	
	public void openInventory() {
		this.player.openInventory(inventory);
	}
	
	public void openInventory(Player p) {
		p.openInventory(inventory);
	}	
	
	private void menuCorner() {
		short z = Short.parseShort(Language.getString("GUI.achievements.glass_id_1")),
			  y = Short.parseShort(Language.getString("GUI.achievements.glass_id_2"));
		int[] arg1 = {0,2,4,6,8,18,26,36,44,46,48,50,52}, arg2 = {1,3,5,7,9,17,27,35,45,47,49,51,53}; // z, y
		for(int a : arg1) inventory.setItem( a, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, z));
		for(int b : arg2) inventory.setItem( b, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, y));
	}
	
	private void a() {
		int index = 10;
		for(Achievement achv : QuartzDefenders.getInstance().getAchievementsManager().getAchievements()) {
			boolean unlock = QuartzDefenders.getInstance().getAchievementsManager().isUnlocked(achv, player);
			Dye d = new Dye(unlock ? DyeColor.LIME : DyeColor.GRAY);
			
			String name = achv.isSecret() ? "&a???" : achv.getName();
			
			if(unlock) {
				name = achv.getName();
			}
			
			ArrayList<String> lore = new ArrayList<>();
			for(String s : achv.getDescription()) {
				lore.add(s);
			}
			
			inventory.setItem(index, ItemUtil.newItem(name, lore, d.toItemStack(1)));
			index++;
			if(index == 17) index = 19;
			if(index == 26) index = 28;
			if(index == 35) index = 37;
			if(index > 43) break;
		}
		
		
	}
	
	
}
