package ua.Endertainment.QuartzDefenders.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class TopManager {

	private QuartzDefenders plugin;
	
	private HashMap<Integer, OfflinePlayer> topKills = new HashMap<>();
	
	
	public TopManager(QuartzDefenders plugin) {
		this.plugin = plugin;
		
		calcKills();
	}
	
	/*
	 * KILLS
	 */	
	private void calcKills() {
		HashMap<Integer, OfflinePlayer> killsMap = new HashMap<>();
		
		for(String s : plugin.getConfigs().getStatsInfo().getKeys(false)) {
			killsMap.put(plugin.getConfigs().getStatsInfo().getInt(s + ".kills"), Bukkit.getOfflinePlayer(UUID.fromString(s)));
		}
	
		ArrayList<Integer> kills = new ArrayList<>();
		
		for(int i : killsMap.keySet()) {
			kills.add(i);
		}
		
		Collections.sort(kills, Collections.reverseOrder());
		
		for(int i = 0; i < kills.size(); i++) {
			topKills.put(i+1, killsMap.get(kills.get(i)));
		}		
	}	
	
	public OfflinePlayer getPlayerByPositionK(int position) {
		return topKills.get(position);
	}
	public int getPlayerPositionK(Player p) {
		for(Entry<Integer, OfflinePlayer> entry : topKills.entrySet()) {
			if(entry.getValue().getUniqueId().equals(p.getUniqueId())) {
				return entry.getKey();
			}
		}
		return topKills.keySet().size()+1;
	}
	public void refreshKillsTop() {
		calcKills();
	}
		
	
	
			
}
