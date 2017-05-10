package ua.Endertainment.QuartzDefenders.Kits;

import java.util.ArrayList;

public class KitsManager {

	private static KitsManager kitsManager = new KitsManager();
	public static KitsManager getInstance() {
		return kitsManager;
	}
	
	private ArrayList<Kit> kits;
	
	private KitsManager() {
		kits = new ArrayList<Kit>();
		
		//kits.add(new DefaultKit());
	}
	
	public Kit getKit(String name) {
		for(Kit kit : kits) {
			if(kit.getName().equals(name)){
				return kit;
			}
		}
		return null;
	}
}
