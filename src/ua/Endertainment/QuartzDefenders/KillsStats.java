package ua.Endertainment.QuartzDefenders;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import ua.Endertainment.QuartzDefenders.Utils.ChatCenterText;

public class KillsStats {

	private Game game;
	private Map<GamePlayer, Integer> kills;
	private TreeMap<GamePlayer, Integer> sorted_map;
	
	public KillsStats(Game game) {
		this.game = game;
		this.kills = new HashMap<GamePlayer, Integer>();
	}
	
	public Map<GamePlayer, Integer> getKillsMap() {
		return kills;
	}
	
	public void addKill(GamePlayer player) {
		if(kills.containsKey(player)) {
			kills.put(player, kills.get(player) + 1);
		} else {
			kills.put(player, 1);
		}
	}
	
	public void sendKillsStats() {
		calc();
		for(GamePlayer p : game.getPlayers()) {
			int pos = 1;
			p.sendMessage("&8-------------------- &2Kills &8--------------------");
			ChatCenterText.sendCenteredMessage(p, " ");
			for(GamePlayer pp : sorted_map.keySet()) {
				if(pos == 4) break;
				ChatCenterText.sendCenteredMessage(p, "&aTop " + pos + "&f: " + pp.getDisplayName() + "&f - &a" + kills.get(pp));
				pos++;
			}			
			ChatCenterText.sendCenteredMessage(p, " ");
			p.sendMessage("&8---------------------------------------------------");
		}
	}
	
	private void calc() {
		 ValueComparator bvc = new ValueComparator(kills);
	     sorted_map = new TreeMap<GamePlayer, Integer>(bvc);
	     sorted_map.putAll(kills);
	}
	
	class ValueComparator implements Comparator<GamePlayer> {
	    Map<GamePlayer, Integer> base;

	    public ValueComparator(Map<GamePlayer, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with
	    // equals.
	    public int compare(GamePlayer a, GamePlayer b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
}
