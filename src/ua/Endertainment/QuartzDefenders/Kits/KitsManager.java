package ua.Endertainment.QuartzDefenders.Kits;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class KitsManager {

	private static KitsManager kitsManager = new KitsManager();
	public static KitsManager getInstance() {
		return kitsManager;
	}
	
	private ArrayList<Kit> kits;
	
	private KitsManager() {
		kits = new ArrayList<Kit>();
		
		kits.add(new DefaultKit());
		kits.add(new ArcherKit());
	}
	
	public Kit getKit(String name) {
		for(Kit kit : kits) {
			if(kit.getName().equals(name)){
				return kit;
			}
		}
		return null;
	}
	public ArrayList<Kit> getKits() {
		return kits;
	}
	
	
	public void buyKitFailed(Kit kit, Player p) {
		String access = access(kit, p);
		if(access.contains("Available")) return;
		p.sendMessage(GameMsg.gameMessage("Shop", "You can not buy kit " + kit.getName() + "&7. Reason: " + access(kit, p)));
	}
	public void buyKit(Kit kit, Player p) {
		StatsPlayer sp = new StatsPlayer(p);
		int i = sp.getCoins() - kit.getPrice();
		sp.setCoins(i);
		FileConfiguration c = QuartzDefenders.getInstance().getConfigs().getKitsInfo();
		if(c.isList(p.getUniqueId().toString())) {
			ArrayList<String> l = (ArrayList<String>) c.getStringList(p.getUniqueId().toString());
			l.add(kit.getName());
			c.set(p.getUniqueId().toString(), l);
		} else {
			ArrayList<String> l = new ArrayList<>();
			l.add(kit.getName());
			c.set(p.getUniqueId().toString(), l);
		}
		QuartzDefenders.getInstance().getConfigs().saveKitsInfo();
		p.sendMessage(GameMsg.gameMessage("Shop", "You buy a new kit: &a" + kit.getName()));
	}
	
	public boolean isKitAccess(Kit kit, Player p) {
		FileConfiguration c = QuartzDefenders.getInstance().getConfigs().getKitsInfo();
		
		if(!c.isList(p.getUniqueId().toString())) return false;
		
		ArrayList<String> plKits = (ArrayList<String>) c.getStringList(p.getUniqueId().toString());		
		if(plKits.contains(kit.getName())) return true;
		
		return false;		
	}
	
	public boolean isKitAccessToBuy(Kit kit, Player p) {
		FileConfiguration c = QuartzDefenders.getInstance().getConfigs().getKitsInfo();
		
		if(c.isList(p.getUniqueId().toString())) {
			ArrayList<String> plKits = (ArrayList<String>) c.getStringList(p.getUniqueId().toString());		
			if(plKits.contains(kit.getName())) return false;
		}
		
		StatsPlayer sp = new StatsPlayer(p);
		int coins = sp.getCoins();
		int level = sp.getLevel();
		if(kit.getLevel() > level) return false;
		
		if(kit.getPrice() > coins) return false; 
		
		return true;		
	}
	
	public String access(Kit kit, Player p) {
		StatsPlayer sp = new StatsPlayer(p);
		int level = sp.getLevel();
		int coins = sp.getCoins();
		
		FileConfiguration c = QuartzDefenders.getInstance().getConfigs().getKitsInfo();
		
		if(c.isList(p.getUniqueId().toString())) {
			ArrayList<String> plKits = (ArrayList<String>) c.getStringList(p.getUniqueId().toString());		
			if(plKits.contains(kit.getName())) return "&aAvailable";
		}
		
		if(kit.getLevel() > level) return "&cUnavailable. Level request " + kit.getLevel();
		
		if(kit.getPrice() > coins) return "&cUnavailable. Price " + kit.getPrice();
		
		return "&aAvailable &7(Click to buy)";
	}
}
