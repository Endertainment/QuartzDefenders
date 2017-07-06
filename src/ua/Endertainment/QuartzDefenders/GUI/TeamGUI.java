package ua.Endertainment.QuartzDefenders.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

public class TeamGUI {
	
	private Inventory inventory;
	
	private Game game;
	
	private Player p;
	
	private String title;
	
	public TeamGUI(Game game, Player p) {
		this.game = game;
		this.p = p;
		this.title = new ColorFormat(ChatColor.BLUE + "Teams").format();
		this.inventory = Bukkit.createInventory(null, 6*9, title);
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
		short z = 3, y = 11;
		int[] arg1 = {0,2,4,6,8,18,26,36,44,46,48,50,52}, arg2 = {1,3,5,7,9,17,27,35,45,47,49,51,53}; // z, y
		for(int a : arg1) inventory.setItem( a, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, z));
		for(int b : arg2) inventory.setItem( b, ItemUtil.newItem(" ", Material.STAINED_GLASS_PANE, 1, y));
	}
	
	private void a() {
		HashMap<ItemStack, Integer> teamsIS = new HashMap<>();
		int x = 20;
		for(GameTeam team : game.getTeams().values()) {
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(" ");
			lore.add("&7Players: " + team.getColor() + team.getPlayersSize() + "&7/" + team.getColor() + team.intPlayersInTeam());
			lore.add(" ");
			ItemStack itemWool = ItemUtil.newItem(team.getColor() + "> " + team.getName() + " <", lore, Material.WOOL, 1, getWoolColor(team));
			if(x == 24) x = 29;
			teamsIS.put(itemWool, x);
			x++;
		}
		
		for(ItemStack s : teamsIS.keySet()) inventory.setItem(teamsIS.get(s), s);
	}
	
	private int getWoolColor(GameTeam team) {
		int x = 0;		
		String g = team.getDefName();
		if(g.equalsIgnoreCase("RED")) x = 14;  
		if(g.equalsIgnoreCase("BLUE")) x = 11;  
		if(g.equalsIgnoreCase("GREEN")) x = 5;  
		if(g.equalsIgnoreCase("YELLOW")) x = 4;  
		if(g.equalsIgnoreCase("MAGENTA")) x = 2;  
		if(g.equalsIgnoreCase("AQUA")) x = 9;  
		if(g.equalsIgnoreCase("WHITE")) x = 0;  
		if(g.equalsIgnoreCase("BLACK")) x = 15;  
		return x;
	}
}
