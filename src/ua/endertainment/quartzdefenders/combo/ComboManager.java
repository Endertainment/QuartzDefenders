package ua.endertainment.quartzdefenders.combo;

import java.util.HashMap;
import java.util.Map;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;

public class ComboManager {

	
	public enum Kills {
		ZERO, FIRST, DOUBLE, TRIPLE, ULTRA, RAMPAGE
	}
	
	private Map<GamePlayer, Combo> comboList;
	
	public ComboManager() {
		this.comboList = new HashMap<>();
	}	
	
	public Map<GamePlayer, Combo> getComboList() {
		return comboList;
	}
	
	public void start(GamePlayer p, Game game) {
		if(!comboList.containsKey(p)) {
			comboList.put(p, new Combo(p, game));
			return;
		}
		Combo c = comboList.get(p);
		if(c.compareTime()) {
			c.up();
		} else {
			c.reset();
			c.up();
		}
	}
	
}
