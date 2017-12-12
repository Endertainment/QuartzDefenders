package ua.endertainment.quartzdefenders.combo;

import java.util.HashMap;
import java.util.Map;

import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class ComboManager {

	private QuartzDefenders plugin;	
	
	public enum Kills {
		ZERO, FIRST, DOUBLE, TRIPLE, ULTRA, RAMPAGE, 
	}
	
	public ComboManager(QuartzDefenders plugin) {
		this.plugin = plugin;
		this.comboList = new HashMap<>();
	}
	
	private Map<GamePlayer, Combo> comboList;
	
	public Map<GamePlayer, Combo> getComboList() {
		return comboList;
	}
	
}
